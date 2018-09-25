package com.unicom.acting.fee.batch.reader;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.domain.QryUserDatumIn;
import com.unicom.acting.batch.domain.UserDatum;
import com.unicom.acting.batch.service.BatchGetUserDatum;
import com.unicom.acting.fee.batch.domain.QryOweFeeHolder;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.skyark.component.exception.SkyArkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @Created on 2018-8-29.
 * @Author: zhengp11
 * @Version: 1.0
 * @Function:
 */
@StepScope
@Component("oweFeeInfoReader")
public class OweFeeInfoReader implements ItemReader<BTradeCommInfo<TradeCommInfo>> {

    private static final Logger logger = LoggerFactory.getLogger(OweFeeInfoReader.class);

    @Autowired
    private BatchGetUserDatum batchGetUserDatum;

    @Autowired
    private FeeCommService feeCommService;

    @Autowired
    private QryOweFeeHolder<FeeCommInfoIn> qryOweFeeHolder;

    private int count = 0;

    @PostConstruct
    public BTradeCommInfo<TradeCommInfo> read() throws ActBException {

        try{
            List<FeeCommInfoIn> orders = qryOweFeeHolder.getListInstance();
            if (null != orders && count < orders.size()) {
                FeeCommInfoIn feeCommInfoIn = qryOweFeeHolder.getListInstance().get(count);
                qryOweFeeHolder.setOrderNumber(count);
                count++;
                BTradeCommInfo btradeCommInfo = new BTradeCommInfo();
                btradeCommInfo.setOrderNumber(count);

                WriteOffRuleInfo writeOffRuleInfo = new WriteOffRuleInfo();
                TradeCommInfo tradeCommInfo = new TradeCommInfo();
                String provinceCode = feeCommInfoIn.getProvinceCode();
                String eparchyCode = feeCommInfoIn.getEparchyCode();
                String netTypeCode = feeCommInfoIn.getNetTypeCode();
                //查询三户资料
                QryUserDatumIn qryUserDatumIn = new QryUserDatumIn();
                qryUserDatumIn.setAcctId(feeCommInfoIn.getAcctId());
                qryUserDatumIn.setUserId(feeCommInfoIn.getUserId());
                qryUserDatumIn.setSerialNumber(feeCommInfoIn.getSerialNumber());
                qryUserDatumIn.setNetTypeCode(feeCommInfoIn.getNetTypeCode());
                qryUserDatumIn.setRemoveTag(feeCommInfoIn.getRemoveTag());
                UserDatum userInfo = batchGetUserDatum.getUserDatum(qryUserDatumIn);
                tradeCommInfo.setAccount(userInfo.getAccount());
                tradeCommInfo.setMainUser(userInfo.getMainUser());
                tradeCommInfo.setPayUsers(userInfo.getDefaultPayUsers());
                tradeCommInfo.setAllPayUsers(userInfo.getDefaultPayUsers());
                //获取销账规则
                feeCommService.getWriteOffRule(writeOffRuleInfo,provinceCode,eparchyCode,netTypeCode);
                tradeCommInfo.setWriteOffRuleInfo(writeOffRuleInfo);
                feeCommService.getEparchyCycleInfo(tradeCommInfo,eparchyCode,provinceCode);

                //获取账本信息
                feeCommService.getAcctBalance(feeCommInfoIn,tradeCommInfo);

                //获取地市账期信息
                feeCommService.getEparchyCycleInfo(tradeCommInfo,eparchyCode,provinceCode);

                //查询账单
                feeCommService.getOweBill(feeCommInfoIn, tradeCommInfo);
                //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
                if (!feeCommService.ifCalcLateFee(feeCommInfoIn, tradeCommInfo)) {
                    //计算滞纳金
                    tradeCommInfo.setCalcLateFee(true);
                    //获取滞纳金减免工单
                    feeCommService.getFeeDerateLateFeeLog(feeCommInfoIn, tradeCommInfo);
                    //获取账户自定义缴费期
                    feeCommService.getAcctPaymentCycle(tradeCommInfo, tradeCommInfo.getAccount().getAcctId());
                }
                btradeCommInfo.setTradeCommonInfo(tradeCommInfo);
                return btradeCommInfo;
            }
            else {
                return null;
            }
        } catch (ActBException e) {
            if(qryOweFeeHolder.getCurrentInstance()!= null) {
                logger.info("Exception::count = {}",qryOweFeeHolder.getOrderNumber());
            }
            logger.info("错误码：-1，错误信息：{}",e.getMessage());
            throw new ActBException("-1", e.getMessage());
        }

    }
}

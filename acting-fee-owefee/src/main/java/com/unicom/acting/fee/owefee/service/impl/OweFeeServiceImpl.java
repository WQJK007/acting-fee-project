package com.unicom.acting.fee.owefee.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.fee.writeoff.domain.*;
import com.unicom.acting.fee.writeoff.service.OweFeeCommService;
import com.unicom.acting.fee.writeoff.service.WriteOffRuleService;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.owefee.service.OweFeeService;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 欠费查询公共方法
 *
 * @author Administrators
 */
@Service
public class OweFeeServiceImpl implements OweFeeService {
    private static final Logger logger = LoggerFactory.getLogger(OweFeeServiceImpl.class);
    @Autowired
    private FeeCommService writeOffService;
    @Autowired
    private CalculateService calculateComponent;
    @Autowired
    private OweFeeCommService oweFeeCommService;
    @Autowired
    private WriteOffRuleService writeOffRuleService;

    @Override
    public void queryOweFee(FeeCommInfoIn feeCommInfoIn, TradeCommInfo tradeCommInfo) throws SkyArkException {
        //查询用户资料
        writeOffService.getUserDatumInfo(feeCommInfoIn, tradeCommInfo);
        //限制大合账用户查询
        if ("1".equals(feeCommInfoIn.getTradeEnter()) && feeCommInfoIn.isBigAcctRecvFee()) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "$$$30010$$$该账户为大合帐账户，请到大合帐页面进行处理!");
        }

        Account feeAccount = tradeCommInfo.getAccount();

        //查询账期信息
        writeOffService.getEparchyCycleInfo(tradeCommInfo,
                feeCommInfoIn.getEparchyCode(), feeAccount.getProvinceCode());

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //获取地市销账规则
        writeOffRuleService.getWriteOffRule(writeOffRuleInfo, feeAccount.getEparchyCode(), feeAccount.getProvinceCode(), feeAccount.getNetTypeCode());
        //查询账本
        writeOffService.getAcctBalance(feeCommInfoIn, tradeCommInfo);
        //查询账单
        writeOffService.getOweBill(feeCommInfoIn, tradeCommInfo);
        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!writeOffService.ifCalcLateFee(feeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            writeOffService.getFeeDerateLateFeeLog(feeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            writeOffService.getAcctPaymentCycle(tradeCommInfo, tradeCommInfo.getAccount().getAcctId());
        }
        //销账和结余计算
        calculateComponent.calc(tradeCommInfo);
    }

    @Override
    public TradeCommInfoOut queryOweFee(FeeCommInfoIn feeCommInfoIn) throws SkyArkException {
        TradeCommInfo tradeCommInfo = new TradeCommInfo();
        queryOweFee(feeCommInfoIn, tradeCommInfo);
        return oweFeeCommService.genOweFeeCommInfoOut(feeCommInfoIn, tradeCommInfo);
    }
}

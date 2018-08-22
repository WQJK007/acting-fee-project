package com.cbss.microservice.service.impl;

import com.cbss.microservice.domain.CycleOweFeeInfo;
import com.cbss.microservice.domain.ItemInfo;
import com.cbss.microservice.domain.OweFeeInfo;
import com.cbss.microservice.domain.QryOweFeeInfoRsp;
import com.cbss.microservice.service.QryOweFeeService;
import com.unicom.act.framework.exception.CbssException;
import com.unicom.act.framework.web.data.Para;
import com.unicom.acting.fee.domain.DetailBillInfo;
import com.unicom.acting.fee.domain.SubDetailBillInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.domain.TradeCommInfoOut;
import com.unicom.acting.fee.owefee.service.OweFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 欠费查询微服务
 *
 * @author shaob
 */
@Service
public class QryOweFeeServiceImpl implements QryOweFeeService {
    private static final Logger logger = LoggerFactory.getLogger(QryOweFeeServiceImpl.class);

    @Autowired
    private OweFeeService oweFeeService;

    @Override
    public List<QryOweFeeInfoRsp>  qryOweFeeInfo(TradeCommInfoIn tradeCommInfoIn)  throws CbssException {
        TradeCommInfoOut tradeCommInfoOut = oweFeeService.queryOweFee(tradeCommInfoIn);
        //组织返回数据
        List<QryOweFeeInfoRsp> qryOweFeeRspList = formatQryOweFee(tradeCommInfoOut);
        return  qryOweFeeRspList;

    }

    private List<QryOweFeeInfoRsp> formatQryOweFee(TradeCommInfoOut tradeCommInfoOut) {

        QryOweFeeInfoRsp qryOweFeeRsp = new QryOweFeeInfoRsp();
        OweFeeInfo oweFeeInfo = new OweFeeInfo();
        String serialNumber = tradeCommInfoOut.getSerialNumber();
        String acctId = tradeCommInfoOut.getAcctId();
        String userId = tradeCommInfoOut.getUserId();
        String provinceCode = tradeCommInfoOut.getProvinceCode();
        String payName = tradeCommInfoOut.getPayName();
        String netTypeCode = tradeCommInfoOut.getNetTypeCode();
        String eparchyCode = tradeCommInfoOut.getEparchyCode();
        String consignTag = tradeCommInfoOut.getCanConsignTag();
        List<SubDetailBillInfo> subDetailBillInfos = tradeCommInfoOut.getSubDetailBillInfos();
        List<DetailBillInfo> detailBillInfoList = tradeCommInfoOut.getDetailBillInfos();
        List<CycleOweFeeInfo> cycleOweFeeInfos = new ArrayList<>(detailBillInfoList.size());


        for (DetailBillInfo cycleBillInfo : detailBillInfoList) {
            CycleOweFeeInfo cycleOweFeeInfoTemp = new CycleOweFeeInfo();
            cycleOweFeeInfoTemp.setCycleId(String.valueOf(cycleBillInfo.getCycleId()));
            cycleOweFeeInfoTemp.setPayName(payName);
            cycleOweFeeInfoTemp.setServiceClassCode(netTypeCode);
            cycleOweFeeInfoTemp.setEparchyCode(eparchyCode);
            cycleOweFeeInfoTemp.setSerialNumber(serialNumber);
            cycleOweFeeInfoTemp.setUserId(userId);
            cycleOweFeeInfoTemp.setProvinceCode(provinceCode);
            cycleOweFeeInfoTemp.setFee(String.valueOf(cycleBillInfo.getMonthfee()));
            cycleOweFeeInfoTemp.setReceivedFee(String .valueOf(cycleBillInfo.getPayFee()));
            cycleOweFeeInfoTemp.setLateFee(String.valueOf(cycleBillInfo.getMonthLateFee()));
            cycleOweFeeInfoTemp.setReceivedLateFee(String.valueOf(cycleBillInfo.getPayLateFee()));
            cycleOweFeeInfoTemp.setBalance(String.valueOf(cycleBillInfo.getSumDebFee()));
            cycleOweFeeInfoTemp.setBadDebtTag("0");
            cycleOweFeeInfoTemp.setBadDebtRemark("非呆坏账");
            cycleOweFeeInfoTemp.setConsignTag( "1".equals(consignTag) ? "0" : "5");
            cycleOweFeeInfoTemp.setConsignRemark("1".equals(consignTag) ? "托收在途" : "正常");
            List<ItemInfo> itemInfostemp = new ArrayList<>();
            for (SubDetailBillInfo subBill : subDetailBillInfos) {
                if(subBill.getCycleIdb() == cycleBillInfo.getCycleId()){
                    ItemInfo itemInfoTemp = new ItemInfo();
                    itemInfoTemp.setIntegrateItemCode(String.valueOf(subBill.getIntegrateItemCode()));
                    itemInfoTemp.setIntegrateItem(subBill.getIntegrateItem());
                    itemInfoTemp.setIntegrateItemCodeType("0");
                    itemInfoTemp.setFee(String.valueOf(subBill.getFee()));
                    long recvedFee = subBill.getFee() - subBill.getBalance();
                    itemInfoTemp.setReceivedFee(String.valueOf(recvedFee));
                    itemInfoTemp.setBalance(String.valueOf(subBill.getBalance()));
                    itemInfostemp.add(itemInfoTemp);
                }
            }
            cycleOweFeeInfoTemp.setItemInfo(itemInfostemp);
            cycleOweFeeInfos.add(cycleOweFeeInfoTemp);
        }
        oweFeeInfo.setCycleOweFeeInfo(cycleOweFeeInfos);
        oweFeeInfo.setPayName(payName);
        oweFeeInfo.setServiceClassCode(netTypeCode);
        oweFeeInfo.setAreaCode(eparchyCode);
        oweFeeInfo.setSerialNumber(serialNumber);
        oweFeeInfo.setAcctId(acctId);
        oweFeeInfo.setUserId(userId);
        oweFeeInfo.setProvinceCode(provinceCode);
        oweFeeInfo.setEparchyCode(eparchyCode);
        oweFeeInfo.setBalanceFee(tradeCommInfoOut.getAllNewBalance());
        //最小应缴 刨除实时费用
        long spayFee = Long.parseLong(tradeCommInfoOut.getSpayFee());
        long realFee = Long.parseLong(tradeCommInfoOut.getAllROweFee());
        oweFeeInfo.setMinPayFee(String.valueOf((spayFee - realFee) > 0 ? (spayFee - realFee) : 0 ));
        oweFeeInfo.setPayFee(tradeCommInfoOut.getSpayFee());
        oweFeeInfo.setTotalFee(tradeCommInfoOut.getAllNewBOweFee());
        oweFeeInfo.setRealFee(tradeCommInfoOut.getAllROweFee());
        oweFeeInfo.setMustPay("0");
        List<Para> paras = new ArrayList<>();

        Para para1 = new Para();
        para1.setParaId("REAL_BALANCE");
        para1.setParaValue(tradeCommInfoOut.getAllBalance());
        paras.add(para1);

        //专项可用预存款
        Para para2 = new Para();
        para2.setParaId("SPECIAL_FEE");
        if("1".equals(tradeCommInfoOut.getConTactType()) || "1".equals(tradeCommInfoOut.getSpecialfeeConTactType())) {
            para2.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getAvailSpePreFee()));
        }else {
            para2.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getCurAvailSpePreFee()));
        }
        paras.add(para2);

        //专项可用赠款 availSpeGrants
        Para para3 = new Para();
        para3.setParaId("SPECIAL_PRESENTFEE");
        if("1".equals(tradeCommInfoOut.getConTactType()) || "1".equals(tradeCommInfoOut.getSpecialfeeConTactType())) {
            para3.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getAvailSpeGrants()));
        }else {
            para3.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getCurAvailSpeGrants()));
        }
        paras.add(para3);

        //专项冻结预存款  frozenSpePreFee
        Para para4 = new Para();
        para4.setParaId("SPECIAL_FREEZEFEE");
        para4.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getFrozenSpePreFee()));
        paras.add(para4);

        //专项冻结赠款  frozenSpeGrants
        Para para5 = new Para();
        para5.setParaId("SPECIAL_FRZPRETFEE");
        para5.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getFrozenSpeGrants()));
        paras.add(para5);

        //可用额度：avialCredit 信用额度：TOTAL_CREDIT_VALUE
        Para para6 = new Para();
        para6.setParaId("AVAILABLE_CREVALUE");
        para6.setParaValue(String.valueOf(tradeCommInfoOut.getUnifiedBalanceInfo().getAvialCredit()));
        paras.add(para6);
        Para para7 = new Para();
        para7.setParaId("TOTAL_CREDIT_VALUE");
        para7.setParaValue(String.valueOf(tradeCommInfoOut.getCreditValue()));
        paras.add(para7);
        oweFeeInfo.setPara(paras);
        qryOweFeeRsp.setOweFeeInfo(oweFeeInfo);
        List<QryOweFeeInfoRsp> qryOweFeeRsps = Collections.singletonList(qryOweFeeRsp);
        return qryOweFeeRsps;
    }
}

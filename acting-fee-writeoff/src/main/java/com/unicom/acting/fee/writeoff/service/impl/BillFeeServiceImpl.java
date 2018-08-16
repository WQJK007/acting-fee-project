package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.calc.domain.BillDefaultSortRule;
import com.unicom.acting.fee.dao.BillFeeDao;
import com.unicom.acting.fee.domain.ActPayPubDef;
import com.unicom.acting.fee.domain.Bill;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.RealBillRspInfo;
import com.unicom.acting.fee.writeoff.service.BillFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 对实时账单，往月账单和坏账账单等账单表的操作
 *
 * @author Administrators
 */
@Service
public class BillFeeServiceImpl implements BillFeeService {
    private Logger logger = LoggerFactory.getLogger(BillFeeServiceImpl.class);
    @Autowired
    private BillFeeDao billFeeDao;
    @Autowired
    private RestClient restClient;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    @Override
    public List<Bill> getRealBillFromMemDB(TradeCommInfoIn tradeCommInfoIn, String acctId, String userId, int startCycleId, int endCycleId) {
        RequestEntity requestEntity = new RequestEntity();
        String[] param = new String[]{tradeCommInfoIn.getProvinceCode(), tradeCommInfoIn.getEparchyCode()};
        requestEntity.setUriPaths(param);
        //组织请求参数
        Map<String, String> reqParam = new HashMap<>();
        reqParam.put("startCycle", String.valueOf(startCycleId));
        reqParam.put("endCycle", String.valueOf(endCycleId));
        reqParam.put("acctId", acctId);
        reqParam.put("userId", userId);

        //按用户查询实时账单
        if ("1".equals(tradeCommInfoIn.getQryBillType())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            reqParam.put("queryMode", "2");
        }

        requestEntity.setUriParams(reqParam);
        //用户资料查询公共微服务
        Rsp rsp = this.restClient.callSkyArkMicroService("accounting", ActPayPubDef.QRY_REAL_BILL,
                HttpMethod.GET, requestEntity, tradeCommInfoIn.getHeaderGray());
        if (!SysTypes.SYS_SUCCESS_CODE.equals(rsp.getRspCode())) {
            throw new SkyArkException(rsp.getRspCode(), rsp.getRspDesc());
        } else {
            if (CollectionUtils.isEmpty(rsp.getData())) {
                return Collections.emptyList();
            }
            List<RealBillRspInfo> realBillRspInfoList = JsonUtil.transMapsToObjs(rsp.getData(), RealBillRspInfo.class);
            if (CollectionUtils.isEmpty(realBillRspInfoList)) {
                return Collections.emptyList();
            }
            List<Bill> realBillList = new ArrayList<>();
            for (RealBillRspInfo realBillRspInfo : realBillRspInfoList) {
                Bill realBill = new Bill();
                realBill.setAcctId(realBillRspInfo.getAcctId());
                realBill.setUserId(realBillRspInfo.getUserId());
                realBill.setCycleId(realBillRspInfo.getCycleId());
                realBill.setBillId("");
                realBill.setIntegrateItemCode(realBillRspInfo.getIntegrateItemCode());
                realBill.setFee(realBillRspInfo.getFee());
                realBill.setBalance(realBillRspInfo.getBalance());
                realBill.setbDiscnt(realBillRspInfo.getbDiscnt());
                realBill.setaDiscnt(realBillRspInfo.getaDiscnt());
                realBill.setAdjustBefore(realBillRspInfo.getAdjustBefore());
                realBill.setAdjustAfter(realBillRspInfo.getAdjustAfter());
                if (!StringUtil.isEmptyCheckNullStr(realBillRspInfo.getUpdateTime())) {
                    realBill.setUpdateTime(realBillRspInfo.getUpdateTime());
                }
                realBill.setCanpayTag(realBillRspInfo.getCanpayTag());
                realBill.setPayTag(realBillRspInfo.getPayTag());
                realBill.setBillPayTag(realBillRspInfo.getBillPayTag());
                realBill.setPrepayTag(realBillRspInfo.getPrepayTag());
                realBillList.add(realBill);
            }
            return realBillList;
        }
    }

    @Override
    public List<Bill> getBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        return billFeeDao.getBillOweByAcctId(acctId, startCycleId, endCycleId, provinceCode);
    }

    @Override
    public List<Bill> getBillOweByUserId(String acctId, String userId, int startCycleId, int endCycleId, String provinceCode) {
        return billFeeDao.getBillOweByUserId(acctId, userId, startCycleId, endCycleId, provinceCode);
    }

    @Override
    public List<Bill> getBadBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        return billFeeDao.getBadBillOweByAcctId(acctId, startCycleId, endCycleId, provinceCode);
    }

    @Override
    public boolean hasPreCycleBillByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        return billFeeDao.getBillByAcctId(acctId, startCycleId, endCycleId, provinceCode);
    }

    @Override
    public List<Bill> removeWriteOffRealBill(List<Bill> billList, List<Bill> realBillList, String acctId, int preCurCycleId, String provinceCode) {
        //往月账单中是否存在待开帐账期账单
        boolean hasPreCycleBills = false;
        //判断是否有未销帐的帐单
        for (Bill bill : billList) {
            if (preCurCycleId == bill.getCycleId()) {
                hasPreCycleBills = true;
                break;
            }
        }

        //没有未销帐的帐单，判断是否有销帐的帐单
        if (!hasPreCycleBills) {
            if (hasPreCycleBillByAcctId(acctId, preCurCycleId, preCurCycleId, ActPayPubDef.ACTING_DRDS_DBCONN)) {
                hasPreCycleBills = true;
            }
        }

        //剔除对应月份的账目
        if (hasPreCycleBills) {
            for (Iterator itr = realBillList.iterator(); itr.hasNext(); ) {
                Bill bill = (Bill) itr.next();
                if (preCurCycleId == bill.getCycleId()) {
                    itr.remove();
                }
            }
        }
        return realBillList;
    }

    @Override
    public void updateRealBillId(List<Bill> realBillList, int billIdCount, String eparchyCode, String provinceCode) {
        //实时账单排序
        realBillList.sort(new BillDefaultSortRule());

        //账单序列数
        List<String> billIdList = sysCommOperFeeService.getSequence(eparchyCode,
                ActPayPubDef.SEQ_BILL_ID, billIdCount, provinceCode);

        String tmpUserId = realBillList.get(0).getUserId();
        int cycleId = realBillList.get(0).getCycleId();
        //更新BILLID开始的下标值
        int upBillIdBeginIndex = 0;
        //BILLID序列下标
        int billIndex = 0;
        for (int i = 0; i < realBillList.size(); i++) {
            //用户标识和账期标识只要有一个不一样就更新BILL_ID
            if (!tmpUserId.equals(realBillList.get(i).getUserId())
                    || cycleId != realBillList.get(i).getCycleId()) {
                String billId = "";
                if (billIndex < billIdList.size()) {
                    billId = billIdList.get(billIndex);
                    billIndex++;
                } else {
                    billId = sysCommOperFeeService.getSequence(eparchyCode, ActPayPubDef.SEQ_BILL_ID, provinceCode);
                }

                for (int k = upBillIdBeginIndex; k < i; ++k) {
                    realBillList.get(k).setBillId(billId);
                }
                upBillIdBeginIndex = i;
            }
            tmpUserId = realBillList.get(i).getUserId();
            cycleId = realBillList.get(i).getCycleId();
        }

        String billId = "";
        if (billIndex < billIdList.size()) {
            billId = billIdList.get(billIndex);
        } else {
            billId = sysCommOperFeeService.getSequence(eparchyCode, ActPayPubDef.SEQ_BILL_ID, provinceCode);
        }

        //设置最后一个用户或者最后一个账期账单BILLID
        for (int m = upBillIdBeginIndex; m < realBillList.size(); ++m) {
            realBillList.get(m).setBillId(billId);
        }
    }
}

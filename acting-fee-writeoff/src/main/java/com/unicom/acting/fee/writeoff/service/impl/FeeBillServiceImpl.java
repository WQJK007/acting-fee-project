package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.FeeBillDao;
import com.unicom.acting.fee.domain.ActingFeePubDef;
import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.domain.FeeBillDefaultSortRule;
import com.unicom.acting.fee.writeoff.domain.RealBillRspInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.fee.writeoff.service.FeeBillService;
import com.unicom.acting.fee.writeoff.service.RealBillService;
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
public class FeeBillServiceImpl implements FeeBillService {
    private Logger logger = LoggerFactory.getLogger(FeeBillServiceImpl.class);
    @Autowired
    private FeeBillDao feeBillDao;
    @Autowired
    private RestClient restClient;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    @Override
    public List<FeeBill> getRealBillFromMemDB(TradeCommInfoIn tradeCommInfoIn, String acctId, String userId, int startCycleId, int endCycleId) {
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
        Rsp rsp = this.restClient.callSkyArkMicroService("accounting", ActingFeePubDef.QRY_REAL_BILL,
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
            List<FeeBill> realFeeBillList = new ArrayList<>();
            for (RealBillRspInfo realBillRspInfo : realBillRspInfoList) {
                FeeBill realFeeBill = new FeeBill();
                realFeeBill.setAcctId(realBillRspInfo.getAcctId());
                realFeeBill.setUserId(realBillRspInfo.getUserId());
                realFeeBill.setCycleId(realBillRspInfo.getCycleId());
                realFeeBill.setBillId("");
                realFeeBill.setIntegrateItemCode(realBillRspInfo.getIntegrateItemCode());
                realFeeBill.setFee(realBillRspInfo.getFee());
                realFeeBill.setBalance(realBillRspInfo.getBalance());
                realFeeBill.setbDiscnt(realBillRspInfo.getbDiscnt());
                realFeeBill.setaDiscnt(realBillRspInfo.getaDiscnt());
                realFeeBill.setAdjustBefore(realBillRspInfo.getAdjustBefore());
                realFeeBill.setAdjustAfter(realBillRspInfo.getAdjustAfter());
                if (!StringUtil.isEmptyCheckNullStr(realBillRspInfo.getUpdateTime())) {
                    realFeeBill.setUpdateTime(realBillRspInfo.getUpdateTime());
                }
                realFeeBill.setCanpayTag(realBillRspInfo.getCanpayTag());
                realFeeBill.setPayTag(realBillRspInfo.getPayTag());
                realFeeBill.setBillPayTag(realBillRspInfo.getBillPayTag());
                realFeeBill.setPrepayTag(realBillRspInfo.getPrepayTag());
                realFeeBillList.add(realFeeBill);
            }
            return realFeeBillList;
        }
    }

    @Override
    public List<FeeBill> getBillOweByAcctId(String acctId, int startCycleId, int endCycleId) {
        return feeBillDao.getBillOweByAcctId(acctId, startCycleId, endCycleId);
    }

    @Override
    public List<FeeBill> getBillOweByUserId(String acctId, String userId, int startCycleId, int endCycleId) {
        return feeBillDao.getBillOweByUserId(acctId, userId, startCycleId, endCycleId);
    }

    @Override
    public List<FeeBill> getBadBillOweByAcctId(String acctId, int startCycleId, int endCycleId) {
        return feeBillDao.getBadBillOweByAcctId(acctId, startCycleId, endCycleId);
    }

    @Override
    public boolean hasPreCycleBillByAcctId(String acctId, int startCycleId, int endCycleId) {
        return feeBillDao.getBillByAcctId(acctId, startCycleId, endCycleId);
    }

    @Override
    public List<FeeBill> removeWriteOffRealBill(List<FeeBill> feeBillList, List<FeeBill> realFeeBillList, String acctId, int preCurCycleId) {
        //往月账单中是否存在待开帐账期账单
        boolean hasPreCycleBills = false;
        //判断是否有未销帐的帐单
        for (FeeBill feeBill : feeBillList) {
            if (preCurCycleId == feeBill.getCycleId()) {
                hasPreCycleBills = true;
                break;
            }
        }

        //没有未销帐的帐单，判断是否有销帐的帐单
        if (!hasPreCycleBills) {
            if (hasPreCycleBillByAcctId(acctId, preCurCycleId, preCurCycleId)) {
                hasPreCycleBills = true;
            }
        }

        //剔除对应月份的账目
        if (hasPreCycleBills) {
            for (Iterator itr = realFeeBillList.iterator(); itr.hasNext(); ) {
                FeeBill feeBill = (FeeBill) itr.next();
                if (preCurCycleId == feeBill.getCycleId()) {
                    itr.remove();
                }
            }
        }
        return realFeeBillList;
    }

    @Override
    public void updateRealBillId(List<FeeBill> realFeeBillList, int billIdCount, String eparchyCode, String provinceCode) {
        //实时账单排序
        realFeeBillList.sort(new FeeBillDefaultSortRule());

        //账单序列数
        List<String> billIdList = sysCommOperFeeService.getActingSequence(ActingFeePubDef.SEQ_BILLID_TABNAME,
                ActingFeePubDef.SEQ_BILLID_COLUMNNAME, billIdCount, provinceCode);

        String tmpUserId = realFeeBillList.get(0).getUserId();
        int cycleId = realFeeBillList.get(0).getCycleId();
        //更新BILLID开始的下标值
        int upBillIdBeginIndex = 0;
        //BILLID序列下标
        int billIndex = 0;
        for (int i = 0; i < realFeeBillList.size(); i++) {
            //用户标识和账期标识只要有一个不一样就更新BILL_ID
            if (!tmpUserId.equals(realFeeBillList.get(i).getUserId())
                    || cycleId != realFeeBillList.get(i).getCycleId()) {
                String billId = "";
                if (billIndex < billIdList.size()) {
                    billId = billIdList.get(billIndex);
                    billIndex++;
                } else {
                    billId = sysCommOperFeeService.getActingSequence(ActingFeePubDef.SEQ_BILLID_TABNAME,
                            ActingFeePubDef.SEQ_BILLID_COLUMNNAME, provinceCode);
                }

                for (int k = upBillIdBeginIndex; k < i; ++k) {
                    realFeeBillList.get(k).setBillId(billId);
                }
                upBillIdBeginIndex = i;
            }
            tmpUserId = realFeeBillList.get(i).getUserId();
            cycleId = realFeeBillList.get(i).getCycleId();
        }

        String billId = "";
        if (billIndex < billIdList.size()) {
            billId = billIdList.get(billIndex);
        } else {
            billId = sysCommOperFeeService.getActingSequence(ActingFeePubDef.SEQ_BILLID_TABNAME,
                    ActingFeePubDef.SEQ_BILLID_COLUMNNAME, provinceCode);
        }

        //设置最后一个用户或者最后一个账期账单BILLID
        for (int m = upBillIdBeginIndex; m < realFeeBillList.size(); ++m) {
            realFeeBillList.get(m).setBillId(billId);
        }
    }
}

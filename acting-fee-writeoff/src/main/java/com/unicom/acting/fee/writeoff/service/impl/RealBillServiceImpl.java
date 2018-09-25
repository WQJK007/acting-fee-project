package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.domain.ActingFeePubDef;
import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.writeoff.domain.RealBillRspInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.fee.writeoff.service.RealBillService;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@ConditionalOnProperty(name = "batchMode", havingValue = "false", matchIfMissing = true)
public class RealBillServiceImpl implements RealBillService {
    @Autowired
    private RestClient restClient;

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
        }

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

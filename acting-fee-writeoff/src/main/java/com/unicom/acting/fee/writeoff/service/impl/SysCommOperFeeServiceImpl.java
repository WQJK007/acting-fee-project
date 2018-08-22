package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.domain.ActingFeePubDef;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统常用公共操作方式
 *
 * @author Administrators
 */
@Service
public class SysCommOperFeeServiceImpl implements SysCommOperFeeService {
    @Autowired
    private RestClient restClient;

    @Override
    public String getSysdate(String fmt) {
        return new SimpleDateFormat(fmt).format(new Date());
    }

    @Override
    public String getTradeSequence(String eparchyCode, String provinceCode) {
        List<String> sqeList = getSequence("0", ActingFeePubDef.SEQ_TRADE_ID, 0, eparchyCode, provinceCode);
        if (!CollectionUtils.isEmpty(sqeList)) {
            return sqeList.get(0);
        }
        return "";
    }

    @Override
    public String getSequence(String eparchyCode, String seqName, String provinceCode) {
        List<String> sqeList = getSequence("1", seqName, 0, eparchyCode, provinceCode);
        if (CollectionUtils.isEmpty(sqeList)) {
            throw new SkyArkException("获取" + seqName + "流水失败!");
        }
        return sqeList.get(0);
    }

    @Override
    public List<String> getSequence(String eparchyCode, String seqName, long preCount, String provinceCode) {
        List<String> sqeList = getSequence("2", seqName, preCount, eparchyCode, provinceCode);
        if (CollectionUtils.isEmpty(sqeList)) {
            throw new SkyArkException("获取" + seqName + "流水失败!");
        }
        if (sqeList.size() != preCount) {
            throw new SkyArkException("获取序列" + seqName + "个数和实际需求个数不一致!");
        }
        return sqeList;
    }

    private List<String> getSequence(String sqeType, String sqeName, long preCount, String eparchyCode, String provinceCode) {
        RequestEntity requestEntity = new RequestEntity();
        String[] param = new String[]{provinceCode, eparchyCode};
        requestEntity.setUriPaths(param);
        //组织请求参数
        Map<String, String> reqParam = new HashMap<>();
        reqParam.put("seqType", sqeType);
        reqParam.put("seqName", sqeName);
        reqParam.put("preCount", String.valueOf(preCount));
        requestEntity.setUriParams(reqParam);
        Rsp rsp = restClient.callSkyArkMicroService("accounting", ActingFeePubDef.GET_SEQUENCE, HttpMethod.GET, requestEntity);
        if (!SysTypes.SYS_SUCCESS_CODE.equals(rsp.getRspCode())) {
            throw new SkyArkException(rsp.getRspCode(), rsp.getRspDesc());
        } else {
            if (CollectionUtils.isEmpty(rsp.getData())) {
                return Collections.emptyList();
            }
            return JsonUtil.transMapsToObjs(rsp.getData(), String.class);
        }
    }
}

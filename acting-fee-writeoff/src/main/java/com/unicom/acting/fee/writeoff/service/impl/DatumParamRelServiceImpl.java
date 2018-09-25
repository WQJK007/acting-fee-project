package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.writeoff.domain.*;
import com.unicom.acting.fee.writeoff.service.DatumParamRelService;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.web.WebTypes;
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

@Service
public class DatumParamRelServiceImpl implements DatumParamRelService {
    private static final Logger logger = LoggerFactory.getLogger(DatumParamRelServiceImpl.class);
    @Autowired
    private RestClient restClient;

    @Override
    public UserDatumInfo getUserDatum(TradeCommInfoIn tradeCommInfoIn) {
        RequestEntity requestEntity = new RequestEntity();
        //组织请求参数
        Map<String, String> reqParam = new HashMap<>();
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getAcctId())) {
            reqParam.put("ACCT_ID", tradeCommInfoIn.getAcctId());

            if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getSerialNumber())) {
                reqParam.put("SERIAL_NUMBER", tradeCommInfoIn.getSerialNumber());
            }

            if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getUserId())) {
                reqParam.put("USER_ID", tradeCommInfoIn.getUserId());
            }

        } else {
            if (StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getSerialNumber())
                    && StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getUserId())) {
                throw new SkyArkException("服务号码和用户标识不能都为空!");
            }
            if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getSerialNumber())) {
                reqParam.put("SERIAL_NUMBER", tradeCommInfoIn.getSerialNumber());
                if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getNetTypeCode())) {
                    reqParam.put("NET_TYPE_CODE", tradeCommInfoIn.getNetTypeCode());
                }
                if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getRemoveTag())) {
                    reqParam.put("REMOVE_TAG", tradeCommInfoIn.getRemoveTag());
                }
            } else {
                reqParam.put("USER_ID", tradeCommInfoIn.getUserId());
            }
        }

        //校验大合帐用户
        reqParam.put("DHZ_TAG", "1");

        requestEntity.setUriParams(reqParam);

        //调用账户中心账户账务交易查询微服务
        Rsp rsp = restClient.callSkyArkMicroService(WebTypes.ACTS_CENTER,
                "payuserdatum", HttpMethod.GET, requestEntity, tradeCommInfoIn.getHeaderGray());

        List<DatumRsp> datumRsps = new ArrayList();
        if (WebTypes.STATUS_SUCCESS.equals(rsp.getRspCode())) {
            if (!CollectionUtils.isEmpty(rsp.getData())) {
                datumRsps = JsonUtil.transMapsToObjs(rsp.getData(), DatumRsp.class);
                if (CollectionUtils.isEmpty(datumRsps)) {
                    throw new SkyArkException("没有查询到用户资料！");
                }
            } else {
                throw new SkyArkException("没有查询到用户资料！");
            }
        } else {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, rsp.getRspDesc());
        }

        return formateUserDatum(datumRsps.get(0));
    }

    @Override
    public List<UserRelationRsp> getUserRelation(String userId, String memberRoleId, boolean islike, List<UserRelationReqDetailInfo> relationTypes, String headerGray) {
        if (StringUtil.isEmptyCheckNullStr(userId)
                && StringUtil.isEmptyCheckNullStr(memberRoleId)) {
            throw new SkyArkException("群组关系查询的用户编码和成员角色编码不能都为空");
        }
        //设置用户群组关系查询参数
        UserRelationReqDetail userRelationReqDetail = new UserRelationReqDetail();
        if (!StringUtil.isEmptyCheckNullStr(userId)) {
            userRelationReqDetail.setUserId(userId);
        } else {
            userRelationReqDetail.setMemberRoleId(memberRoleId);
        }
        if (islike) {
            userRelationReqDetail.setIslike("1");
        }
        userRelationReqDetail.setRelationTypeInfo(relationTypes);
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestBody(userRelationReqDetail);
        //调用群组关系查询微服务
        Rsp rsp = restClient.callSkyArkMicroService(WebTypes.USERS_CENTER, "relationinfo", HttpMethod.POST, requestEntity, headerGray);
        if (WebTypes.STATUS_SUCCESS.equals(rsp.getRspCode())) {
            //查询成功，获取data节点信息
            List userDatumList = rsp.getData();
            if (!CollectionUtils.isEmpty(userDatumList)) {
                return JsonUtil.transMapsToObjs(userDatumList, UserRelationRsp.class);
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, rsp.getRspDesc());
        }
    }

    @Override
    public List<UserParamRsp> getUserParam(List<UserParamReqDetailInfo> detailInfos, String headerGray) {
        UserParamReqDetail userInfo = new UserParamReqDetail();
        userInfo.setUserInfo(detailInfos);
        UserParamReq userParamReq = new UserParamReq();
        userParamReq.setReq(userInfo);
        RequestEntity requestEntity = new RequestEntity();
        Rsp rsp = restClient.callSkyArkMicroService(WebTypes.USERS_CENTER, "useriteminfo", HttpMethod.POST, requestEntity, headerGray);
        if (WebTypes.STATUS_SUCCESS.equals(rsp.getRspCode())) {
            //查询成功，获取data节点信息
            List userDatumList = rsp.getData();
            if (!CollectionUtils.isEmpty(userDatumList)) {
                return JsonUtil.transMapsToObjs(userDatumList, UserParamRsp.class);
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, rsp.getRspDesc());
        }
    }


    /**
     * 三户信息转换
     *
     * @param datumRsp
     * @return
     */
    private UserDatumInfo formateUserDatum(DatumRsp datumRsp) {
        DatumAccount datumAccount = datumRsp.getAccount();
        DatumUser datumUser = datumRsp.getMainUser();
        logger.info("account = " + datumAccount.toString());
        logger.info("mainUserInfo = " + datumUser.toString());
        if (datumAccount == null || datumUser == null) {
            throw new SkyArkException("调用用户资料查询微服务异常，没有返回三户信息");
        }
        UserDatumInfo userDatumInfo = new UserDatumInfo();
        //设置账户信息
        Account account = new Account();
        account.setAcctId(datumAccount.getAcctId());
        account.setCustId(datumAccount.getCustId());
        account.setProvinceCode(datumAccount.getProvinceCode());
        account.setEparchyCode(datumAccount.getEparchyCode());
        account.setCityCode(datumAccount.getCityCode());
        account.setNetTypeCode(setValue(datumAccount.getNetTypeCode()));
        account.setPayModeCode(setValue(datumAccount.getPayModeCode()));
        account.setPayName(datumAccount.getPayName());
        userDatumInfo.setAccount(account);

        //设置用户信息
        User mainUser = new User();
        mainUser.setUserId(datumUser.getUserId());
        mainUser.setSerialNumber(datumUser.getSerialNumber());
        mainUser.setNetTypeCode(datumUser.getNetTypeCode());
        mainUser.setProvinceCode(datumUser.getProvinceCode());
        mainUser.setEparchyCode(datumUser.getEparchyCode());
        mainUser.setOpenMode(datumUser.getOpenMode());

        if (!StringUtil.isEmptyCheckNullStr(datumUser.getCreditValue())) {
            mainUser.setCreditValue(Long.parseLong(datumUser.getCreditValue()));
        }
        mainUser.setRemoveTag(datumUser.getRemoveTag());
        mainUser.setServiceStateCode(datumUser.getServiceStateCode());
        mainUser.setPrepayTag(datumUser.getPrepayTag());
        mainUser.setBrandCode(datumUser.getBrandCode());
        if (!StringUtil.isEmptyCheckNullStr(datumUser.getDestroyDate())) {
            mainUser.setDestroyDate(datumUser.getDestroyDate());
        } else {
            mainUser.setDestroyDate("");
        }
        userDatumInfo.setMainUser(mainUser);

        if ("1".equals(datumAccount.getDhzFlag())) {
            //大合账用户走异步缴费
            userDatumInfo.setBigAcct(true);
            return userDatumInfo;
        } else {
            //非大合帐用户缴费
            userDatumInfo.setBigAcct(false);
        }

        if (StringUtil.isEmptyCheckNullStr(account.getAcctId())
                || StringUtil.isEmptyCheckNullStr(mainUser.getUserId())
                || StringUtil.isEmptyCheckNullStr(mainUser.getSerialNumber())) {
            throw new SkyArkException("没有查询到账户资料！");
        }

        if (CollectionUtils.isEmpty(datumRsp.getDefaultPayUsers())) {
            throw new SkyArkException("该帐户下的用户付费关系都已失效，请按照服务号码或者用户标识进行缴费");
        }
        //默认付费用户信息
        List<User> defaultPayUserList = new ArrayList(datumRsp.getDefaultPayUsers().size());
        for (DatumUser defaultUser : datumRsp.getDefaultPayUsers()) {
            User user = new User();
            user.setUserId(defaultUser.getUserId());
            user.setDestroyDate(defaultUser.getDestroyDate());
            user.setEparchyCode(defaultUser.getEparchyCode());
            user.setSerialNumber(defaultUser.getSerialNumber());
            user.setNetTypeCode(defaultUser.getNetTypeCode());
            user.setRemoveTag(defaultUser.getRemoveTag());
            defaultPayUserList.add(user);
        }
        userDatumInfo.setDefaultPayUsers(defaultPayUserList);
        return userDatumInfo;
    }

    private String setValue(String value) {
        return StringUtil.isEmptyCheckNullStr(value)?"" : value;
    }

}

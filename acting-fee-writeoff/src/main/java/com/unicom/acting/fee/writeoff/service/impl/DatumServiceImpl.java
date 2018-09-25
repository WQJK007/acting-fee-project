package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.FeeAccountDao;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.*;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import com.unicom.acting.fee.dao.FeeUserOtherInfoDao;
import com.unicom.acting.fee.writeoff.service.DatumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 三户资料相关信息通过JDBC方式获取
 *
 * @author Administrators
 */
@Service
public class DatumServiceImpl implements DatumService {
    private static final Logger logger = LoggerFactory.getLogger(DatumServiceImpl.class);
    @Autowired
    private FeeAccountDao feeAccountDao;
    @Autowired
    private FeeUserOtherInfoDao feeUserOtherInfoDao;
    @Autowired
    private RestClient restClient;

    @Override
    public UserDatumInfo getUserDatumByMS(TradeCommInfoIn tradeCommInfoIn) {
        RequestEntity requestEntity = new RequestEntity();
        String[] param = new String[]{tradeCommInfoIn.getProvinceCode(), tradeCommInfoIn.getEparchyCode()};
        requestEntity.setUriPaths(param);
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

        //校验大合帐用户使用
        reqParam.put("QUERY_MODE", "1");

        requestEntity.setUriParams(reqParam);

        //用户资料查询公共微服务
        Rsp rsp = this.restClient.callSkyArkMicroService("accounting",
                ActingFeePubDef.QRY_USER_DATUM, HttpMethod.GET, requestEntity,
                tradeCommInfoIn.getHeaderGray());
        if (!SysTypes.SYS_SUCCESS_CODE.equals(rsp.getRspCode())) {
            throw new SkyArkException(rsp.getRspCode(), rsp.getRspDesc());
        } else if (CollectionUtils.isEmpty(rsp.getData())) {
            throw new SkyArkException("没有查询到用户资料！");
        } else {
            List<DefaultUsersInfoRsp> userDatumList = JsonUtil.transMapsToObjs(rsp.getData(), DefaultUsersInfoRsp.class);
            if (CollectionUtils.isEmpty(userDatumList)) {
                throw new SkyArkException("没有查询到用户资料！");
            }
            logger.debug("DefaultUsersInfoRsp :" + userDatumList.get(0).getMainUser().toString());
            return formateUserDatum(userDatumList.get(0));
        }
    }

    @Override
    public FeeAcctPaymentCycle getAcctPaymentCycle(String acctId) {
        return feeAccountDao.getAcctPaymentCycle(acctId);
    }

    @Override
    public boolean isBadBillUser(String acctId) {
        return feeUserOtherInfoDao.isBadBillUser(acctId);
    }

    @Override
    public boolean isNoCalcLateFeeUser(String userId, String acctId) {
        return feeUserOtherInfoDao.isNoCalcLateFeeUser(userId, acctId);
    }

    private UserDatumInfo formateUserDatum(DefaultUsersInfoRsp userDatumRsp) {
//        MainUser datumRecvInfo = userDatumRsp.getMainUser();
//        if (datumRecvInfo == null) {
//            throw new SkyArkException("调用用户资料查询微服务异常，没有返回三户信息");
//        }
//        UserDatumInfo userDatumInfo = new UserDatumInfo();
//        //设置账户信息
//        FeeAccount feeAccount = new FeeAccount();
//        feeAccount.setAcctId(datumRecvInfo.getAcctId());
//        feeAccount.setCustId(datumRecvInfo.getCustId());
//        feeAccount.setProvinceCode(datumRecvInfo.getProvinceCode());
//        feeAccount.setEparchyCode(datumRecvInfo.getEparchyCode());
//        feeAccount.setCityCode(datumRecvInfo.getCityCode());
//        feeAccount.setNetTypeCode(datumRecvInfo.getaNetTypeCode());
//        feeAccount.setPayModeCode(datumRecvInfo.getPayModeCode());
//        feeAccount.setPayName(datumRecvInfo.getPayName());
//        userDatumInfo.setFeeAccount(feeAccount);
//
//        //设置用户信息
//        User mainUser = new User();
//        mainUser.setUserId(datumRecvInfo.getUserId());
//        mainUser.setSerialNumber(datumRecvInfo.getSerialNumber());
//        mainUser.setNetTypeCode(datumRecvInfo.getuNetTypeCode());
//        mainUser.setProvinceCode(datumRecvInfo.getuProvinceCode());
//        mainUser.setEparchyCode(datumRecvInfo.getuEparchyCode());
//        mainUser.setOpenMode(datumRecvInfo.getOpenMode());
//
//        if (!StringUtil.isEmptyCheckNullStr(datumRecvInfo.getCreditValue())) {
//            mainUser.setCreditValue(Long.parseLong(datumRecvInfo.getCreditValue()));
//        }
//        mainUser.setRemoveTag(datumRecvInfo.getRemoveTag());
//        mainUser.setServiceStateCode(datumRecvInfo.getServiceStateCode());
//        mainUser.setPrepayTag(datumRecvInfo.getPrepayTag());
//        mainUser.setBrandCode(datumRecvInfo.getBrandCode());
//        if (!StringUtil.isEmptyCheckNullStr(datumRecvInfo.getDestroyDate())) {
//            mainUser.setDestroyDate(datumRecvInfo.getDestroyDate());
//        } else {
//            mainUser.setDestroyDate("");
//        }
//        userDatumInfo.setMainUser(mainUser);
//
//        if ("1".equals(datumRecvInfo.getDhzFlag())) {
//            //大合账用户走异步缴费
//            userDatumInfo.setBigAcct(true);
//            return userDatumInfo;
//        } else {
//            //非大合帐用户缴费
//            userDatumInfo.setBigAcct(false);
//        }
//
//        if (StringUtil.isEmptyCheckNullStr(feeAccount.getAcctId())
//                || StringUtil.isEmptyCheckNullStr(mainUser.getUserId())
//                || StringUtil.isEmptyCheckNullStr(mainUser.getSerialNumber())) {
//            throw new SkyArkException("没有查询到账户资料！");
//        }
//
//        if (CollectionUtils.isEmpty(userDatumRsp.getDefaultUsers())) {
//            throw new SkyArkException("该帐户下的用户付费关系都已失效，请按照服务号码或者用户标识进行缴费");
//        }
//        //默认付费用户信息
//        List<User> defaultPayUserList = new ArrayList(userDatumRsp.getDefaultUsers().size());
//        for (UserInfoRsp userInfoRsp : userDatumRsp.getDefaultUsers()) {
//            User user = new User();
//            user.setUserId(userInfoRsp.getUserId());
//            user.setDestroyDate(userInfoRsp.getDestroyDate());
//            user.setEparchyCode(userInfoRsp.getEparchyCode());
//            user.setSerialNumber(userInfoRsp.getSerialNumber());
//            user.setNetTypeCode(userInfoRsp.getNetTypeCode());
//            user.setRemoveTag(userInfoRsp.getRemoveTag());
//            defaultPayUserList.add(user);
//        }
//        userDatumInfo.setDefaultPayUsers(defaultPayUserList);
        return null;
    }


}

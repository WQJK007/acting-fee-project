package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import com.unicom.acting.fee.dao.UserOtherInfoFeeDao;
import com.unicom.acting.fee.domain.ActPayPubDef;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.domain.User;
import com.unicom.acting.fee.writeoff.domain.DefaultUsersInfoRsp;
import com.unicom.acting.fee.writeoff.domain.MainUser;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acting.fee.writeoff.domain.UserInfoRsp;
import com.unicom.acting.fee.writeoff.service.CycleFeeService;
import com.unicom.acting.fee.writeoff.service.DatumFeeService;
import com.unicom.acts.pay.dao.AccountDao;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AcctPaymentCycle;
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
public class DatumFeeServiceImpl implements DatumFeeService {
    private static final Logger logger = LoggerFactory.getLogger(DatumFeeServiceImpl.class);
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserOtherInfoFeeDao userOtherInfoFeeDao;
    @Autowired
    private CycleFeeService cycleFeeService;
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
                ActPayPubDef.QRY_USER_DATUM, HttpMethod.GET, requestEntity,
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
    public AcctPaymentCycle getAcctPaymentCycle(String acctId, String provinceCode) {
        return accountDao.getAcctPaymentCycle(acctId, provinceCode);
    }

    @Override
    public boolean isSpecialRecvState(Cycle cycle) {
        if (cycleFeeService.isDrecvPeriod(cycle) || cycleFeeService.isPatchDrecvPeriod(cycle)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBadBillUser(String acctId, String provinceCode) {
        return userOtherInfoFeeDao.isBadBillUser(acctId, provinceCode);
    }

    @Override
    public boolean isNoCalcLateFeeUser(String userId, String acctId, String provinceCode) {
        return userOtherInfoFeeDao.isNoCalcLateFeeUser(userId, acctId, provinceCode);
    }

    private UserDatumInfo formateUserDatum(DefaultUsersInfoRsp userDatumRsp) {
        MainUser datumRecvInfo = userDatumRsp.getMainUser();
        if (datumRecvInfo == null) {
            throw new SkyArkException("调用用户资料查询微服务异常，没有返回三户信息");
        }
        UserDatumInfo userDatumInfo = new UserDatumInfo();
        //设置账户信息
        Account account = new Account();
        account.setAcctId(datumRecvInfo.getAcctId());
        account.setCustId(datumRecvInfo.getCustId());
        account.setProvinceCode(datumRecvInfo.getProvinceCode());
        account.setEparchyCode(datumRecvInfo.getEparchyCode());
        account.setCityCode(datumRecvInfo.getCityCode());
        account.setNetTypeCode(datumRecvInfo.getaNetTypeCode());
        account.setPayModeCode(datumRecvInfo.getPayModeCode());
        account.setPayName(datumRecvInfo.getPayName());
        userDatumInfo.setAccount(account);

        //设置用户信息
        User mainUser = new User();
        mainUser.setUserId(datumRecvInfo.getUserId());
        mainUser.setSerialNumber(datumRecvInfo.getSerialNumber());
        mainUser.setNetTypeCode(datumRecvInfo.getuNetTypeCode());
        mainUser.setProvinceCode(datumRecvInfo.getuProvinceCode());
        mainUser.setEparchyCode(datumRecvInfo.getuEparchyCode());
        mainUser.setOpenMode(datumRecvInfo.getOpenMode());

        if (!StringUtil.isEmptyCheckNullStr(datumRecvInfo.getCreditValue())) {
            mainUser.setCreditValue(Long.parseLong(datumRecvInfo.getCreditValue()));
        }
        mainUser.setRemoveTag(datumRecvInfo.getRemoveTag());
        mainUser.setServiceStateCode(datumRecvInfo.getServiceStateCode());
        mainUser.setPrepayTag(datumRecvInfo.getPrepayTag());
        mainUser.setBrandCode(datumRecvInfo.getBrandCode());
        if (!StringUtil.isEmptyCheckNullStr(datumRecvInfo.getDestroyDate())) {
            mainUser.setDestroyDate(datumRecvInfo.getDestroyDate());
        } else {
            mainUser.setDestroyDate("");
        }
        userDatumInfo.setMainUser(mainUser);

        if ("1".equals(datumRecvInfo.getDhzFlag())) {
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

        if (CollectionUtils.isEmpty(userDatumRsp.getDefaultUsers())) {
            throw new SkyArkException("该帐户下的用户付费关系都已失效，请按照服务号码或者用户标识进行缴费");
        }
        //默认付费用户信息
        List<User> defaultPayUserList = new ArrayList(userDatumRsp.getDefaultUsers().size());
        for (UserInfoRsp userInfoRsp : userDatumRsp.getDefaultUsers()) {
            User user = new User();
            user.setUserId(userInfoRsp.getUserId());
            user.setDestroyDate(userInfoRsp.getDestroyDate());
            user.setEparchyCode(userInfoRsp.getEparchyCode());
            user.setSerialNumber(userInfoRsp.getSerialNumber());
            user.setNetTypeCode(userInfoRsp.getNetTypeCode());
            user.setRemoveTag(userInfoRsp.getRemoveTag());
            defaultPayUserList.add(user);
        }
        userDatumInfo.setDefaultPayUsers(defaultPayUserList);
        return userDatumInfo;
    }


}

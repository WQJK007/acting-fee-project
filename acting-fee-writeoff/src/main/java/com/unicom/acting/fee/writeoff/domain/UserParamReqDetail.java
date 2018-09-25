package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 用户资料参数查询
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserParamReqDetail {
    private List<UserParamReqDetailInfo> userInfo;

    public List<UserParamReqDetailInfo> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserParamReqDetailInfo> userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserParamReqDetail{" +
                "userInfo=" + userInfo +
                '}';
    }
}

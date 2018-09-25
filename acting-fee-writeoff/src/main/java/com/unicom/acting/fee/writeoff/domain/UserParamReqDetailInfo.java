package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 用户资料参数请求类
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserParamReqDetailInfo {
    private String userId;
    private String paramId;
    private String userTypeCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    @Override
    public String toString() {
        return "UserParamReqDetail{" +
                "userId='" + userId + '\'' +
                ", paramId='" + paramId + '\'' +
                ", userTypeCode='" + userTypeCode + '\'' +
                '}';
    }
}

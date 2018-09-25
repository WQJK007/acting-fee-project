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
public class UserParamReq {
    private UserParamReqDetail req;

    public UserParamReqDetail getReq() {
        return req;
    }

    public void setReq(UserParamReqDetail req) {
        this.req = req;
    }

    @Override
    public String toString() {
        return "UserParamReq{" +
                "req=" + req +
                '}';
    }
}

package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 群组关系查询
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRelationReq {
    private UserRelationReqDetail req;

    public UserRelationReqDetail getReq() {
        return req;
    }

    public void setReq(UserRelationReqDetail req) {
        this.req = req;
    }

    @Override
    public String toString() {
        return "UserRelationReq{" +
                "req=" + req +
                '}';
    }
}

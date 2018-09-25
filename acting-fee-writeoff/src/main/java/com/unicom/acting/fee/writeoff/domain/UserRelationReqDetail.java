package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 群组关系查询明细参数
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRelationReqDetail {
    private String userId;
    private String memberRoleId;
    private String cycleId;
    private List<UserRelationReqDetailInfo> relationTypeInfo;
    private String islike;
    private String isunion;
    private String userTypeCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(String memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public List<UserRelationReqDetailInfo> getRelationTypeInfo() {
        return relationTypeInfo;
    }

    public void setRelationTypeInfo(List<UserRelationReqDetailInfo> relationTypeInfo) {
        this.relationTypeInfo = relationTypeInfo;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public String getIsunion() {
        return isunion;
    }

    public void setIsunion(String isunion) {
        this.isunion = isunion;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    @Override
    public String toString() {
        return "UserRelationReq{" +
                "userId='" + userId + '\'' +
                ", memberRoleId='" + memberRoleId + '\'' +
                ", cycleId='" + cycleId + '\'' +
                ", relationTypeInfo=" + relationTypeInfo +
                ", islike='" + islike + '\'' +
                ", isunion='" + isunion + '\'' +
                ", userTypeCode='" + userTypeCode + '\'' +
                '}';
    }
}



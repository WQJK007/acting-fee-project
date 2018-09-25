package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;


/**
 * 用户融合关系应答
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRelationRsp {
    @ApiModelProperty(name = "userId", value = "用户标识", required = true, example = "1115031633460322")
    private String userId;
    @ApiModelProperty(name = "netTypeCode", value = "用户成员网别", required = true, example = "50")
    private String netTypeCode;
    @ApiModelProperty(name = "memberRoleCode", value = "成员角色编码", required = true, example = "6009")
    private String memberRoleCode;
    @ApiModelProperty(name = "memberRoleType", value = "成员角色类型", required = true, example = "0")
    private String memberRoleType;
    @ApiModelProperty(name = "memberRoleId", value = "成员角色标识", required = true, example = "1115042034840490")
    private String memberRoleId;
    @ApiModelProperty(name = "memberRoleNumber", value = "成员角色", required = true, example = "18519096120")
    private String memberRoleNumber;
    @ApiModelProperty(name = "startDate", value = "开始时间", required = true, example = "2015-04-21 11:29:38")
    private String startDate;
    @ApiModelProperty(name = "endDate", value = "结束时间", required = true, example = "2050-12-31 23:59:59")
    private String endDate;
    @ApiModelProperty(name = "discntPriority", value = "资费优先级", required = true, example = "-1")
    private String discntPriority;
    @ApiModelProperty(name = "relationTypeCode", value = "关系类型编码", required = true, example = "ZF")
    private String relationTypeCode;
    @ApiModelProperty(name = "itemId", value = "账目标识", required = true, example = "11001")
    private String itemId;
    @ApiModelProperty(name = "provCode", value = "省份编码", required = true, example = "11")
    private String provCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getMemberRoleCode() {
        return memberRoleCode;
    }

    public void setMemberRoleCode(String memberRoleCode) {
        this.memberRoleCode = memberRoleCode;
    }

    public String getMemberRoleType() {
        return memberRoleType;
    }

    public void setMemberRoleType(String memberRoleType) {
        this.memberRoleType = memberRoleType;
    }

    public String getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(String memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public String getMemberRoleNumber() {
        return memberRoleNumber;
    }

    public void setMemberRoleNumber(String memberRoleNumber) {
        this.memberRoleNumber = memberRoleNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDiscntPriority() {
        return discntPriority;
    }

    public void setDiscntPriority(String discntPriority) {
        this.discntPriority = discntPriority;
    }

    public String getRelationTypeCode() {
        return relationTypeCode;
    }

    public void setRelationTypeCode(String relationTypeCode) {
        this.relationTypeCode = relationTypeCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    @Override
    public String toString() {
        return "UserRelationRsp{" +
                "userId='" + userId + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", memberRoleCode='" + memberRoleCode + '\'' +
                ", memberRoleType='" + memberRoleType + '\'' +
                ", memberRoleId='" + memberRoleId + '\'' +
                ", memberRoleNumber='" + memberRoleNumber + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", discntPriority='" + discntPriority + '\'' +
                ", relationTypeCode='" + relationTypeCode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", provCode='" + provCode + '\'' +
                '}';
    }
}

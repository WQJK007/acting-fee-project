package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户属性应答
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserParamRsp {
    @ApiModelProperty(name = "userId", value = "用户标识", required = true, example = "50002612")
    private String userId;
    @ApiModelProperty(name = "paramId", value = "资料参数编码", required = true, example = "50002612")
    private String paramId;
    @ApiModelProperty(name = "paramValue", value = "资料参数值", required = true, example = "111111")
    private String paramValue;
    @ApiModelProperty(name = "paramName", value = "资料参数名称", required = true, example = "初始密码")
    private String paramName;
    @ApiModelProperty(name = "startDate", value = "开始时间", required = true, example = "2015-04-21 11:29:38")
    private String startDate;
    @ApiModelProperty(name = "endDate", value = "结束时间", required = true, example = "2050-12-31 23:59:59")
    private String endDate;
    @ApiModelProperty(name = "provCode", value = "省份编码", required = true, example = "11")
    private String provCode;

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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
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

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    @Override
    public String toString() {
        return "UserParamRsp{" +
                "userId='" + userId + '\'' +
                ", paramId='" + paramId + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", provCode='" + provCode + '\'' +
                '}';
    }
}

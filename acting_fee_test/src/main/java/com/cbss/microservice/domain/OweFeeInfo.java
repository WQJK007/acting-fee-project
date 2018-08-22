package com.cbss.microservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.act.framework.common.CbssPropertyNamingStrategy;
import com.unicom.act.framework.web.data.Para;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 欠费信息
 *
 * @author shaob
 */
@JsonNaming(CbssPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OweFeeInfo {
    @ApiModelProperty(name = "PAY_NAME", value = "发票客户名称", required = true,example = "张三")
    private String payName;
    @ApiModelProperty(name = "SERVICE_CLASS_CODE", value = "网别" ,required = true,example = "1000")
    private String serviceClassCode;
    @ApiModelProperty(name = "AREA_CODE", value = "长途区号", example = "0010" )
    private String areaCode;
    @ApiModelProperty(name = "SERIAL_NUMBER", value = "服务号码",required = true,example = "18513583782")
    private String serialNumber;
    @ApiModelProperty(name = "USER_ID", value = "用户标识", required = true,example = "1114033124170501")
    private String userId;
    @ApiModelProperty(name = "ACCT_ID", value = "账户标识", required = true,example = "1116032119640474")
    private String acctId;
    @ApiModelProperty(name = "PROVINCE_CODE", value = "归属省份", required = true,example = "11")
    private String provinceCode;
    @ApiModelProperty(name = "EPARCHY_CODE", value = "归属地市", required = true,example = "0010")
    private String eparchyCode;
    @ApiModelProperty(name = "BALANCE_FEE", value = "当前余额", required = true,example = "200")
    private String balanceFee;
    @ApiModelProperty(name = "MIN_PAY_FEE", value = "最小应交", required = true,example = "200")
    private String minPayFee;
    @ApiModelProperty(name = "PAY_FEE", value = "应交金额", required = true,example = "200")
    private String payFee;
    @ApiModelProperty(name = "TOTAL_FEE", value = "往月欠费合计", required = true,example = "200")
    private String totalFee;
    @ApiModelProperty(name = "REAL_FEE", value = "实时话费",example = "1111")
    private String realFee;
    @ApiModelProperty(name = "MUST_PAY", value = "是否可部分缴费", required = true,example = "0")
    private String mustPay;
    @ApiModelProperty(name = "CYCLE_OWE_FEE_INFO", value = "分账期欠费信息")
    private List<CycleOweFeeInfo> cycleOweFeeInfo;
    @ApiModelProperty(name = "PARA", value = "保留字段")
    private List<Para> para;

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getBalanceFee() {
        return balanceFee;
    }

    public void setBalanceFee(String balanceFee) {
        this.balanceFee = balanceFee;
    }

    public String getMinPayFee() {
        return minPayFee;
    }

    public void setMinPayFee(String minPayFee) {
        this.minPayFee = minPayFee;
    }

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getRealFee() {
        return realFee;
    }

    public void setRealFee(String realFee) {
        this.realFee = realFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getMustPay() {
        return mustPay;
    }

    public void setMustPay(String mustPay) {
        this.mustPay = mustPay;
    }

    public List<CycleOweFeeInfo> getCycleOweFeeInfo() {
        return cycleOweFeeInfo;
    }

    public void setCycleOweFeeInfo(List<CycleOweFeeInfo> cycleOweFeeInfo) {
        this.cycleOweFeeInfo = cycleOweFeeInfo;
    }

    public List<Para> getPara() {
        return para;
    }

    public void setPara(List<Para> para) {
        this.para = para;
    }

    @Override
    public String toString() {
        return "OweFeeInfo{" +
                "payName='" + payName + '\'' +
                ", serviceClassCode='" + serviceClassCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", balanceFee='" + balanceFee + '\'' +
                ", minPayFee='" + minPayFee + '\'' +
                ", payFee='" + payFee + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", realFee='" + realFee + '\'' +
                ", mustPay='" + mustPay + '\'' +
                ", cycleOweFeeInfo=" + cycleOweFeeInfo +
                ", para=" + para +
                '}';
    }
}

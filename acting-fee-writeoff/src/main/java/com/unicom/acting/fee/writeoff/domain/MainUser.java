package com.unicom.acting.fee.writeoff.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 三户资料信息
 *
 * @author Wangkh
 */

@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainUser {
    private String provinceCode;
    private String eparchyCode;
    private String cityCode;
    private String custId;
    private String aNetTypeCode;
    private String payModeCode;
    private String acctId="";
    private String userId="";
    private String creditValue="";
    private String serialNumber="";
    private String uNetTypeCode="";
    private String brandCode="";
    private String dhzFlag = "";
    private String payName;
    private String serviceStateCode;
    private String removeTag;
    private String prepayTag;
    private String uProvinceCode;
    private String uEparchyCode;
    private String openMode;
    private String usecustId;
    private String destroyDate;
    private String openDate;
    private String userStateCodeset;
    private String scoreValue;
    private String userPasswd;
    private String productId;
    private String changeuserDate;

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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getaNetTypeCode() {
        return aNetTypeCode;
    }

    public void setaNetTypeCode(String aNetTypeCode) {
        this.aNetTypeCode = aNetTypeCode;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getuNetTypeCode() {
        return uNetTypeCode;
    }

    public void setuNetTypeCode(String uNetTypeCode) {
        this.uNetTypeCode = uNetTypeCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getDhzFlag() {
        return dhzFlag;
    }

    public void setDhzFlag(String dhzFlag) {
        this.dhzFlag = dhzFlag;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getServiceStateCode() {
        return serviceStateCode;
    }

    public void setServiceStateCode(String serviceStateCode) {
        this.serviceStateCode = serviceStateCode;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }

    public String getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(String prepayTag) {
        this.prepayTag = prepayTag;
    }

    public String getuProvinceCode() {
        return uProvinceCode;
    }

    public void setuProvinceCode(String uProvinceCode) {
        this.uProvinceCode = uProvinceCode;
    }

    public String getuEparchyCode() {
        return uEparchyCode;
    }

    public void setuEparchyCode(String uEparchyCode) {
        this.uEparchyCode = uEparchyCode;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getUsecustId() {
        return usecustId;
    }

    public void setUsecustId(String usecustId) {
        this.usecustId = usecustId;
    }

    public String getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(String destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getUserStateCodeset() {
        return userStateCodeset;
    }

    public void setUserStateCodeset(String userStateCodeset) {
        this.userStateCodeset = userStateCodeset;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getChangeuserDate() {
        return changeuserDate;
    }

    public void setChangeuserDate(String changeuserDate) {
        this.changeuserDate = changeuserDate;
    }

    @Override
    public String toString() {
        return "MainUser{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", custId='" + custId + '\'' +
                ", aNetTypeCode='" + aNetTypeCode + '\'' +
                ", payModeCode='" + payModeCode + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", creditValue='" + creditValue + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", uNetTypeCode='" + uNetTypeCode + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", dhzFlag='" + dhzFlag + '\'' +
                ", payName='" + payName + '\'' +
                ", serviceStateCode='" + serviceStateCode + '\'' +
                ", removeTag='" + removeTag + '\'' +
                ", prepayTag='" + prepayTag + '\'' +
                ", uProvinceCode='" + uProvinceCode + '\'' +
                ", uEparchyCode='" + uEparchyCode + '\'' +
                ", openMode='" + openMode + '\'' +
                ", usecustId='" + usecustId + '\'' +
                ", destroyDate='" + destroyDate + '\'' +
                ", openDate='" + openDate + '\'' +
                ", userStateCodeset='" + userStateCodeset + '\'' +
                ", scoreValue='" + scoreValue + '\'' +
                ", userPasswd='" + userPasswd + '\'' +
                ", productId='" + productId + '\'' +
                ", changeuserDate='" + changeuserDate + '\'' +
                '}';
    }
}

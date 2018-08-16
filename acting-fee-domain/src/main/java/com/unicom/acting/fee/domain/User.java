package com.unicom.acting.fee.domain;

/**
 * 用户资料表，映射TF_F_USER表部分字段
 *
 * @author Wangkh
 */
public class User {
    private String userId;
    private String provinceCode;
    private String removeTag;
    private String netTypeCode;
    private String prepayTag;
    private String useCustId;
    private String serialNumber;
    private String destroyDate;
    private String eparchyCode;
    private String brandCode;
    private String openDate;
    private String openMode;
    private String userStateCode;
    private String scoreValue;
    private String userPassWd;
    private String serviceStateCode;
    private String custId;
    private String productId;
    private String changeUserDate;
    private long creditValue;

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(String prepayTag) {
        this.prepayTag = prepayTag;
    }

    public String getUseCustId() {
        return useCustId;
    }

    public void setUseCustId(String useCustId) {
        this.useCustId = useCustId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(String destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getUserStateCode() {
        return userStateCode;
    }

    public void setUserStateCode(String userStateCode) {
        this.userStateCode = userStateCode;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getUserPassWd() {
        return userPassWd;
    }

    public void setUserPassWd(String userPassWd) {
        this.userPassWd = userPassWd;
    }

    public String getServiceStateCode() {
        return serviceStateCode;
    }

    public void setServiceStateCode(String serviceStateCode) {
        this.serviceStateCode = serviceStateCode;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getChangeUserDate() {
        return changeUserDate;
    }

    public void setChangeUserDate(String changeUserDate) {
        this.changeUserDate = changeUserDate;
    }

    public long getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(long creditValue) {
        this.creditValue = creditValue;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", removeTag='" + removeTag + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", prepayTag='" + prepayTag + '\'' +
                ", useCustId='" + useCustId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", destroyDate='" + destroyDate + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", openDate='" + openDate + '\'' +
                ", openMode='" + openMode + '\'' +
                ", userStateCode='" + userStateCode + '\'' +
                ", scoreValue='" + scoreValue + '\'' +
                ", userPassWd='" + userPassWd + '\'' +
                ", serviceStateCode='" + serviceStateCode + '\'' +
                ", custId='" + custId + '\'' +
                ", productId='" + productId + '\'' +
                ", changeUserDate='" + changeUserDate + '\'' +
                ", creditValue=" + creditValue +
                '}';
    }
}

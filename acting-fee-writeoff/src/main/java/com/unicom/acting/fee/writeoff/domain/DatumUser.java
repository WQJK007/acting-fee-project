package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

/**
 * 本次交易用户应答信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumUser {
    @ApiModelProperty(name = "provinceCode", value = "省份编码", required = true, example = "11")
    private String provinceCode;
    @ApiModelProperty(name = "eparchyCode", value = "地市编码", required = true, example = "0010")
    private String eparchyCode;
    @ApiModelProperty(name = "serialNumber", value = "服务号码", required = true, example = "18601275997")
    private String serialNumber;
    @ApiModelProperty(name = "netTypeCode", value = "网别", required = true, example = "50")
    private String netTypeCode;
    @ApiModelProperty(name = "parentTypeCode", value = "父级网别编码(0 移网 1固网)", required = true, example = "0")
    private String parentTypeCode;
    @ApiModelProperty(name = "removeTag", value = "销号标识", required = true, example = "0")
    private String removeTag;
    @ApiModelProperty(name = "userId", value = "用户标识", required = true, example = "1114081924600967")
    private String userId;
    @ApiModelProperty(name = "custId", value = "客户标识", required = true, example = "1114081922730226")
    private String custId;
    @ApiModelProperty(name = "custName", value = "客户名称", required = true, example = "中国人民财产保险股份有限公司")
    private String custName;
    @ApiModelProperty(name = "creditValue", value = "用户信用度", required = true, example = "100099999")
    private String creditValue;
    @ApiModelProperty(name = "scoreValue", value = "用户积分", required = true, example = "0")
    private String scoreValue;
    @ApiModelProperty(name = "brandCode", value = "品牌编码", required = true, example = "4G00")
    private String brandCode;
    @ApiModelProperty(name = "serviceStateCode", value = "服务状态标识", required = true, example = "0")
    private String serviceStateCode;
    @ApiModelProperty(name = "prepayTag", value = "预付费标识", required = true, example = "0")
    private String prepayTag;
    @ApiModelProperty(name = "openMode", value = "开户方式", required = true, example = "0")
    private String openMode;
    @ApiModelProperty(name = "openDate", value = "开户时间", required = true, example = "2010-02-10 14:27:03")
    private String openDate;
    @ApiModelProperty(name = "destroyDate", value = "销户时间", required = true, example = "2017-10-03 14:25:40")
    private String destroyDate;
    @ApiModelProperty(name = "productId", value = "产品标识", required = true, example = "89002922")
    private String productId;
    @ApiModelProperty(name = "changeuserDate", value = "过户时间", required = true, example = "2017-10-03 18:37:35")
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getParentTypeCode() {
        return parentTypeCode;
    }

    public void setParentTypeCode(String parentTypeCode) {
        this.parentTypeCode = parentTypeCode;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getServiceStateCode() {
        return serviceStateCode;
    }

    public void setServiceStateCode(String serviceStateCode) {
        this.serviceStateCode = serviceStateCode;
    }

    public String getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(String prepayTag) {
        this.prepayTag = prepayTag;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(String destroyDate) {
        this.destroyDate = destroyDate;
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
        return "User{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", parentTypeCode='" + parentTypeCode + '\'' +
                ", removeTag='" + removeTag + '\'' +
                ", userId='" + userId + '\'' +
                ", custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", creditValue='" + creditValue + '\'' +
                ", scoreValue='" + scoreValue + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", serviceStateCode='" + serviceStateCode + '\'' +
                ", prepayTag='" + prepayTag + '\'' +
                ", openMode='" + openMode + '\'' +
                ", openDate='" + openDate + '\'' +
                ", destroyDate='" + destroyDate + '\'' +
                ", productId='" + productId + '\'' +
                ", changeuserDate='" + changeuserDate + '\'' +
                '}';
    }
}

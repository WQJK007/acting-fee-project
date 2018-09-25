package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

/**
 * 默认付费账户应答信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumAccount {
    @ApiModelProperty(name = "provinceCode", value = "省份编码", required = true, example = "11")
    private String provinceCode;
    @ApiModelProperty(name = "eparchyCode", value = "地市编码", required = true, example = "0010")
    private String eparchyCode;
    @ApiModelProperty(name = "cityCode", value = "区县编码", required = true, example = "0010")
    private String cityCode;
    @ApiModelProperty(name = "NetTypeCode", value = "网别编码", required = true, example = "10")
    private String netTypeCode;
    @ApiModelProperty(name = "custId", value = "客户标识", required = true, example = "9930132348")
    private String custId;
    @ApiModelProperty(name = "payModeCode", value = "付费方式", required = true, example = "0")
    private String payModeCode;
    @ApiModelProperty(name = "acctId", value = "账户标识", required = true, example = "1117052708620522")
    private String acctId;
    @ApiModelProperty(name = "payName", value = "付费名称", required = true, example = "中国人民财产保险股份有限公司")
    private String payName;
    @ApiModelProperty(name = "dhzFlag", value = "大合帐标识 0不是大合帐1是大合帐", required = true, example = "0")
    private String dhzFlag;

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

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
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

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getDhzFlag() {
        return dhzFlag;
    }

    public void setDhzFlag(String dhzFlag) {
        this.dhzFlag = dhzFlag;
    }

    @Override
    public String toString() {
        return "DatumAccount{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", custId='" + custId + '\'' +
                ", payModeCode='" + payModeCode + '\'' +
                ", acctId='" + acctId + '\'' +
                ", payName='" + payName + '\'' +
                ", dhzFlag='" + dhzFlag + '\'' +
                '}';
    }
}

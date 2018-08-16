package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 三户资料查询微服务应答对象
 *
 * @author Wangkh
 */

@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDatumInfoRsp {
    private String provinceCode;
    private String eparchyCode;
    private String xPayAcctId;
    private String xPaySerialNumber;
    private String xPayUserId;
    private String serviceClassCode;
    private String xRecordnum;

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

    public String getxPayAcctId() {
        return xPayAcctId;
    }

    public void setxPayAcctId(String xPayAcctId) {
        this.xPayAcctId = xPayAcctId;
    }

    public String getxPaySerialNumber() {
        return xPaySerialNumber;
    }

    public void setxPaySerialNumber(String xPaySerialNumber) {
        this.xPaySerialNumber = xPaySerialNumber;
    }

    public String getxPayUserId() {
        return xPayUserId;
    }

    public void setxPayUserId(String xPayUserId) {
        this.xPayUserId = xPayUserId;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public String getxRecordnum() {
        return xRecordnum;
    }

    public void setxRecordnum(String xRecordnum) {
        this.xRecordnum = xRecordnum;
    }

    @Override
    public String toString() {
        return "UserDatumInfoRsp{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", xPayAcctId='" + xPayAcctId + '\'' +
                ", xPaySerialNumber='" + xPaySerialNumber + '\'' +
                ", xPayUserId='" + xPayUserId + '\'' +
                ", serviceClassCode='" + serviceClassCode + '\'' +
                ", xRecordnum='" + xRecordnum + '\'' +
                '}';
    }
}

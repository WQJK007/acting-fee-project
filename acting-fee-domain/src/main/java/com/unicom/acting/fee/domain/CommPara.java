package com.unicom.acting.fee.domain;


/**
 * 账务公共参数表，映射TD_B_COMMPARA
 *
 * @author Wangkh
 */
public class CommPara {
    private String provinceCode;
    private String eparchyCode;
    private long paraAttr;
    private String paraCode;
    private String paraName;
    private String paraCode1;
    private String paraCode2;
    private String paraCode3;
    private String paraCode4;
    private String paraCode5;
    private String paraCode6;
    private String paraDate7;
    private String paraDate8;
    private String paraDate9;
    private String paraDate10;
    private String startDate;
    private String endDate;
    private char useTag;
    private String remark;
    private String updateTime;
    private String updateEparchyCode;
    private String updateCityCode;
    private String updateDepartId;
    private String updateStaffId;

    public CommPara() {
        paraAttr = -1;
        useTag = '\0';
    }

    public void init() {
        provinceCode = "";
        eparchyCode = "";
        paraAttr = -1;
        paraCode = "";
        paraName = "";
        paraCode1 = "";
        paraCode2 = "";
        paraCode3 = "";
        paraCode4 = "";
        paraCode5 = "";
        paraCode6 = "";
        paraDate7 = "";
        paraDate8 = "";
        paraDate9 = "";
        paraDate10 = "";
        startDate = "";
        endDate = "";
        useTag = '\0';
        remark = "";
        updateTime = "";
        updateEparchyCode = "";
        updateCityCode = "";
        updateDepartId = "";
        updateStaffId = "";
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

    public long getParaAttr() {
        return paraAttr;
    }

    public void setParaAttr(long paraAttr) {
        this.paraAttr = paraAttr;
    }

    public String getParaCode() {
        return paraCode;
    }

    public void setParaCode(String paraCode) {
        this.paraCode = paraCode;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getParaCode1() {
        return paraCode1;
    }

    public void setParaCode1(String paraCode1) {
        this.paraCode1 = paraCode1;
    }

    public String getParaCode2() {
        return paraCode2;
    }

    public void setParaCode2(String paraCode2) {
        this.paraCode2 = paraCode2;
    }

    public String getParaCode3() {
        return paraCode3;
    }

    public void setParaCode3(String paraCode3) {
        this.paraCode3 = paraCode3;
    }

    public String getParaCode4() {
        return paraCode4;
    }

    public void setParaCode4(String paraCode4) {
        this.paraCode4 = paraCode4;
    }

    public String getParaCode5() {
        return paraCode5;
    }

    public void setParaCode5(String paraCode5) {
        this.paraCode5 = paraCode5;
    }

    public String getParaCode6() {
        return paraCode6;
    }

    public void setParaCode6(String paraCode6) {
        this.paraCode6 = paraCode6;
    }

    public String getParaDate7() {
        return paraDate7;
    }

    public void setParaDate7(String paraDate7) {
        this.paraDate7 = paraDate7;
    }

    public String getParaDate8() {
        return paraDate8;
    }

    public void setParaDate8(String paraDate8) {
        this.paraDate8 = paraDate8;
    }

    public String getParaDate9() {
        return paraDate9;
    }

    public void setParaDate9(String paraDate9) {
        this.paraDate9 = paraDate9;
    }

    public String getParaDate10() {
        return paraDate10;
    }

    public void setParaDate10(String paraDate10) {
        this.paraDate10 = paraDate10;
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

    public char getUseTag() {
        return useTag;
    }

    public void setUseTag(char useTag) {
        this.useTag = useTag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateEparchyCode() {
        return updateEparchyCode;
    }

    public void setUpdateEparchyCode(String updateEparchyCode) {
        this.updateEparchyCode = updateEparchyCode;
    }

    public String getUpdateCityCode() {
        return updateCityCode;
    }

    public void setUpdateCityCode(String updateCityCode) {
        this.updateCityCode = updateCityCode;
    }

    public String getUpdateDepartId() {
        return updateDepartId;
    }

    public void setUpdateDepartId(String updateDepartId) {
        this.updateDepartId = updateDepartId;
    }

    public String getUpdateStaffId() {
        return updateStaffId;
    }

    public void setUpdateStaffId(String updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    @Override
    public String toString() {
        return "CommPara{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", paraAttr=" + paraAttr +
                ", paraCode='" + paraCode + '\'' +
                ", paraName='" + paraName + '\'' +
                ", paraCode1='" + paraCode1 + '\'' +
                ", paraCode2='" + paraCode2 + '\'' +
                ", paraCode3='" + paraCode3 + '\'' +
                ", paraCode4='" + paraCode4 + '\'' +
                ", paraCode5='" + paraCode5 + '\'' +
                ", paraCode6='" + paraCode6 + '\'' +
                ", paraDate7='" + paraDate7 + '\'' +
                ", paraDate8='" + paraDate8 + '\'' +
                ", paraDate9='" + paraDate9 + '\'' +
                ", paraDate10='" + paraDate10 + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", useTag=" + useTag +
                ", remark='" + remark + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateEparchyCode='" + updateEparchyCode + '\'' +
                ", updateCityCode='" + updateCityCode + '\'' +
                ", updateDepartId='" + updateDepartId + '\'' +
                ", updateStaffId='" + updateStaffId + '\'' +
                '}';
    }
}

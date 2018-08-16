package com.unicom.acting.fee.domain;

/**
 * 账期定义表，映射TD_B_CYCLE和TD_B_CYCLE_EPARCHY表
 *
 * @author Wangkh
 */
public class Cycle {
    private int cycleId;
    private String provinceCode;
    private String eparchyCode;
    private String cycStartTime;
    private String cycEndTime;
    private String monthAcctStatus;
    private String auxAcctStatus;
    private char useTag;
    private char addTag;
    private String openAcctDate;

    public Cycle() {
        cycleId = -1;
        useTag = '\0';
        addTag = '\0';
    }

    public void init() {
        cycleId = -1;
        useTag = '\0';
        addTag = '\0';
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
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

    public String getCycStartTime() {
        return cycStartTime;
    }

    public void setCycStartTime(String cycStartTime) {
        this.cycStartTime = cycStartTime;
    }

    public String getCycEndTime() {
        return cycEndTime;
    }

    public void setCycEndTime(String cycEndTime) {
        this.cycEndTime = cycEndTime;
    }

    public String getMonthAcctStatus() {
        return monthAcctStatus;
    }

    public void setMonthAcctStatus(String monthAcctStatus) {
        this.monthAcctStatus = monthAcctStatus;
    }

    public String getAuxAcctStatus() {
        return auxAcctStatus;
    }

    public void setAuxAcctStatus(String auxAcctStatus) {
        this.auxAcctStatus = auxAcctStatus;
    }

    public char getUseTag() {
        return useTag;
    }

    public void setUseTag(char useTag) {
        this.useTag = useTag;
    }

    public char getAddTag() {
        return addTag;
    }

    public void setAddTag(char addTag) {
        this.addTag = addTag;
    }

    public String getOpenAcctDate() {
        return openAcctDate;
    }

    public void setOpenAcctDate(String openAcctDate) {
        this.openAcctDate = openAcctDate;
    }

    public String toString() {
        return "cycleId = " + this.cycleId + ",provinceCode = "
                + this.provinceCode + ",eparchyCode = " + this.eparchyCode
                + ",cycStartTime = " + this.cycStartTime + ",cycEndTime = "
                + this.cycEndTime + ",monthAcctStatus = "
                + this.monthAcctStatus + ",auxAcctStatus = "
                + this.auxAcctStatus + ",useTag = " + this.useTag
                + ",addTag = " + this.addTag + ",openAcctDate = "
                + this.openAcctDate;
    }
}

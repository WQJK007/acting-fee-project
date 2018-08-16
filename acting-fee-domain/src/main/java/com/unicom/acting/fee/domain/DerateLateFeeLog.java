package com.unicom.acting.fee.domain;

/**
 * 滞纳金减免工单对象，主要映射TF_B_DERATELATEFEELOG表主要字段
 *
 * @author Wangkh
 */
public class DerateLateFeeLog {
    private String eparchyCode;
    private String derateId;
    private String acctId;
    private String userId;
    private int cycleId;
    private int derateRuleId;
    private long derateFee;
    private long usedDerateFee;
    private long oldUsedDerateFee;  //持续使用需要备份
    private String operateId;
    private String startDate;
    private String endDate;
    private String derateTime;
    private String derateEparchyCode;
    private String derateCityCode;
    private String derateDepartId;
    private String derateStaffId;
    private int derateReasonCode;
    private String remark;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private String rsrvInfo2;
    private long sumLateFee;        //零时使用滞纳金总额－－减免到情况下使用
    private char useTag;
    private char oldUseTag;         //老的使用标志

    public DerateLateFeeLog() {
        cycleId = -1;
        derateRuleId = -1;
        derateFee = 0;
        usedDerateFee = 0;
        oldUsedDerateFee = 0;
        useTag = '0';
        oldUseTag = '0';
        derateReasonCode = -1;
        rsrvFee1 = 0;
        rsrvFee2 = 0;
        sumLateFee = 0;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getDerateId() {
        return derateId;
    }

    public void setDerateId(String derateId) {
        this.derateId = derateId;
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

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public int getDerateRuleId() {
        return derateRuleId;
    }

    public void setDerateRuleId(int derateRuleId) {
        this.derateRuleId = derateRuleId;
    }

    public long getDerateFee() {
        return derateFee;
    }

    public void setDerateFee(long derateFee) {
        this.derateFee = derateFee;
    }

    public long getUsedDerateFee() {
        return usedDerateFee;
    }

    public void setUsedDerateFee(long usedDerateFee) {
        this.usedDerateFee = usedDerateFee;
    }

    public long getOldUsedDerateFee() {
        return oldUsedDerateFee;
    }

    public void setOldUsedDerateFee(long oldUsedDerateFee) {
        this.oldUsedDerateFee = oldUsedDerateFee;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
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

    public String getDerateTime() {
        return derateTime;
    }

    public void setDerateTime(String derateTime) {
        this.derateTime = derateTime;
    }

    public String getDerateEparchyCode() {
        return derateEparchyCode;
    }

    public void setDerateEparchyCode(String derateEparchyCode) {
        this.derateEparchyCode = derateEparchyCode;
    }

    public String getDerateCityCode() {
        return derateCityCode;
    }

    public void setDerateCityCode(String derateCityCode) {
        this.derateCityCode = derateCityCode;
    }

    public String getDerateDepartId() {
        return derateDepartId;
    }

    public void setDerateDepartId(String derateDepartId) {
        this.derateDepartId = derateDepartId;
    }

    public String getDerateStaffId() {
        return derateStaffId;
    }

    public void setDerateStaffId(String derateStaffId) {
        this.derateStaffId = derateStaffId;
    }

    public int getDerateReasonCode() {
        return derateReasonCode;
    }

    public void setDerateReasonCode(int derateReasonCode) {
        this.derateReasonCode = derateReasonCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRsrvFee1() {
        return rsrvFee1;
    }

    public void setRsrvFee1(long rsrvFee1) {
        this.rsrvFee1 = rsrvFee1;
    }

    public long getRsrvFee2() {
        return rsrvFee2;
    }

    public void setRsrvFee2(long rsrvFee2) {
        this.rsrvFee2 = rsrvFee2;
    }

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
    }

    public String getRsrvInfo2() {
        return rsrvInfo2;
    }

    public void setRsrvInfo2(String rsrvInfo2) {
        this.rsrvInfo2 = rsrvInfo2;
    }

    public long getSumLateFee() {
        return sumLateFee;
    }

    public void setSumLateFee(long sumLateFee) {
        this.sumLateFee = sumLateFee;
    }

    public char getUseTag() {
        return useTag;
    }

    public void setUseTag(char useTag) {
        this.useTag = useTag;
    }

    public char getOldUseTag() {
        return oldUseTag;
    }

    public void setOldUseTag(char oldUseTag) {
        this.oldUseTag = oldUseTag;
    }

    @Override
    public String toString() {
        return "DerateLateFeeLog{" +
                "eparchyCode='" + eparchyCode + '\'' +
                ", derateId='" + derateId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", cycleId=" + cycleId +
                ", derateRuleId=" + derateRuleId +
                ", derateFee=" + derateFee +
                ", usedDerateFee=" + usedDerateFee +
                ", oldUsedDerateFee=" + oldUsedDerateFee +
                ", operateId='" + operateId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", derateTime='" + derateTime + '\'' +
                ", derateEparchyCode='" + derateEparchyCode + '\'' +
                ", derateCityCode='" + derateCityCode + '\'' +
                ", derateDepartId='" + derateDepartId + '\'' +
                ", derateStaffId='" + derateStaffId + '\'' +
                ", derateReasonCode=" + derateReasonCode +
                ", remark='" + remark + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", sumLateFee=" + sumLateFee +
                ", useTag=" + useTag +
                ", oldUseTag=" + oldUseTag +
                '}';
    }
}

package com.unicom.acting.fee.domain;

/**
 * 限额账本销账日志
 *
 * @author Wangkh
 */
public class LimitFeeDepositLog {
    private String chargeId;
    private String acctBalanceId;
    private String acctId;
    private String userId;
    private int partitionId;
    private int cycleId;
    private long usedMoney;
    private String eparchyCode;
    private String updateTime;
    private int rsrvItemCode;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private String rsrvInfo2;
    private char cancelTag;
    /**
     * @see #newFlag
     * 是否新增标志
     */
    private char newFlag;
    private long impMoney;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
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

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public long getUsedMoney() {
        return usedMoney;
    }

    public void setUsedMoney(long usedMoney) {
        this.usedMoney = usedMoney;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getRsrvItemCode() {
        return rsrvItemCode;
    }

    public void setRsrvItemCode(int rsrvItemCode) {
        this.rsrvItemCode = rsrvItemCode;
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

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public char getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(char newFlag) {
        this.newFlag = newFlag;
    }

    public long getImpMoney() {
        return impMoney;
    }

    public void setImpMoney(long impMoney) {
        this.impMoney = impMoney;
    }

    @Override
    public String toString() {
        return "LimitFeeDepositLog{" +
                "chargeId='" + chargeId + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", partitionId=" + partitionId +
                ", cycleId=" + cycleId +
                ", usedMoney=" + usedMoney +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", rsrvItemCode=" + rsrvItemCode +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", cancelTag=" + cancelTag +
                ", newFlag=" + newFlag +
                ", impMoney=" + impMoney +
                '}';
    }
}

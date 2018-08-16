package com.unicom.acting.fee.writeoff.domain;

/**
 * 欠费查询返回账本明细信息
 *
 * @author Wangkh
 */
public class DetailDepositInfo {
    private String serviceClassCode;
    /**
     * 用户归属地市编码
     */
    private String areaCode;
    private String serialNumber;
    private String acctId;
    private String userId;
    private String bindUserId;
    /**
     * 账本科目编码名称
     */
    private String depositName;
    private String acctBalanceId;
    private String depositCode;
    /**
     * 账本余额 = 奇偶月余额总和
     */
    private long depositMoney;
    /**
     * 本月使用金额
     */
    private long usedMoney;
    /**
     * 本月可用金额
     */
    private long canUsedMoney;
    /**
     * 可清退标志
     * 0-不可清退，1-可清退
     */
    private char returnTag;
    /**
     * 科目类型
     * 0：可用预存款
     * 1：冻结预存款
     * 2：可用赠款
     * 3：冻结赠款
     */
    private char depositTypeCode;
    /**
     * 款项类型标志
     * 0：普通款项
     * 1: 专项款项
     */
    private char feeKind;
    /**
     * 能否转账
     * 0：不能转账
     * 1：可以转账
     */
    private char canTranTag;
    /**
     * 私有标志
     * 0:账户级
     * 1:用户级
     */
    private char privateTag;
    /**
     * 有效标志
     * 0:正常
     * 1:冻结
     * 2:失效
     * 3:未激活状态
     */
    private char validTag;
    private String startDate;
    private String endDate;
    private int startCycleId;
    private int endCycleId;
    /**
     * 月转兑金额
     */
    private long amount;
    /**
     * 冻结金额
     */
    private long freezeFee;
    /**
     * 专用账本标识
     */
    private String specialFlag;

    private long bankFee;
    private long transFee;
    private char limitMode;                        //限定方式0:无限定 1:限额 2:话费比例限定(LIMIT_MONEY对应的比例)
    private long limitMoney;

    public char getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(char limitMode) {
        this.limitMode = limitMode;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public long getBankFee() {
        return bankFee;
    }

    public void setBankFee(long bankFee) {
        this.bankFee = bankFee;
    }

    public long getTransFee() {
        return transFee;
    }

    public void setTransFee(long transFee) {
        this.transFee = transFee;
    }

    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(String bindUserId) {
        this.bindUserId = bindUserId;
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

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(String depositCode) {
        this.depositCode = depositCode;
    }

    public long getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(long depositMoney) {
        this.depositMoney = depositMoney;
    }

    public long getUsedMoney() {
        return usedMoney;
    }

    public void setUsedMoney(long usedMoney) {
        this.usedMoney = usedMoney;
    }

    public long getCanUsedMoney() {
        return canUsedMoney;
    }

    public void setCanUsedMoney(long canUsedMoney) {
        this.canUsedMoney = canUsedMoney;
    }

    public char getReturnTag() {
        return returnTag;
    }

    public void setReturnTag(char returnTag) {
        this.returnTag = returnTag;
    }

    public char getDepositTypeCode() {
        return depositTypeCode;
    }

    public void setDepositTypeCode(char depositTypeCode) {
        this.depositTypeCode = depositTypeCode;
    }

    public char getFeeKind() {
        return feeKind;
    }

    public void setFeeKind(char feeKind) {
        this.feeKind = feeKind;
    }

    public char getCanTranTag() {
        return canTranTag;
    }

    public void setCanTranTag(char canTranTag) {
        this.canTranTag = canTranTag;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public char getValidTag() {
        return validTag;
    }

    public void setValidTag(char validTag) {
        this.validTag = validTag;
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

    public int getStartCycleId() {
        return startCycleId;
    }

    public void setStartCycleId(int startCycleId) {
        this.startCycleId = startCycleId;
    }

    public int getEndCycleId() {
        return endCycleId;
    }

    public void setEndCycleId(int endCycleId) {
        this.endCycleId = endCycleId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFreezeFee() {
        return freezeFee;
    }

    public void setFreezeFee(long freezeFee) {
        this.freezeFee = freezeFee;
    }

    @Override
    public String toString() {
        return "DetailDepositInfo{" +
                "serviceClassCode='" + serviceClassCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", bindUserId='" + bindUserId + '\'' +
                ", depositName='" + depositName + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", depositCode='" + depositCode + '\'' +
                ", depositMoney=" + depositMoney +
                ", usedMoney=" + usedMoney +
                ", canUsedMoney=" + canUsedMoney +
                ", returnTag=" + returnTag +
                ", depositTypeCode=" + depositTypeCode +
                ", feeKind=" + feeKind +
                ", canTranTag=" + canTranTag +
                ", privateTag=" + privateTag +
                ", validTag=" + validTag +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startCycleId=" + startCycleId +
                ", endCycleId=" + endCycleId +
                ", amount=" + amount +
                ", freezeFee=" + freezeFee +
                ", specialFlag='" + specialFlag + '\'' +
                ", bankFee=" + bankFee +
                ", transFee=" + transFee +
                ", limitMode=" + limitMode +
                ", limitMoney=" + limitMoney +
                '}';
    }
}

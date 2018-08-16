package com.unicom.acting.fee.domain;

/**
 * 欠费查询返回账单明细信息
 *
 * @author Wangkh
 */
public class DetailBillInfo {
    private String acctId;
    private int cycleId;
    private char canpayTag;
    private char payTag;
    //本月应缴  = 本月应收 + 已收滞纳金 + 本次滞纳金
    /**
     * 账目项应收
     * monthfee = fee + adjust_after
     */
    private long monthfee;
    /**
     * 账目项已收欠费
     * payFee = fee + adjust_after - balance
     */
    private long payFee;
    /**
     * 本次应缴滞纳金
     * monthLateFee = newLateFee - derateFee
     */
    private long monthLateFee;
    /**
     * 账目项已收滞纳金
     * payLateFee = late_fee - late_balance
     */
    private long payLateFee;
    /**
     * 滞纳金减免金额
     */
    private long derateFee;
    /**
     * 账目项欠费总和
     * sumDebFee = balance + newLateFee - derateFee
     */
    private long sumDebFee;
    /**
     * 本月应缴
     * spayFee = fee + adjust_after + late_fee + newLateFee - derateFee
     */
    private long spayFee;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public char getCanpayTag() {
        return canpayTag;
    }

    public void setCanpayTag(char canpayTag) {
        this.canpayTag = canpayTag;
    }

    public char getPayTag() {
        return payTag;
    }

    public void setPayTag(char payTag) {
        this.payTag = payTag;
    }

    public long getMonthfee() {
        return monthfee;
    }

    public void setMonthfee(long monthfee) {
        this.monthfee = monthfee;
    }

    public long getPayFee() {
        return payFee;
    }

    public void setPayFee(long payFee) {
        this.payFee = payFee;
    }

    public long getMonthLateFee() {
        return monthLateFee;
    }

    public void setMonthLateFee(long monthLateFee) {
        this.monthLateFee = monthLateFee;
    }

    public long getPayLateFee() {
        return payLateFee;
    }

    public void setPayLateFee(long payLateFee) {
        this.payLateFee = payLateFee;
    }

    public long getDerateFee() {
        return derateFee;
    }

    public void setDerateFee(long derateFee) {
        this.derateFee = derateFee;
    }

    public long getSumDebFee() {
        return sumDebFee;
    }

    public void setSumDebFee(long sumDebFee) {
        this.sumDebFee = sumDebFee;
    }

    public long getSpayFee() {
        return spayFee;
    }

    public void setSpayFee(long spayFee) {
        this.spayFee = spayFee;
    }

    @Override
    public String toString() {
        return "DetailBillInfo{" +
                "acctId='" + acctId + '\'' +
                ", cycleId=" + cycleId +
                ", canpayTag=" + canpayTag +
                ", payTag=" + payTag +
                ", monthfee=" + monthfee +
                ", payFee=" + payFee +
                ", monthLateFee=" + monthLateFee +
                ", payLateFee=" + payLateFee +
                ", derateFee=" + derateFee +
                ", sumDebFee=" + sumDebFee +
                ", spayFee=" + spayFee +
                '}';
    }
}

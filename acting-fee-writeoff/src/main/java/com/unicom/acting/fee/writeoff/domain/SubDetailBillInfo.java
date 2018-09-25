package com.unicom.acting.fee.writeoff.domain;

/**
 * 欠费查询返回子账单明细信息
 *
 * @author shaob
 */
public class SubDetailBillInfo {
    private String acctId;
    private int cycleIdb;
    private int integrateItemCode;
    private String integrateItem;
    private long fee;
    private long balance;
    private long lateBalance;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public int getCycleIdb() {
        return cycleIdb;
    }

    public void setCycleIdb(int cycleIdb) {
        this.cycleIdb = cycleIdb;
    }

    public int getIntegrateItemCode() {
        return integrateItemCode;
    }

    public void setIntegrateItemCode(int integrateItemCode) {
        this.integrateItemCode = integrateItemCode;
    }

    public String getIntegrateItem() {
        return integrateItem;
    }

    public void setIntegrateItem(String integrateItem) {
        this.integrateItem = integrateItem;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getLateBalance() {
        return lateBalance;
    }

    public void setLateBalance(long lateBalance) {
        this.lateBalance = lateBalance;
    }

    public char getCanpayTag() {
        return canpayTag;
    }

    public void setCanpayTag(char canpayTag) {
        this.canpayTag = canpayTag;
    }

    private char canpayTag;

    @Override
    public String toString() {
        return "SubDetailBillInfo{" +
                "acctId='" + acctId + '\'' +
                ", cycleIdb=" + cycleIdb +
                ", integrateItemCode=" + integrateItemCode +
                ", integrateItem='" + integrateItem + '\'' +
                ", fee=" + fee +
                ", balance=" + balance +
                ", lateBalance=" + lateBalance +
                ", canpayTag=" + canpayTag +
                '}';
    }
}
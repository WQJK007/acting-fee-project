package com.unicom.acting.fee.domain;

/**
 * 用户结余对象,主要是信控结余计算使用
 *
 * @author Wangkh
 */
public class UserBalance {
    /**
     * 是否默认付费用户
     */
    private char defaultPay;
    /**
     * 用户结余
     */
    private long balance;
    /**
     * 存在私有账本的用户
     */
    private char hasPrivate;
    /**
     * 用户欠费
     */
    private long oweFee;
    /**
     * 往月欠费
     */
    private long oldOweFee;

    /**
     * 销账后用户往月欠费
     */
    private long newBoweFee;

    public UserBalance() {
        defaultPay = '0';
        balance = 0;
        hasPrivate = '0';
        oweFee = 0;
        oldOweFee = 0;
        newBoweFee = 0;
    }

    public char getDefaultPay() {
        return defaultPay;
    }

    public void setDefaultPay(char defaultPay) {
        this.defaultPay = defaultPay;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public char getHasPrivate() {
        return hasPrivate;
    }

    public void setHasPrivate(char hasPrivate) {
        this.hasPrivate = hasPrivate;
    }

    public long getOweFee() {
        return oweFee;
    }

    public void setOweFee(long oweFee) {
        this.oweFee = oweFee;
    }

    public long getOldOweFee() {
        return oldOweFee;
    }

    public void setOldOweFee(long oldOweFee) {
        this.oldOweFee = oldOweFee;
    }

    public long getNewBoweFee() {
        return newBoweFee;
    }

    public void setNewBoweFee(long newBoweFee) {
        this.newBoweFee = newBoweFee;
    }

    @Override
    public String toString() {
        return "UserBalance{" +
                "defaultPay=" + defaultPay +
                ", balance=" + balance +
                ", hasPrivate=" + hasPrivate +
                ", oweFee=" + oweFee +
                ", oldOweFee=" + oldOweFee +
                ", newBoweFee=" + newBoweFee +
                '}';
    }
}

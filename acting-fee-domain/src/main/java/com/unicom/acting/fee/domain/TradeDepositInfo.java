package com.unicom.acting.fee.domain;

/**
 * 可清退账本明细信息
 *
 * @author Wangkh
 */
public class TradeDepositInfo {
    /**
     * @see #acctBalanceId 可清退账本实例
     */
    private String acctBalanceId;
    /**
     * @see #money 账本可清退金额
     */
    private long money;

    /**
     * @see #forceBakeTag 账本强制清退标识
     */
    private char forceBakeTag;


    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public char getForceBakeTag() {
        return forceBakeTag;
    }

    public void setForceBakeTag(char forceBakeTag) {
        this.forceBakeTag = forceBakeTag;
    }
}

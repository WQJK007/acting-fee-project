package com.unicom.acting.fee.domain;

/**
 * 帐本关系表，映射TF_F_ACCTBALANCE_REL表
 *
 * @author Wangkh
 */
public class FeeAcctBalanceRel {
    private String acctBalanceId;
    private String acctId;
    private String acctBalanceId2;
    private int rate;

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

    public String getAcctBalanceId2() {
        return acctBalanceId2;
    }

    public void setAcctBalanceId2(String acctBalanceId2) {
        this.acctBalanceId2 = acctBalanceId2;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "FeeAcctBalanceRel{" +
                "acctBalanceId='" + acctBalanceId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", acctBalanceId2='" + acctBalanceId2 + '\'' +
                ", rate=" + rate +
                '}';
    }
}

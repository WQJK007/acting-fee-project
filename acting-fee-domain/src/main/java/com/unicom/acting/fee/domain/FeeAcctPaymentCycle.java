package com.unicom.acting.fee.domain;

/**
 * 账户自定义缴费期，映射TD_B_PAYMENT_CYCLE和TF_F_ACCT_PAYMENT_CYCLE
 *
 * @author Wangkh
 */
public class FeeAcctPaymentCycle {
    private int offDays;
    private int offMonths;
    private int bundleMonths;
    private String inDate;

    public FeeAcctPaymentCycle() {
        offDays = 0;
        offMonths = 0;
        bundleMonths = 0;
    }

    public int getOffDays() {
        return offDays;
    }

    public void setOffDays(int offDays) {
        this.offDays = offDays;
    }

    public int getOffMonths() {
        return offMonths;
    }

    public void setOffMonths(int offMonths) {
        this.offMonths = offMonths;
    }

    public int getBundleMonths() {
        return bundleMonths;
    }

    public void setBundleMonths(int bundleMonths) {
        this.bundleMonths = bundleMonths;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    @Override
    public String toString() {
        return "FeeAcctPaymentCycle{" +
                "offDays=" + offDays +
                ", offMonths=" + offMonths +
                ", bundleMonths=" + bundleMonths +
                ", inDate='" + inDate + '\'' +
                '}';
    }
}

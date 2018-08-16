package com.unicom.acting.fee.domain;

/**
 * 储值方式和帐本科目关系，映射TD_B_PAYMENT_DEPOSIT
 *
 * @author Wangkh
 */
public class PaymentDeposit {
    private int paymentId;
    private int payFeeModeCode;
    private int ruleId;
    private int depositCode;
    private char privateTag;
    private char invoiceTag;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayFeeModeCode() {
        return payFeeModeCode;
    }

    public void setPayFeeModeCode(int payFeeModeCode) {
        this.payFeeModeCode = payFeeModeCode;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public char getInvoiceTag() {
        return invoiceTag;
    }

    public void setInvoiceTag(char invoiceTag) {
        this.invoiceTag = invoiceTag;
    }

    @Override
    public String toString() {
        return "paymentId = " + this.paymentId + ",payFeeModeCode = "
                + this.payFeeModeCode + ",ruleId = " + this.ruleId
                + ",depositCode = " + this.depositCode + ",privateTag = "
                + this.privateTag + ",invoiceTag = " + this.invoiceTag;
    }
}

package com.unicom.acting.fee.domain;

/**
 * 帐本科目规则和定义，映射TD_B_DEPOSITPRIORRULE，TD_B_DEPOSIT
 *
 * @author Wangkh
 */
public class DepositPriorRule {
    private int depositPriorRuleId;
    private int depositCode;
    private int depositPriority;
    private int itemPriorRuleId;
    private char returnTag;
    private char depositTypeCode;
    private char invoiceTag;
    private char canDisTag;
    private char canTranTag;
    private char canConsignTag;
    private char cashTag;
    private char cashType;
    private char ifBalance;
    private char creditMode;
    private char ifCalcOwe;
    private char ifUnite;
    private char ifAdjust;
    private String dealType;
    private String depositName;
    private int priority;

    public int getDepositPriorRuleId() {
        return depositPriorRuleId;
    }

    public void setDepositPriorRuleId(int depositPriorRuleId) {
        this.depositPriorRuleId = depositPriorRuleId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public int getDepositPriority() {
        return depositPriority;
    }

    public void setDepositPriority(int depositPriority) {
        this.depositPriority = depositPriority;
    }

    public int getItemPriorRuleId() {
        return itemPriorRuleId;
    }

    public void setItemPriorRuleId(int itemPriorRuleId) {
        this.itemPriorRuleId = itemPriorRuleId;
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

    public char getInvoiceTag() {
        return invoiceTag;
    }

    public void setInvoiceTag(char invoiceTag) {
        this.invoiceTag = invoiceTag;
    }

    public char getCanDisTag() {
        return canDisTag;
    }

    public void setCanDisTag(char canDisTag) {
        this.canDisTag = canDisTag;
    }

    public char getCanTranTag() {
        return canTranTag;
    }

    public void setCanTranTag(char canTranTag) {
        this.canTranTag = canTranTag;
    }

    public char getCanConsignTag() {
        return canConsignTag;
    }

    public void setCanConsignTag(char canConsignTag) {
        this.canConsignTag = canConsignTag;
    }

    public char getCashTag() {
        return cashTag;
    }

    public void setCashTag(char cashTag) {
        this.cashTag = cashTag;
    }

    public char getCashType() {
        return cashType;
    }

    public void setCashType(char cashType) {
        this.cashType = cashType;
    }

    public char getIfBalance() {
        return ifBalance;
    }

    public void setIfBalance(char ifBalance) {
        this.ifBalance = ifBalance;
    }

    public char getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(char creditMode) {
        this.creditMode = creditMode;
    }

    public char getIfCalcOwe() {
        return ifCalcOwe;
    }

    public void setIfCalcOwe(char ifCalcOwe) {
        this.ifCalcOwe = ifCalcOwe;
    }

    public char getIfUnite() {
        return ifUnite;
    }

    public void setIfUnite(char ifUnite) {
        this.ifUnite = ifUnite;
    }

    public char getIfAdjust() {
        return ifAdjust;
    }

    public void setIfAdjust(char ifAdjust) {
        this.ifAdjust = ifAdjust;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString() {
        return "depositPriorRuleId = " + this.depositPriorRuleId
                + ",depositCode = " + this.depositCode + ",depositPriority = "
                + this.depositPriority + ",itemPriorRuleId = "
                + this.itemPriorRuleId + ",returnTag = " + this.returnTag
                + ",depositTypeCode = " + this.depositTypeCode
                + ",invoiceTag = " + this.invoiceTag + ",canDisTag = "
                + this.canDisTag + ",canTranTag = " + this.canTranTag
                + ",canConsignTag = " + this.canConsignTag + ",cashTag = "
                + this.cashTag + ",cashType = " + this.cashType
                + ",ifBalance = " + this.ifBalance + ",creditMode = "
                + this.creditMode + ",ifCalcOwe = " + this.ifCalcOwe
                + ",ifUnite = " + this.ifUnite + ",ifAdjust = " + this.ifAdjust
                + ",dealType = " + this.dealType + ",depositName = "
                + this.depositName + ",priority = " + this.priority;
    }
}

package com.unicom.acting.fee.domain;

/**
 * 帐本科目限定规则，映射TD_B_DEPOSITLIMITRULE
 *
 * @author Wangkh
 */
public class DepositLimitRule {
    private int depositLimitRuleId;
    private int depositCode;
    private int itemCode;
    private char limitMode;
    private char limitType;

    public int getDepositLimitRuleId() {
        return depositLimitRuleId;
    }

    public void setDepositLimitRuleId(int depositLimitRuleId) {
        this.depositLimitRuleId = depositLimitRuleId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public char getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(char limitMode) {
        this.limitMode = limitMode;
    }

    public char getLimitType() {
        return limitType;
    }

    public void setLimitType(char limitType) {
        this.limitType = limitType;
    }

    public String toString() {
        return "depositLimitRuleId = " + this.depositLimitRuleId
                + ",depositCode = " + this.depositCode + ",itemCode = "
                + this.itemCode + ",limitMode = " + this.limitMode
                + ",limitType = " + this.limitType;

    }
}

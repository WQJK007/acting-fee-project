package com.unicom.acting.fee.domain;

/**
 * 帐目优先规则,映射TD_B_ITEMPRIORRULE
 *
 * @author Wangkh
 */
public class ItemPriorRule {
    private int itemPriorRuleId;
    private int itemCode;
    private int itemPriority;

    public int getItemPriorRuleId() {
        return itemPriorRuleId;
    }

    public void setItemPriorRuleId(int itemPriorRuleId) {
        this.itemPriorRuleId = itemPriorRuleId;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public int getItemPriority() {
        return itemPriority;
    }

    public void setItemPriority(int itemPriority) {
        this.itemPriority = itemPriority;
    }

    public String toString() {
        return "itemPriorRuleId = " + this.itemPriorRuleId + ",itemCode = "
                + this.itemCode + ",itemPriority = " + itemPriority;
    }
}

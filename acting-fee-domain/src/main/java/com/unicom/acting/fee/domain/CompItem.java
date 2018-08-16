package com.unicom.acting.fee.domain;

/**
 * 组合账目项表，映射TD_B_COMPITEM
 *
 * @author Wangkh
 */
public class CompItem {
    private int itemId;
    private int subItemId;
    private int subItemNo;

    public CompItem() {
        itemId = -1;
        subItemId = -1;
        subItemNo = -1;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSubItemId() {
        return subItemId;
    }

    public void setSubItemId(int subItemId) {
        this.subItemId = subItemId;
    }

    public int getSubItemNo() {
        return subItemNo;
    }

    public void setSubItemNo(int subItemNo) {
        this.subItemNo = subItemNo;
    }

    public String toString() {
        return "itemId = " + this.itemId + ",subItemId = " + this.subItemId
                + ",subItemNo = " + this.subItemNo;
    }
}

package com.unicom.acting.fee.domain;

/**
 * 明细帐目定义,映射TD_B_DETAILITEM
 *
 * @author Wangkh
 */
public class DetailItem {
    private int itemId;
    private String itemName;
    private char itemUesType;
    private char addupElemType;
    private char itemClass;
    private char oweTag;
    private char latefeeCalcTag;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public char getItemUesType() {
        return itemUesType;
    }

    public void setItemUesType(char itemUesType) {
        this.itemUesType = itemUesType;
    }

    public char getAddupElemType() {
        return addupElemType;
    }

    public void setAddupElemType(char addupElemType) {
        this.addupElemType = addupElemType;
    }

    public char getItemClass() {
        return itemClass;
    }

    public void setItemClass(char itemClass) {
        this.itemClass = itemClass;
    }

    public char getOweTag() {
        return oweTag;
    }

    public void setOweTag(char oweTag) {
        this.oweTag = oweTag;
    }

    public char getLatefeeCalcTag() {
        return latefeeCalcTag;
    }

    public void setLatefeeCalcTag(char latefeeCalcTag) {
        this.latefeeCalcTag = latefeeCalcTag;
    }

    @Override
    public String toString() {
        return "itemId = " + this.itemId + ",itemName = " + this.itemName
                + ",itemUesType = " + this.itemUesType + ",addupElemType = "
                + this.addupElemType + ",itemClass = " + this.itemClass
                + ",oweTag = " + this.oweTag + ",latefeeCalcTag = "
                + this.latefeeCalcTag;
    }
}

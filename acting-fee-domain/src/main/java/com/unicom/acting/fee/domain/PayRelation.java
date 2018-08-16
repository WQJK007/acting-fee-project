package com.unicom.acting.fee.domain;

/**
 * 付费关系表，映射TF_F_PAYRELATION
 *
 * @author Wangkh
 */
public class PayRelation {
    private String payrelationId;
    private String userId;
    private String itemCode;
    private String payPriority;
    private String addupMethod;
    private String addupMonths;
    private String limitType;
    private String limitValue;
    private String fillTag;
    private String acctId;
    private String bindType;
    private String discntPriority;
    private int defaultTag;
    private String actTag;
    private int startCycId;
    private int endCycId;
    private String updateTime;
    private String updateDepartId;
    private String updateStaffId;
    private String provCode;

    public String getPayrelationId() {
        return payrelationId;
    }

    public void setPayrelationId(String payrelationId) {
        this.payrelationId = payrelationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPayPriority() {
        return payPriority;
    }

    public void setPayPriority(String payPriority) {
        this.payPriority = payPriority;
    }

    public String getAddupMethod() {
        return addupMethod;
    }

    public void setAddupMethod(String addupMethod) {
        this.addupMethod = addupMethod;
    }

    public String getAddupMonths() {
        return addupMonths;
    }

    public void setAddupMonths(String addupMonths) {
        this.addupMonths = addupMonths;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public String getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(String limitValue) {
        this.limitValue = limitValue;
    }

    public String getFillTag() {
        return fillTag;
    }

    public void setFillTag(String fillTag) {
        this.fillTag = fillTag;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getDiscntPriority() {
        return discntPriority;
    }

    public void setDiscntPriority(String discntPriority) {
        this.discntPriority = discntPriority;
    }

    public int getDefaultTag() {
        return defaultTag;
    }

    public void setDefaultTag(int defaultTag) {
        this.defaultTag = defaultTag;
    }

    public String getActTag() {
        return actTag;
    }

    public void setActTag(String actTag) {
        this.actTag = actTag;
    }

    public int getStartCycId() {
        return startCycId;
    }

    public void setStartCycId(int startCycId) {
        this.startCycId = startCycId;
    }

    public int getEndCycId() {
        return endCycId;
    }

    public void setEndCycId(int endCycId) {
        this.endCycId = endCycId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateDepartId() {
        return updateDepartId;
    }

    public void setUpdateDepartId(String updateDepartId) {
        this.updateDepartId = updateDepartId;
    }

    public String getUpdateStaffId() {
        return updateStaffId;
    }

    public void setUpdateStaffId(String updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }
}

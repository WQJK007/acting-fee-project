package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 实时账单查询应答信息
 *
 * @author Wangkh
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SkyArkPropertyNamingStrategy.class)
public class RealBillRspInfo {
    private String acctId;
    private String userId;
    private int partitionId;
    private int cycleId;
    private int integrateItemCode;
    private long fee;
    private long balance;
    private long bDiscnt;
    private long aDiscnt;
    private long adjustBefore;
    private long adjustAfter;
    private String updateTime;
    private char canpayTag;
    private char payTag;
    private char billPayTag;
    private char prepayTag;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public int getIntegrateItemCode() {
        return integrateItemCode;
    }

    public void setIntegrateItemCode(int integrateItemCode) {
        this.integrateItemCode = integrateItemCode;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getbDiscnt() {
        return bDiscnt;
    }

    public void setbDiscnt(long bDiscnt) {
        this.bDiscnt = bDiscnt;
    }

    public long getaDiscnt() {
        return aDiscnt;
    }

    public void setaDiscnt(long aDiscnt) {
        this.aDiscnt = aDiscnt;
    }

    public long getAdjustBefore() {
        return adjustBefore;
    }

    public void setAdjustBefore(long adjustBefore) {
        this.adjustBefore = adjustBefore;
    }

    public long getAdjustAfter() {
        return adjustAfter;
    }

    public void setAdjustAfter(long adjustAfter) {
        this.adjustAfter = adjustAfter;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public char getCanpayTag() {
        return canpayTag;
    }

    public void setCanpayTag(char canpayTag) {
        this.canpayTag = canpayTag;
    }

    public char getPayTag() {
        return payTag;
    }

    public void setPayTag(char payTag) {
        this.payTag = payTag;
    }

    public char getBillPayTag() {
        return billPayTag;
    }

    public void setBillPayTag(char billPayTag) {
        this.billPayTag = billPayTag;
    }

    public char getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(char prepayTag) {
        this.prepayTag = prepayTag;
    }
}

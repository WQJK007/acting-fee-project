package com.unicom.acting.fee.domain;

/**
 * 滞纳金计算参数,映射TD_B_LATECALPARA表
 *
 * @author Wangkh
 */
public class LateCalPara {
    private int ruleId;
    private long startCycleId;
    private long endCycleId;
    private long lateFeeRatio1;
    private long lateFeeRatio2;
    private long iniCalFee;
    private long maxLateFee;
    private long maxDayNum;
    /**
     * @see #iniDays
     * 滞纳金开始计算天数
     */
    private int iniDays;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public long getStartCycleId() {
        return startCycleId;
    }

    public void setStartCycleId(long startCycleId) {
        this.startCycleId = startCycleId;
    }

    public long getEndCycleId() {
        return endCycleId;
    }

    public void setEndCycleId(long endCycleId) {
        this.endCycleId = endCycleId;
    }

    public long getLateFeeRatio1() {
        return lateFeeRatio1;
    }

    public void setLateFeeRatio1(long lateFeeRatio1) {
        this.lateFeeRatio1 = lateFeeRatio1;
    }

    public long getLateFeeRatio2() {
        return lateFeeRatio2;
    }

    public void setLateFeeRatio2(long lateFeeRatio2) {
        this.lateFeeRatio2 = lateFeeRatio2;
    }

    public long getIniCalFee() {
        return iniCalFee;
    }

    public void setIniCalFee(long iniCalFee) {
        this.iniCalFee = iniCalFee;
    }

    public long getMaxLateFee() {
        return maxLateFee;
    }

    public void setMaxLateFee(long maxLateFee) {
        this.maxLateFee = maxLateFee;
    }

    public long getMaxDayNum() {
        return maxDayNum;
    }

    public void setMaxDayNum(long maxDayNum) {
        this.maxDayNum = maxDayNum;
    }

    public int getIniDays() {
        return iniDays;
    }

    public void setIniDays(int iniDays) {
        this.iniDays = iniDays;
    }

    @Override
    public String toString() {
        return "LateCalPara{" +
                "ruleId=" + ruleId +
                ", startCycleId=" + startCycleId +
                ", endCycleId=" + endCycleId +
                ", lateFeeRatio1=" + lateFeeRatio1 +
                ", lateFeeRatio2=" + lateFeeRatio2 +
                ", iniCalFee=" + iniCalFee +
                ", maxLateFee=" + maxLateFee +
                ", maxDayNum=" + maxDayNum +
                ", iniDays=" + iniDays +
                '}';
    }
}

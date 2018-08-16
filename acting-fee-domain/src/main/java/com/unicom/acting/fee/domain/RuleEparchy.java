package com.unicom.acting.fee.domain;

/**
 * 地市销帐规则，映射TD_B_RULE_EPARCHY
 *
 * @author Wangkh
 */
public class RuleEparchy {
    private int ruleId;
    private char ruleType;
    private String provinceCode;
    private String eparchyCode;
    private String netTypeCode;
    private String condDesc;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public char getRuleType() {
        return ruleType;
    }

    public void setRuleType(char ruleType) {
        this.ruleType = ruleType;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getCondDesc() {
        return condDesc;
    }

    public void setCondDesc(String condDesc) {
        this.condDesc = condDesc;
    }

    public String toString() {
        return "ruleId = " + this.ruleId + ",ruleType = " + this.ruleType
                + ",provinceCode = " + this.provinceCode + ",eparchyCode = "
                + this.eparchyCode + ",netTypeCode = " + this.netTypeCode
                + ",condDesc = " + this.condDesc;
    }
}

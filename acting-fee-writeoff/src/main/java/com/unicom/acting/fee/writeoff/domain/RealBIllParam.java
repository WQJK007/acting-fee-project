package com.unicom.acting.fee.writeoff.domain;

import com.unicom.acting.common.domain.User;

import java.util.List;

/**
 * 实时账单查询请求参数
 */
public class RealBIllParam {
    /**
     * 灰度参数 仅微服务查询使用
     */
    private String headerGray;
    /**
     * 账户归属省份编码
     */
    private String provinceCode;
    /**
     * 账户归属地市编码
     */
    private String eparchyCode;
    /**
     * 账户标识
     */
    private String acctId;
    /**
     * 用户标识
     */
    private String userId;
    /**
     * 实时账单查询开始账期
     */
    private int startCycleId;
    /**
     * 实时账单查询结束账期
     */
    private int endCycleId;
    /**
     * 账户默认付费用户
     */
    private List<User> defalutUsers;

    public String getHeaderGray() {
        return headerGray;
    }

    public void setHeaderGray(String headerGray) {
        this.headerGray = headerGray;
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

    public int getStartCycleId() {
        return startCycleId;
    }

    public void setStartCycleId(int startCycleId) {
        this.startCycleId = startCycleId;
    }

    public int getEndCycleId() {
        return endCycleId;
    }

    public void setEndCycleId(int endCycleId) {
        this.endCycleId = endCycleId;
    }

    public List<User> getDefalutUsers() {
        return defalutUsers;
    }

    public void setDefalutUsers(List<User> defalutUsers) {
        this.defalutUsers = defalutUsers;
    }
}

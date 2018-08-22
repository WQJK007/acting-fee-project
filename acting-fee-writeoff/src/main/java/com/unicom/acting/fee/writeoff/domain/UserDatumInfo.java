package com.unicom.acting.fee.writeoff.domain;


import com.unicom.acting.fee.domain.FeeAccount;
import com.unicom.acting.fee.domain.User;

import java.util.List;

/**
 * 调用三户资料查询微服务返回格式化后的三户资料信息
 * 主要包括交易主用户，默认付费账户，账户默认付费用户列表，是否大合帐用户等信息
 *
 * @author Wangkh
 */
public class UserDatumInfo {
    private User mainUser;  //输入用户信息
    private FeeAccount feeAccount;  //付费账户信息
    private List<User> defaultPayUsers; //默认付费用户信息
    private boolean isBigAcct;  //大合帐用户

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }

    public FeeAccount getFeeAccount() {
        return feeAccount;
    }

    public void setFeeAccount(FeeAccount account) {
        this.feeAccount = account;
    }

    public List<User> getDefaultPayUsers() {
        return defaultPayUsers;
    }

    public void setDefaultPayUsers(List<User> defaultPayUsers) {
        this.defaultPayUsers = defaultPayUsers;
    }

    public boolean isBigAcct() {
        return isBigAcct;
    }

    public void setBigAcct(boolean bigAcct) {
        isBigAcct = bigAcct;
    }

    @Override
    public String toString() {
        return "UserDatumInfo{" +
                "mainUser=" + mainUser +
                ", feeAccount=" + feeAccount +
                ", defaultPayUsers=" + defaultPayUsers +
                ", isBigAcct=" + isBigAcct +
                '}';
    }
}

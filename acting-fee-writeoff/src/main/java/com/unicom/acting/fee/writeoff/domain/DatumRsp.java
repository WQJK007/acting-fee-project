package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 三户资料查询应答
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumRsp {
    @ApiModelProperty(name = "account", value = "默认付费账户", required = true)
    private DatumAccount account;
    @ApiModelProperty(name = "mainUser", value = "本次交易用户", required = true)
    private DatumUser mainUser;
    @ApiModelProperty(name = "defaultPayUsers", value = "账户默认付费用户", required = true)
    private List<DatumUser> defaultPayUsers;

    public DatumAccount getAccount() {
        return account;
    }

    public void setAccount(DatumAccount account) {
        this.account = account;
    }

    public DatumUser getMainUser() {
        return mainUser;
    }

    public void setMainUser(DatumUser mainUser) {
        this.mainUser = mainUser;
    }

    public List<DatumUser> getDefaultPayUsers() {
        return defaultPayUsers;
    }

    public void setDefaultPayUsers(List<DatumUser> defaultPayUsers) {
        this.defaultPayUsers = defaultPayUsers;
    }

    @Override
    public String toString() {
        return "UserDatumRsp{" +
                "account=" + account +
                ", mainUser=" + mainUser +
                ", defaultPayUsers=" + defaultPayUsers +
                '}';
    }
}

package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 用户资料查询
 *
 * @author Wangkh
 */

@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultUsersInfoRsp {
    private MainUser mainUser;
    private List<UserInfoRsp> defaultUsers;

    public MainUser getMainUser() {
        return mainUser;
    }

    public void setMainUser(MainUser mainUser) {
        this.mainUser = mainUser;
    }

    public List<UserInfoRsp> getDefaultUsers() {
        return defaultUsers;
    }

    public void setDefaultUsers(List<UserInfoRsp> defaultUsers) {
        this.defaultUsers = defaultUsers;
    }
}

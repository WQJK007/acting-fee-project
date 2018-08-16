package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 用户资料先关数据表操作，主要包括以下几张表：
 * TF_F_BADBILL_USERINFO 坏账用户资料表
 * TF_F_NOCALCLATEFEE_USER 免滞纳金计算用户表
 *
 * @author Wangkh
 */
public interface UserOtherInfoFeeDao extends IBaseDao {
    /**
     * 是否坏账账户
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    boolean isBadBillUser(String acctId, String provinceCode);

    /**
     * 是否免滞纳金计算用户
     *
     * @param userId
     * @param acctId
     * @param provinceCode
     * @return
     */
    boolean isNoCalcLateFeeUser(String userId, String acctId, String provinceCode);
}

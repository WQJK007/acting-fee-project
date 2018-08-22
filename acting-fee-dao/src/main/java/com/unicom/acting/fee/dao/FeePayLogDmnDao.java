package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.FeeDiscntDeposit;
import com.unicom.acting.fee.domain.FeePayLogDmn;

import java.util.List;

/**
 * 缴费日志相关数据库操作
 *
 * @author Wangkh
 */
public interface FeePayLogDmnDao extends IBaseDao {
    /**
     * @param acctId
     * @param getMode
     * @param provinceCode
     * @return
     */
    List<FeePayLogDmn> getPaylogDmnByAcctId(String acctId, String getMode, String provinceCode);

    /**
     * 是否存在交易记录
     *
     * @param tradeId
     * @param provinceCode
     * @return
     */
    boolean ifExistOuterTradeId(String tradeId, String provinceCode);

    /**
     * 用户活动实例
     *
     * @param acctId
     * @param userId
     * @param provinceId
     * @return
     */
    List<FeeDiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId);

}

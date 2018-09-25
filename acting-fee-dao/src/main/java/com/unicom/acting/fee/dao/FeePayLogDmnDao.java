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
     * 获取账务交易后台工单记录信息
     *
     * @param acctId
     * @param getMode
     * @param dbType
     * @param routeValue
     * @return
     */
    List<FeePayLogDmn> getPaylogDmnByAcctId(String acctId, String getMode, String dbType, String routeValue);

    /**
     * 用户活动实例
     *
     * @param acctId
     * @param userId
     * @return
     */
    List<FeeDiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId);

}

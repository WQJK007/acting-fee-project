package com.unicom.acting.fee.dao;

import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.domain.FeeAcctBalanceRel;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 账管相关数据库操作
 *
 * @author Wangkh
 */
public interface FeeAccountDepositDao extends IBaseDao {
    /**
     * 根据账户标识查询账本
     *
     * @param acctId
     * @return
     */
    List<FeeAccountDeposit> getAcctDepositByAcctId(String acctId);

    /**
     * 根据账户标识查询账本销账关系
     *
     * @param acctId
     * @return
     */
    List<FeeAcctBalanceRel> getAcctBalanceRelByAcctId(String acctId);
}

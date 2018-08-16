package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.Bill;

import java.util.List;

/**
 * 对TS_B_BILL,TS_B_BADBILL相关的一些操作
 *
 * @author Wangkh
 */
public interface BillFeeDao extends IBaseDao {
    /**
     * 按照账户和开始结束时间获取往月账单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    List<Bill> getBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode);

    /**
     * 按照账户,用户和开始结束时间获取往月账单
     *
     * @param acctId
     * @param userId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    List<Bill> getBillOweByUserId(String acctId, String userId, int startCycleId, int endCycleId, String provinceCode);

    /**
     * 按照账户和开始结束时间坏账账单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    List<Bill> getBadBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode);

    /**
     * 获取账户账单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    boolean getBillByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode);
}

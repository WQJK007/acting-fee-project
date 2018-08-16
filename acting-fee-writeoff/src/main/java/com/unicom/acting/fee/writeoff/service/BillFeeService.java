package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.Bill;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

import java.util.List;

/**
 * 对实时账单，往月账单和坏账账单等账单表的操作
 *
 * @author Administrators
 */
public interface BillFeeService extends IBaseService {
    /**
     * 查询内存库实时账单
     *
     * @param tradeCommInfoIn
     * @param acctId
     * @param userId
     * @param startCycleId
     * @param endCycleId
     * @return
     */
    List<Bill> getRealBillFromMemDB(TradeCommInfoIn tradeCommInfoIn, String acctId, String userId, int startCycleId, int endCycleId);

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
     * 获取账户账单所有
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    boolean hasPreCycleBillByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode);

    /**
     * 内存数据库方式，开帐标志由销帐打，抵扣时候尚未开帐，判断抵扣入库期间或增量出账期间
     * 如果有开帐帐期的帐单已入库需要去除实时帐单中的对应月份的部分
     * 因为这个时候开帐标识use_tag='0'有两个月的实时话费，而实际上已经有帐单入库了
     * 多个抵扣入库进程没有办法控制一起完成
     *
     * @param bills
     * @param realBills
     * @param preCurCycleId
     * @return
     */
    List<Bill> removeWriteOffRealBill(List<Bill> bills, List<Bill> realBills, String acctId, int preCurCycleId, String provinceCode);

    void updateRealBillId(List<Bill> realBill, int billIdCount, String eparchyCode, String provinceCode);

}

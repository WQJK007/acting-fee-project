package com.unicom.acting.fee.calc.service;

import com.unicom.acting.fee.domain.Bill;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;
import java.util.Set;

public interface BillService extends IBaseService {
    /**
     * 账单剩余欠费金额
     *
     * @param bill
     * @return
     */
    long getBillBalance(Bill bill);

    /**
     * 账单销账前总欠费
     *
     * @param bill
     * @return
     */
    long getOldBillBalance(Bill bill);

    //账单排序
    void billSort(List<Bill> bills, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 更新账目项销账标识
     *
     * @param bill
     */
    void setPayTag(Bill bill);

    //更新账单销账标识
    void setBillPayTag(List<Bill> bills);

    /**
     * 还原账单
     *
     * @param billList
     */
    void regressData(List<Bill> billList);

    //选择账单销账顺序
    void chooseWriteOff(Set<String> chooseUsers, Set<Integer> chooseCycles, Set<Integer> chooseItems, List<Bill> bills);

}

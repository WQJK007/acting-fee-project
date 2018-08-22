package com.unicom.acting.fee.calc.service;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;
import java.util.Set;

public interface BillCalcService extends IBaseService {
    /**
     * 账单剩余欠费金额
     *
     * @param feeBill
     * @return
     */
    long getBillBalance(FeeBill feeBill);

    /**
     * 账单销账前总欠费
     *
     * @param feeBill
     * @return
     */
    long getOldBillBalance(FeeBill feeBill);

    //账单排序
    void billSort(List<FeeBill> feeBills, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 更新账目项销账标识
     *
     * @param feeBill
     */
    void setPayTag(FeeBill feeBill);

    //更新账单销账标识
    void setBillPayTag(List<FeeBill> feeBills);

    /**
     * 还原账单
     *
     * @param feeBillList
     */
    void regressData(List<FeeBill> feeBillList);

    //选择账单销账顺序
    void chooseWriteOff(Set<String> chooseUsers, Set<Integer> chooseCycles, Set<Integer> chooseItems, List<FeeBill> feeBills);

}

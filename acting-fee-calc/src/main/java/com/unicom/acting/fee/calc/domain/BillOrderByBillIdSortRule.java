package com.unicom.acting.fee.calc.domain;

import com.unicom.acting.fee.domain.Bill;

import java.util.Comparator;

/**
 * 账单列表按照账单实例标识升序排列
 */
public class BillOrderByBillIdSortRule implements Comparator<Bill> {
    @Override
    public int compare(Bill left, Bill right) {
        //帐期优先
        if (left.getCycleId() < right.getCycleId()) {
            return -1;
        } else if (left.getCycleId() == right.getCycleId()) {
            //用户
            if (left.getUserId().compareTo(right.getUserId()) < 0) {
                return -1;
            } else if (left.getUserId().compareTo(right.getUserId()) == 0) {
                //帐单ID
                if (left.getBillId().compareTo(right.getBillId()) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}

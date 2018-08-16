package com.unicom.acting.fee.calc.domain;

import com.unicom.acting.fee.domain.Bill;

import java.util.Comparator;

/**
 * 账单列表按照以下规则升序排列
 * cycleId--> userId  --> integrateItemCode --> negativeTag
 */
public class BillNegativeTagSortRule implements Comparator<Bill> {
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
                if (left.getIntegrateItemCode() < right.getIntegrateItemCode() ) {
                    return -1;
                } else if (left.getIntegrateItemCode() == right.getIntegrateItemCode()) {
                    if (left.getNegativeTag() < right.getNegativeTag()) {
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
        } else {
            return 1;
        }
    }
}

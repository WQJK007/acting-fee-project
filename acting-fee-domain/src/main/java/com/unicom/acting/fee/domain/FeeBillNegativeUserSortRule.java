package com.unicom.acting.fee.domain;

import java.util.Comparator;

/**
 * 账单列表按照以下规则升序排列
 * cyclId -- > negativeUser --> userId --> negativeTag --> itemPriority --> itemCode -->balance
 */
public class FeeBillNegativeUserSortRule implements Comparator<FeeBill> {
    @Override
    public int compare(FeeBill left, FeeBill right) {
        //帐期优先
        if (left.getCycleId() < right.getCycleId()) {
            return -1;
        } else if (left.getCycleId() == right.getCycleId()) {
            //总费用为负数的用户
            if (left.getNegativeUser() < right.getNegativeUser()) {
                return -1;
            } else if (left.getNegativeUser() == right.getNegativeUser()) {
                //用户
                if (left.getUserId().compareTo(right.getUserId()) < 0) {
                    return -1;
                } else if (left.getUserId().compareTo(right.getUserId()) == 0) {
                    if (left.getNegativeTag() < right.getNegativeTag()) {
                        return -1;
                    } else if (left.getNegativeTag() == right.getNegativeTag()) {
                        if (left.getItemPriority() < right.getItemPriority() ) {
                            return -1;
                        } else if (left.getItemPriority() == right.getItemPriority()) {
                            if (left.getIntegrateItemCode() < right.getIntegrateItemCode()) {
                                return -1;
                            } else if (left.getIntegrateItemCode() == right.getIntegrateItemCode()) {
                                //如果有负账目会排在最前面
                                if (left.getBalance() < right.getBalance()) {
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

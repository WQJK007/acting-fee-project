package com.unicom.acting.fee.domain;


import java.util.Comparator;

/**
 * 账本销账比例关系按照账本实例做排序
 *
 * @author Wangkh
 */
public class FeeAcctBalanceRelSortRule implements Comparator<FeeAcctBalanceRel> {
    @Override
    public int compare(FeeAcctBalanceRel left, FeeAcctBalanceRel right) {
        if (left.getAcctBalanceId().compareTo(right.getAcctBalanceId()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}

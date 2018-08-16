package com.unicom.acting.fee.calc.domain;

import com.unicom.acts.pay.domain.AcctBalanceRel;

import java.util.Comparator;

/**
 * 账本销账比例关系按照账本实例做排序
 *
 * @author Wangkh
 */
public class AcctBalanceRelSortRule implements Comparator<AcctBalanceRel> {
    @Override
    public int compare(AcctBalanceRel left, AcctBalanceRel right) {
        if (left.getAcctBalanceId().compareTo(right.getAcctBalanceId()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}

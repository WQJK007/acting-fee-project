package com.unicom.acting.fee.calc.domain;

import com.unicom.acts.pay.domain.AccountDeposit;

import java.util.Comparator;

/**
 * 账本列表按照以下规则排序
 * 先私有再公有-->账本销账优先级-->账本销账开始账期-->账本实例标识按照升序排列
 *
 * @author Wangkh
 */
public class AcctDepositPrivateFirstSortRule implements Comparator<AccountDeposit> {
    @Override
    public int compare(AccountDeposit left, AccountDeposit right) {
        //私有先用
        if (left.getPrivateTag() < right.getPrivateTag()) {
            return 1;
        } else if (left.getPrivateTag() == right.getPrivateTag()) {
            if (left.getPriority() < right.getPriority()) {
                return -1;
            } else if (left.getPriority() == right.getPriority()) {
                //现在不能保证帐本标识小的就先进入系统，所以只能按时间排序
                if (left.getStartCycleId() < right.getStartCycleId()) {
                    return -1;
                } else if (left.getStartCycleId() == right.getStartCycleId()) {
                    if (left.getAcctBalanceId().compareTo(right.getAcctBalanceId()) < 0) {
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
            return -1;
        }
    }
}

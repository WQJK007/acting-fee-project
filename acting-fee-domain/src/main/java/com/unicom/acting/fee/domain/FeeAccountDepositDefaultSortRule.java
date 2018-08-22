package com.unicom.acting.fee.domain;


import java.util.Comparator;

/**
 * 账本列表按照以下规则升序排序：
 * 账本公私有标识-->账本销账优先级-->账本销账开始账期-->账本实例标识
 * @author Wangkh
 */
public class FeeAccountDepositDefaultSortRule implements Comparator<FeeAccountDeposit> {
    @Override
    public int compare(FeeAccountDeposit left, FeeAccountDeposit right) {
        if(left.getPrivateTag() < right.getPrivateTag()) {
            return -1;
        }else if (left.getPrivateTag() == right.getPrivateTag()) {
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
                }
                else {
                    return 1;
                }
            }
            else {
                return 1;
            }
        }else {
            return 1;
        }
    }
}

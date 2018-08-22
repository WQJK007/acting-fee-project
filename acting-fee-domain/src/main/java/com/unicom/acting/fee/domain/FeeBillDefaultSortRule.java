package com.unicom.acting.fee.domain;

import java.util.Comparator;

/**
 * 账单列表按照以下规则升序排列
 * cyclId --> userId --> billId --> itemPriority--> balance --> itemCode
 * 往月账单销账时排序也是用了这个类，排序逻辑暂时不修改
 *
 * @author Wangkh
 */
public class FeeBillDefaultSortRule implements Comparator<FeeBill> {
    @Override
    public int compare(FeeBill left, FeeBill right) {
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
                } else if (left.getBillId().compareTo(right.getBillId()) == 0) {
                    //账目优先级
                    if (left.getItemPriority() < right.getItemPriority()) {
                        return -1;
                    } else if (left.getItemPriority() == right.getItemPriority()) {
                        //账目欠费金额从小到大
                        if (left.getBalance() < right.getBalance()) {
                            return -1;
                        } else if (left.getBalance() == right.getBalance()) {
                            //账目编码
                            if (left.getIntegrateItemCode() < right.getIntegrateItemCode()) {
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
    }
}

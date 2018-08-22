package com.unicom.acting.fee.calc.service.impl;

import com.unicom.acting.fee.calc.service.BillCalcService;
import com.unicom.acting.fee.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 账单操作的一些公共方法
 *
 * @author Wangkh
 */
@Service
public class BillCalcServiceImpl implements BillCalcService {
    private static final Logger logger = LoggerFactory.getLogger(BillCalcServiceImpl.class);

    /**
     * 账单剩余欠费金额
     *
     * @param feeBill
     * @return
     */
    @Override
    public long getBillBalance(FeeBill feeBill) {
        return feeBill.getBalance() + feeBill.getLateBalance() + feeBill.getNewLateFee()
                - feeBill.getDerateFee() - feeBill.getCurrWriteOffBalance() - feeBill.getCurrWriteOffLate();
    }

    /**
     * 账单销账前总欠费
     *
     * @param feeBill
     * @return
     */
    @Override
    public long getOldBillBalance(FeeBill feeBill) {
        return feeBill.getBalance() + feeBill.getLateBalance() + feeBill.getNewLateFee() - feeBill.getDerateFee();
    }

    //账单排序
    @Override
    public void billSort(List<FeeBill> feeBills, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(feeBills)
                || writeOffRuleInfo == null) {
            return;
        }

        //默认不存在负账单，如果存在设置为true
        boolean sortDealFlag = false;
        //更新账单的负账单和负账单用户标识
        for (FeeBill pFeeBill : feeBills) {
            pFeeBill.setItemPriority(writeOffRuleInfo.findItemprior(pFeeBill.getIntegrateItemCode()));
            if (pFeeBill.getBalance() < 0) {
                pFeeBill.setNegativeTag('1');
                sortDealFlag = true;
            } else {
                pFeeBill.setNegativeTag('2');
            }
            pFeeBill.setNegativeUser('1');
        }

        if (sortDealFlag) {
            logger.debug("Exists NegativeBill");
            //按照负账单标识升序排列
            feeBills.sort(new FeeBillNegativeTagSortRule());
            this.updateBillNegUserTag(feeBills);
            //有负账单需要特殊排序处理
            this.updateBillNegTag(feeBills);
            //负账单排序规则
            feeBills.sort(new FeeBillNegativeUserSortRule());
        } else {
            //无负账单排序原则
            feeBills.sort(new FeeBillDefaultSortRule());

        }
    }

    //更新账单的用户负账单标识
    private void updateBillNegUserTag(List<FeeBill> feeBills) {
        if (CollectionUtils.isEmpty(feeBills)) {
            return;
        }
        /*
          处理对应有帐目的必须把对应的正帐目排到一起 如下:
         1009    -700
         1001    -500
         1001     500
         1002    -600
         1002     600
         1011    -100
         1011    -200
         1011     300
         */

        for (int i = 0; i < feeBills.size(); i++) {
            int nextIndex = i + 1;
            //和下一条比较，如果本条是负帐单标志，下一条也是设置成负账单标志
            if (nextIndex < feeBills.size() && '1' == feeBills.get(i).getNegativeTag()
                    && feeBills.get(i).getUserId().equals(feeBills.get(nextIndex).getUserId())
                    && feeBills.get(i).getCycleId() == feeBills.get(nextIndex).getCycleId()
                    && feeBills.get(i).getIntegrateItemCode() == feeBills.get(nextIndex).getIntegrateItemCode()) {
                feeBills.get(i + 1).setNegativeTag('1');
            }
        }
    }

    //负账单特殊排序处理
    private void updateBillNegTag(List<FeeBill> feeBills) {
        String userId = "";
        int cycleId = -1;
        int totalFee = 0;
        int pos = 0;
        for (int i = 0; i < feeBills.size(); ++i) {
            if (!"".equals(userId)
                    && (!userId.equals(feeBills.get(i).getUserId())
                    || cycleId != feeBills.get(i).getCycleId())) {
                if (totalFee < 0) {
                    pos = updateSubBillNegTag(feeBills, i, pos);
                } else {
                    pos = i;
                }
                totalFee = 0;
            }

            userId = feeBills.get(i).getUserId();
            cycleId = feeBills.get(i).getCycleId();
            totalFee += feeBills.get(i).getBalance();
        }
        //处理最后一个
        if (totalFee < 0) {
            updateSubBillNegTag(feeBills, -1, pos);
        }
    }

    //更新负账单标识
    private int updateSubBillNegTag(List<FeeBill> feeBills, int endIndex, int pos) {
        int index = pos;
        if (endIndex > 0) {
            while (index < endIndex) {
                feeBills.get(index).setNegativeUser('0');
                index++;
            }
        } else {
            while (index < feeBills.size()) {
                feeBills.get(index).setNegativeUser('0');
                index++;
            }
        }
        return index;
    }

    @Override
    public void setPayTag(FeeBill feeBill) {
        if (getBillBalance(feeBill) == 0) {
            if ('0' == feeBill.getOldPayTag() || '8' == feeBill.getOldPayTag()) {
                feeBill.setPayTag('1');
            } else if ('7' == feeBill.getOldPayTag()) {
                //托收在途销帐
                feeBill.setPayTag('9');
            } else if ('1' != feeBill.getPayTag()) {
                //pay_tag=3,4
                feeBill.setPayTag('5');
            }
        } else {
            //发票预打,托收还是保持原样
            if ('8' != feeBill.getOldPayTag() && '7' != feeBill.getOldPayTag()) {
                feeBill.setPayTag('0');
            }
        }
    }

    //更新账单销账标识
    @Override
    public void setBillPayTag(List<FeeBill> feeBills) {
        if (CollectionUtils.isEmpty(feeBills)) {
            return;
        }
        feeBills.sort(new FeeBillOrderByBillIdSortRule());  //按BILL_ID排序
        String billId = feeBills.get(0).getBillId();
        boolean payFlag = true;
        int k = 0;
        for (int i = 0; i < feeBills.size(); ++i) {
            if (billId.equals(feeBills.get(i).getBillId())) {
                if (payFlag && feeBills.get(i).getPayTag() != '1'
                        && feeBills.get(i).getPayTag() != '5'
                        && feeBills.get(i).getPayTag() != '9') {
                    payFlag = false;
                }
            } else {
                if (payFlag) {
                    for (int j = k; j < i; j++) {
                        feeBills.get(j).setBillPayTag('1');
                    }
                }

                k = i;
                if ('1' == feeBills.get(i).getPayTag()
                        || '9' == feeBills.get(i).getPayTag()
                        || '5' == feeBills.get(i).getPayTag()) {
                    payFlag = true;
                } else {
                    payFlag = false;
                }
                billId = feeBills.get(i).getBillId();
            }
        }

        if (payFlag) {
            for (int j = k; j < feeBills.size(); ++j) {
                feeBills.get(j).setBillPayTag('1');
            }
        }
    }

    /**
     * 还原账单
     *
     * @param feeBillList
     */
    @Override
    public void regressData(List<FeeBill> feeBillList) {
        if (CollectionUtils.isEmpty(feeBillList)) {
            return;
        }
        for (FeeBill feeBill : feeBillList) {
            feeBill.setCurrWriteOffBalance(0);
            feeBill.setCurrWriteOffLate(0);
            feeBill.setPayTag(feeBill.getOldPayTag());
            feeBill.setImpFee(0);
            //生成票据的还原
            feeBill.setGenNoteTag('0');
            feeBill.setBillPayTag('0');
            feeBill.setWriteoffFee1(feeBill.getRsrvFee1());
            feeBill.setWriteoffFee2(feeBill.getRsrvFee2());
            feeBill.setRsrvFee1(0);
            feeBill.setRsrvFee2(0);
        }
    }

    //选择账单销账顺序
    @Override
    public void chooseWriteOff(Set<String> chooseUsers, Set<Integer> chooseCycles, Set<Integer> chooseItems, List<FeeBill> feeBills) {
        if (CollectionUtils.isEmpty(feeBills)
                || (CollectionUtils.isEmpty(chooseUsers)
                && CollectionUtils.isEmpty(chooseCycles)
                && CollectionUtils.isEmpty(chooseItems))) {
            return;
        }

        //优先销账账单
        List<FeeBill> firstFeeBills = new ArrayList<>();
        //其他销账账单
        List<FeeBill> otherFeeBills = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chooseUsers)) {
            for (FeeBill pFeeBill : feeBills) {
                if (chooseUsers.contains(pFeeBill.getUserId())) {
                    firstFeeBills.add(pFeeBill);
                } else {
                    otherFeeBills.add(pFeeBill);
                }
            }
            feeBills.clear();
            feeBills.addAll(firstFeeBills);
            feeBills.addAll(otherFeeBills);
        }


        if (!CollectionUtils.isEmpty(chooseCycles)) {
            firstFeeBills.clear();
            otherFeeBills.clear();
            for (FeeBill pFeeBill : feeBills) {
                if (chooseCycles.contains(pFeeBill.getCycleId())) {
                    firstFeeBills.add(pFeeBill);
                } else {
                    otherFeeBills.add(pFeeBill);
                }
            }
            feeBills.clear();
            feeBills.addAll(firstFeeBills);
            feeBills.addAll(otherFeeBills);
        }

        if (!CollectionUtils.isEmpty(chooseItems)) {
            firstFeeBills.clear();
            otherFeeBills.clear();
            for (FeeBill pFeeBill : feeBills) {
                if (chooseItems.contains(pFeeBill.getIntegrateItemCode())) {
                    firstFeeBills.add(pFeeBill);
                } else {
                    otherFeeBills.add(pFeeBill);
                }
            }
            feeBills.clear();
            feeBills.addAll(firstFeeBills);
            feeBills.addAll(otherFeeBills);
        }
    }

}

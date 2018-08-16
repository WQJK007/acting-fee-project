package com.unicom.acting.fee.calc.service.impl;

import com.unicom.acting.fee.calc.domain.BillDefaultSortRule;
import com.unicom.acting.fee.calc.domain.BillNegativeTagSortRule;
import com.unicom.acting.fee.calc.domain.BillNegativeUserSortRule;
import com.unicom.acting.fee.calc.domain.BillOrderByBillIdSortRule;
import com.unicom.acting.fee.calc.service.BillService;
import com.unicom.acting.fee.domain.Bill;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
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
public class BillServiceImpl implements BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    /**
     * 账单剩余欠费金额
     *
     * @param bill
     * @return
     */
    @Override
    public long getBillBalance(Bill bill) {
        return bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee()
                - bill.getDerateFee() - bill.getCurrWriteOffBalance() - bill.getCurrWriteOffLate();
    }

    /**
     * 账单销账前总欠费
     *
     * @param bill
     * @return
     */
    @Override
    public long getOldBillBalance(Bill bill) {
        return bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee() - bill.getDerateFee();
    }

    //账单排序
    @Override
    public void billSort(List<Bill> bills, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(bills)
                || writeOffRuleInfo == null) {
            return;
        }

        //默认不存在负账单，如果存在设置为true
        boolean sortDealFlag = false;
        //更新账单的负账单和负账单用户标识
        for (Bill pBill : bills) {
            pBill.setItemPriority(writeOffRuleInfo.findItemprior(pBill.getIntegrateItemCode()));
            if (pBill.getBalance() < 0) {
                pBill.setNegativeTag('1');
                sortDealFlag = true;
            } else {
                pBill.setNegativeTag('2');
            }
            pBill.setNegativeUser('1');
        }

        if (sortDealFlag) {
            //按照负账单标识升序排列
            bills.sort(new BillNegativeTagSortRule());
            this.updateBillNegUserTag(bills);
            //有负账单需要特殊排序处理
            this.updateBillNegTag(bills);
            //负账单排序规则
            bills.sort(new BillNegativeUserSortRule());
        } else {
            //无负账单排序原则
            bills.sort(new BillDefaultSortRule());

        }
    }

    //更新账单的用户负账单标识
    private void updateBillNegUserTag(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
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

        for (int i = 0; i < bills.size(); i++) {
            int nextIndex = i + 1;
            //和下一条比较，如果本条是负帐单标志，下一条也是设置成负账单标志
            if (nextIndex < bills.size() && '1' == bills.get(i).getNegativeTag()
                    && bills.get(i).getUserId().equals(bills.get(nextIndex).getUserId())
                    && bills.get(i).getCycleId() == bills.get(nextIndex).getCycleId()
                    && bills.get(i).getIntegrateItemCode() == bills.get(nextIndex).getIntegrateItemCode()) {
                bills.get(i + 1).setNegativeTag('1');
            }
        }
    }

    //负账单特殊排序处理
    private void updateBillNegTag(List<Bill> bills) {
        String userId = "";
        int cycleId = -1;
        int totalFee = 0;
        int pos = 0;
        for (int i = 0; i < bills.size(); ++i) {
            if (!"".equals(userId)
                    && (!userId.equals(bills.get(i).getUserId())
                    || cycleId != bills.get(i).getCycleId())) {
                if (totalFee < 0) {
                    pos = updateSubBillNegTag(bills, i, pos);
                } else {
                    pos = i;
                }
                totalFee = 0;
            }

            userId = bills.get(i).getUserId();
            cycleId = bills.get(i).getCycleId();
            totalFee += bills.get(i).getBalance();
        }
        //处理最后一个
        if (totalFee < 0) {
            updateSubBillNegTag(bills, -1, pos);
        }
    }

    //更新负账单标识
    private int updateSubBillNegTag(List<Bill> bills, int endIndex, int pos) {
        int index = pos;
        if (endIndex > 0) {
            while (index < endIndex) {
                bills.get(index).setNegativeUser('0');
                index++;
            }
        } else {
            while (index < bills.size()) {
                bills.get(index).setNegativeUser('0');
                index++;
            }
        }
        return index;
    }

    @Override
    public void setPayTag(Bill bill) {
        if (getBillBalance(bill) == 0) {
            if ('0' == bill.getOldPayTag() || '8' == bill.getOldPayTag()) {
                bill.setPayTag('1');
            } else if ('7' == bill.getOldPayTag()) {
                //托收在途销帐
                bill.setPayTag('9');
            } else if ('1' != bill.getPayTag()) {
                //pay_tag=3,4
                bill.setPayTag('5');
            }
        } else {
            //发票预打,托收还是保持原样
            if ('8' != bill.getOldPayTag() && '7' != bill.getOldPayTag()) {
                bill.setPayTag('0');
            }
        }
    }

    //更新账单销账标识
    @Override
    public void setBillPayTag(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }
        bills.sort(new BillOrderByBillIdSortRule());  //按BILL_ID排序
        String billId = bills.get(0).getBillId();
        boolean payFlag = true;
        int k = 0;
        for (int i = 0; i < bills.size(); ++i) {
            if (billId.equals(bills.get(i).getBillId())) {
                if (payFlag && bills.get(i).getPayTag() != '1'
                        && bills.get(i).getPayTag() != '5'
                        && bills.get(i).getPayTag() != '9') {
                    payFlag = false;
                }
            } else {
                if (payFlag) {
                    for (int j = k; j < i; j++) {
                        bills.get(j).setBillPayTag('1');
                    }
                }

                k = i;
                if ('1' == bills.get(i).getPayTag()
                        || '9' == bills.get(i).getPayTag()
                        || '5' == bills.get(i).getPayTag()) {
                    payFlag = true;
                } else {
                    payFlag = false;
                }
                billId = bills.get(i).getBillId();
            }
        }

        if (payFlag) {
            for (int j = k; j < bills.size(); ++j) {
                bills.get(j).setBillPayTag('1');
            }
        }
    }

    /**
     * 还原账单
     *
     * @param billList
     */
    @Override
    public void regressData(List<Bill> billList) {
        if (CollectionUtils.isEmpty(billList)) {
            return;
        }
        for (Bill bill : billList) {
            bill.setCurrWriteOffBalance(0);
            bill.setCurrWriteOffLate(0);
            bill.setPayTag(bill.getOldPayTag());
            bill.setImpFee(0);
            //生成票据的还原
            bill.setGenNoteTag('0');
            bill.setBillPayTag('0');
            bill.setWriteoffFee1(bill.getRsrvFee1());
            bill.setWriteoffFee2(bill.getRsrvFee2());
            bill.setRsrvFee1(0);
            bill.setRsrvFee2(0);
        }
    }

    //选择账单销账顺序
    @Override
    public void chooseWriteOff(Set<String> chooseUsers, Set<Integer> chooseCycles, Set<Integer> chooseItems, List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)
                || (CollectionUtils.isEmpty(chooseUsers)
                && CollectionUtils.isEmpty(chooseCycles)
                && CollectionUtils.isEmpty(chooseItems))) {
            return;
        }

        //优先销账账单
        List<Bill> firstBills = new ArrayList<>();
        //其他销账账单
        List<Bill> otherBills = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chooseUsers)) {
            for (Bill pBill : bills) {
                if (chooseUsers.contains(pBill.getUserId())) {
                    firstBills.add(pBill);
                } else {
                    otherBills.add(pBill);
                }
            }
            bills.clear();
            bills.addAll(firstBills);
            bills.addAll(otherBills);
        }


        if (!CollectionUtils.isEmpty(chooseCycles)) {
            firstBills.clear();
            otherBills.clear();
            for (Bill pBill : bills) {
                if (chooseCycles.contains(pBill.getCycleId())) {
                    firstBills.add(pBill);
                } else {
                    otherBills.add(pBill);
                }
            }
            bills.clear();
            bills.addAll(firstBills);
            bills.addAll(otherBills);
        }

        if (!CollectionUtils.isEmpty(chooseItems)) {
            firstBills.clear();
            otherBills.clear();
            for (Bill pBill : bills) {
                if (chooseItems.contains(pBill.getIntegrateItemCode())) {
                    firstBills.add(pBill);
                } else {
                    otherBills.add(pBill);
                }
            }
            bills.clear();
            bills.addAll(firstBills);
            bills.addAll(otherBills);
        }
    }

}

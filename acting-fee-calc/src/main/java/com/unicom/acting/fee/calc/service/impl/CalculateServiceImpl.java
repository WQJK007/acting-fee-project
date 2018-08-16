package com.unicom.acting.fee.calc.service.impl;

import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.BillService;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.calc.service.DepositService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctPaymentCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 销账公共方法组，主要包括滞纳金计算，销账计算和结余结算等功能
 *
 * @author Wangkh
 */
@Service
public class CalculateServiceImpl implements CalculateService {
    private static final Logger logger = LoggerFactory.getLogger(CalculateServiceImpl.class);
    @Autowired
    private DepositService depositServiceImpl;
    @Autowired
    private BillService billServiceImpl;

    //一次销账计算
    @Override
    public void calc(TradeCommInfo tradeCommInfo) {
        //后付费的实时账单不参与计算
        tradeCommInfo.setBills(removePreRealBill(tradeCommInfo.getBills(), tradeCommInfo.getWriteOffRuleInfo()));
        //备份赠款销账金额
        backupWriteOffFee1(tradeCommInfo.getBills());
        //生成虚拟帐本
        depositServiceImpl.genVirtualAcctBalanceId(tradeCommInfo);
        //存折排序
        depositServiceImpl.accountDepositSort(tradeCommInfo.getAccountDeposits(), tradeCommInfo.getWriteOffRuleInfo());
        //账单排序
        billServiceImpl.billSort(tradeCommInfo.getBills(), tradeCommInfo.getWriteOffRuleInfo());
        //滞纳金计算
        if (tradeCommInfo.isCalcLateFee()) {
            calcLateFee(tradeCommInfo);
        }
        pay(tradeCommInfo);
        //删除虚拟账本
        depositServiceImpl.delVirtualAcctBalanceId(tradeCommInfo.getAccountDeposits(), tradeCommInfo.getVirtualRel());
        //结余计算
        genSimpleBalance(tradeCommInfo, false);
    }

    //二次销账计算
    @Override
    public void recvCalc(TradeCommInfo tradeCommInfo) {
        //清除数据
        regressData(tradeCommInfo);
        //生成虚拟帐本
        depositServiceImpl.genVirtualAcctBalanceId(tradeCommInfo);
        //存折排序
        depositServiceImpl.accountDepositSort(tradeCommInfo.getAccountDeposits(), tradeCommInfo.getWriteOffRuleInfo());
        //账单排序
        billServiceImpl.billSort(tradeCommInfo.getBills(), tradeCommInfo.getWriteOffRuleInfo());
        //选择销帐处理
        billServiceImpl.chooseWriteOff(tradeCommInfo.getChooseUserId(),
                tradeCommInfo.getChooseCycleId(), tradeCommInfo.getChooseItem(), tradeCommInfo.getBills());
        //销账计算
        pay(tradeCommInfo);
        //删除虚拟账本
        depositServiceImpl.delVirtualAcctBalanceId(tradeCommInfo.getAccountDeposits(), tradeCommInfo.getVirtualRel());
        //结余计算
        genSimpleBalance(tradeCommInfo, true);
        //生成存取款日志表
        genAccessLog(tradeCommInfo);
        //设置账单标识
        billServiceImpl.setBillPayTag(tradeCommInfo.getBills());
        //刨去新生成的滞纳金,生成实际的滞纳金，库外信控不走这步
        decLateFee(tradeCommInfo.getBills());
    }

    //剔除实时账单 改为return
    private List<Bill> removePreRealBill(List<Bill> billList, WriteOffRuleInfo writeOffRuleInfo) {
        //没有账单或者参数配置不剔除实时账单
        if (CollectionUtils.isEmpty(billList)
                || writeOffRuleInfo.isCanPrerealbillCalc()) {
            return billList;
        }

        List<Bill> tmpBillList = new ArrayList<>();
        //后付费的实时账单不参与计算
        for (Bill pBill : billList) {
            //准预付费的实时账单才输出 后付和预付均不输出
            if ('2' != pBill.getCanpayTag() || '1' == pBill.getPrepayTag()) {
                tmpBillList.add(pBill);
            }
        }

        if (billList.size() != tmpBillList.size()) {
            return tmpBillList;
        } else {
            return billList;
        }
    }

    //备份赠款销账金额
    private void backupWriteOffFee1(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }
        for (Bill pBill : bills) {
            pBill.setRsrvFee1(pBill.getWriteoffFee1());
            pBill.setRsrvFee2(pBill.getWriteoffFee2());
        }
    }

    //滞纳金计算
    private void calcLateFee(TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getBills())
                || tradeCommInfo.getWriteOffRuleInfo() == null
                || tradeCommInfo.getAccount() == null) {
            return;
        }

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        List<Bill> bills = tradeCommInfo.getBills();

        Account account = tradeCommInfo.getAccount();
        //坏帐算滞纳金标志
        boolean badBillCalc = writeOffRuleInfo.isBadBillCalcLateFee();

        //取得往月欠费，算滞纳金的本金, 存放每个帐期的往月欠费
        Map<Integer, Long> cycleIdBillBalance = genCycleIdBillBalance(bills);
        //离网用户销户时间
        Map<String, String> userDestroyDateMap = new HashMap();
        if (!CollectionUtils.isEmpty(cycleIdBillBalance)) {
            for (User user : tradeCommInfo.getPayUsers()) {
                if (!"0".equals(user.getRemoveTag()) && !StringUtil.isEmptyCheckNullStr(user.getDestroyDate())) {
                    userDestroyDateMap.put(user.getUserId(), user.getDestroyDate());
                }
            }
        }

        for (Map.Entry<Integer, Long> it : cycleIdBillBalance.entrySet()) {
            //滞纳金计算规则
            LateCalPara lateCalPara = writeOffRuleInfo.getLateCalPara(it.getKey());
            //账期欠费之和不大于0不参与滞纳金计算
            if (it.getValue() <= 0 || it.getValue() < lateCalPara.getIniCalFee()) {
                continue;
            }

            //账户自定义缴费期对应账期做滞纳金计算开始天数
            int lateCalcSpecialBeginDays = getLateCalcSpecialIniDays(tradeCommInfo, it.getKey());

            //滞纳金计算时间
            String lateFeeBeginTime = getLateCalBeginTime(it.getKey(), lateCalPara, lateCalcSpecialBeginDays);
            String calcDate = writeOffRuleInfo.getSysdate();
            //当前时间小于账期做滞纳金计算开始时间不做滞纳金计算
            if (calcDate.compareTo(lateFeeBeginTime) <= 0) {
                continue;
            }
            //月滞纳金
            long monthLateFee = 0;
            //零时滞纳金合计
            long tmpLateFeeSum = 0;

            for (Bill pBill : bills) {
                if (pBill.getCycleId() == it.getKey()) {
                    //不计算滞纳金或者系统配置坏账不算滞纳金的情况，不做滞纳金计算
                    if (!WriteOffRuleStaticInfo.isCalcLatefee(pBill.getIntegrateItemCode())
                            || (('7' == pBill.getCanpayTag() || '8' == pBill.getCanpayTag())
                            && !badBillCalc)) {
                        continue;
                    }

                    //如果存在滞纳金计算截止时间
                    if (!StringUtil.isEmpty(pBill.getLateCalDate()) && pBill.getLateCalDate().length() > 6) {
                        calcDate = pBill.getLateCalDate();
                    }

                    //离网用户滞纳金计算时间取销户时间
                    if (!CollectionUtils.isEmpty(userDestroyDateMap)
                            && userDestroyDateMap.containsKey(pBill.getUserId())) {
                        calcDate = userDestroyDateMap.get(pBill.getUserId());
                    }

                    //当前时间小于账单对应账期做滞纳金计算开始时间不做滞纳金计算
                    if (calcDate.compareTo(lateFeeBeginTime) < 0) {
                        continue;
                    }

                    long lateFee = 0;
                    //计算违约金天数
                    long days = TimeUtil.diffDays(calcDate, lateFeeBeginTime);
                    days = (days > lateCalPara.getMaxDayNum() ? lateCalPara.getMaxDayNum() : days);
                    //帐后调账调减出现1分钱误差修改
                    if ((pBill.getBalance() - pBill.getCurrWriteOffBalance()) < 0) {
                        lateFee += (double) (pBill.getBalance() - pBill.getCurrWriteOffBalance()) / 1000
                                * lateCalPara.getLateFeeRatio1() * days - 0.5;
                    } else {
                        lateFee += (double) (pBill.getBalance() - pBill.getCurrWriteOffBalance()) / 1000
                                * lateCalPara.getLateFeeRatio1() * days + 0.5;
                    }

                    //控制最大滞纳金减免金额
                    if (tmpLateFeeSum + pBill.getLateBalance() + lateFee > lateCalPara.getMaxLateFee()) {
                        lateFee = lateCalPara.getMaxLateFee() - (tmpLateFeeSum + pBill.getLateBalance());
                    }
                    tmpLateFeeSum += pBill.getLateBalance() + lateFee;

                    //滞纳金减免额度
                    long derateFee = derateLatefee(tradeCommInfo,
                            pBill.getAcctId(), pBill.getCycleId(), (pBill.getLateBalance() + lateFee));

                    //新增滞纳金,不包含减免的滞纳金
                    pBill.setNewLateFee(lateFee);
                    pBill.setDerateFee(pBill.getDerateFee() + derateFee);
                    monthLateFee += pBill.getLateBalance() + lateFee;
                }
            }

            //由于滞纳金按比例减免存在小数问题 所以需要修正处理
            DerateLateFeeLog derateLog = getDerateLateFeeLog(
                    tradeCommInfo.getDerateLateFeeLogs(), account.getAcctId(), it.getKey());
            //按比例
            if (derateLog != null && derateLog.getDerateRuleId() == 1) {
                //实际按比例需要减免的金额
                long tmpDeratefee = monthLateFee * derateLog.getDerateFee() / 100;
                //误差
                long errorFee = tmpDeratefee - derateLog.getUsedDerateFee();
                if (errorFee > 0 && !CollectionUtils.isEmpty(bills)) {
                    //少减免,不存在多减免的情况，因为是浮点型到整型的减免
                    for (int i = 0; i < bills.size(); ++i) {
                        if (bills.get(i).getCycleId() == it.getKey()) {
                            //新增滞纳金
                            long newAdd = bills.get(i).getNewLateFee() + bills.get(i).getLateBalance() - bills.get(i).getDerateFee();
                            if (newAdd > errorFee) {
                                //滞纳金够补误差
                                bills.get(i).setDerateFee(bills.get(i).getDerateFee() + errorFee);
                                errorFee = 0;
                                break;
                            } else {
                                bills.get(i).setDerateFee(bills.get(i).getDerateFee() + newAdd);
                                errorFee -= newAdd;
                            }
                        }
                    }
                }
                derateLog.setUsedDerateFee(tmpDeratefee);
            }
        }
    }

    //每个账期的总欠费
    private Map<Integer, Long> genCycleIdBillBalance(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return new HashMap();
        }
        Map<Integer, Long> cycBillBalance = new HashMap();
        for (Bill pBill : bills) {
            if (pBill.getCanpayTag() != '2'
                    && WriteOffRuleStaticInfo.isCalcLatefee(pBill.getIntegrateItemCode())) {
                long balance = pBill.getBalance() + pBill.getLateFee() - pBill.getCurrWriteOffBalance();
                if (cycBillBalance.containsKey(pBill.getCycleId())) {
                    long tmpFee = cycBillBalance.get(pBill.getCycleId()) + balance;
                    cycBillBalance.put(pBill.getCycleId(), tmpFee);
                } else {
                    cycBillBalance.put(pBill.getCycleId(), balance);
                }
            }
        }
        return cycBillBalance;
    }

    //账户自定义缴费期滞纳金计算开始计算天数
    private int getLateCalcSpecialIniDays(TradeCommInfo tradeCommInfo, int cycleId) {
        //没有自定义缴费期
        if (tradeCommInfo.getAcctPaymentCycle() == null
                || tradeCommInfo.getAcctPaymentCycle().getOffMonths() == 0
                && tradeCommInfo.getAcctPaymentCycle().getBundleMonths() == 0
                && tradeCommInfo.getAcctPaymentCycle().getOffDays() == 0) {
            return 0;
        }
        AcctPaymentCycle acctPaymentCycle = tradeCommInfo.getAcctPaymentCycle();

        //账户自定义缴费期还没生效
        if (Integer.parseInt(acctPaymentCycle.getInDate()) > cycleId) {
            return 0;
        }

        int buldeEndCycleId = 0;
        if (acctPaymentCycle.getBundleMonths() > 0) {
            //按创建时间计算
            buldeEndCycleId = Integer.parseInt(acctPaymentCycle.getInDate());
            buldeEndCycleId = TimeUtil.genCycle(buldeEndCycleId, -1);
            while (buldeEndCycleId < cycleId) {
                buldeEndCycleId = TimeUtil.genCycle(buldeEndCycleId, acctPaymentCycle.getBundleMonths());
            }
        } else {
            buldeEndCycleId = cycleId;
        }

        int endCycleId = TimeUtil.genCycle(buldeEndCycleId, acctPaymentCycle.getOffMonths());
        Cycle endCycle = WriteOffRuleStaticInfo.getCycle(endCycleId);
        String endDate = TimeUtil.dateAddDays(endCycle.getCycEndTime().substring(0, 10), acctPaymentCycle.getOffDays());
        Cycle thisCycle = WriteOffRuleStaticInfo.getCycle(cycleId);

        return TimeUtil.diffDays(endDate, thisCycle.getCycEndTime().substring(0, 10));
    }

    //账单销账计算
    private void pay(TradeCommInfo tradeCommInfo) {
        //账本集
        List<AccountDeposit> accountDepositList = tradeCommInfo.getAccountDeposits();
        //账单集
        List<Bill> billList = tradeCommInfo.getBills();

        //先使用负账本销账
        for (AccountDeposit accountDeposit : accountDepositList) {
            //被虚拟的账本不参与销帐
            if ('2' == accountDeposit.getVirtualTag()) {
                continue;
            }

            //先销负帐本
            if (depositServiceImpl.getRemainMoney(accountDeposit) < 0) {
                for (Bill pBill : billList) {
                    strikeBalance(tradeCommInfo, accountDeposit, pBill);
                }
            }
        }

        logger.debug("deposits.size = " + accountDepositList.size());

        for (AccountDeposit accountDeposit : accountDepositList) {
            logger.info("acctId= " + accountDeposit.getAcctId() + ",acctBalanceId=" + accountDeposit.getAcctBalanceId());
            //被虚拟的账本不参与销帐
            if ('2' == accountDeposit.getVirtualTag()) {
                continue;
            }

            for (Bill pBill : billList) {
                strikeBalance(tradeCommInfo, accountDeposit, pBill);
            }
        }

        //查询账单的时候副控制
        for (Bill pBill : billList) {
            if (StringUtil.isEmptyCheckNullStr(pBill.getLateCalDate())) {
                pBill.setLateCalDate("");
            }
        }
    }

    private void strikeBalance(TradeCommInfo tradeCommInfo, AccountDeposit deposit, Bill bill) {
        //已销帐账单不再计算处理
        if ('1' == bill.getPayTag() || '5' == bill.getPayTag() || '9' == bill.getPayTag()) {
            return;
        }

        //如果是私有账本，并且用户ID不相等就不能销账
        if ('1' == deposit.getPrivateTag() && !bill.getUserId().equals(deposit.getUserId())) {
            return;
        }

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //不可销往月帐的帐本,有特殊使用关系的返回(月结的时候没有开帐也符合条件)
        if (('0' == deposit.getIfCalcOwe() || '0' != deposit.getLimitMode())
                && bill.getCycleId() <= writeOffRuleInfo.getMaxAcctCycle().getCycleId()
                || tradeCommInfo.isAddAccount()
                && ('0' == deposit.getIfCalcOwe() || '0' != deposit.getLimitMode())
                && bill.getCycleId() <= TimeUtil.genCycle(
                writeOffRuleInfo.getMaxAcctCycle().getCycleId(), -1)) {
            return;
        }


        //帐单欠费余额
        long billBalance = billServiceImpl.getBillBalance(bill);
        //如果配置了销负账单的账本则转成指定账本，否则转成优先级最大的账本
        if (writeOffRuleInfo.getNegativeBillDeposit() >= 0
                && bill.getCanpayTag() != '2' && billBalance < 0
                && deposit.getDepositCode() != writeOffRuleInfo.getNegativeBillDeposit()) {
            return;
        }

        //帐本没有余额并且欠费大于0返回.
        if (depositServiceImpl.getRemainMoney(deposit) == 0 && billBalance > 0) {
            return;
        }

        String sysdate = writeOffRuleInfo.getSysdate();
        //有效账本才参与销账处理
        if (('0' == deposit.getValidTag() || '3' == deposit.getValidTag())
                && sysdate.compareTo(deposit.getStartDate()) >= 0
                && sysdate.compareTo(deposit.getEndDate()) <= 0
                && bill.getCycleId() >= deposit.getStartCycleId()
                && bill.getCycleId() <= deposit.getEndCycleId()) {

            //校验账本是否可销账目项
            boolean canPay = depositServiceImpl.canPay(writeOffRuleInfo.getDepositLimitRuleMap(),
                    deposit.getDepositCode(), bill.getIntegrateItemCode());
            if (canPay) {
                //账单实际销账金额
                long useFee = 0;
                //账本销账使用原预存款金额
                long impFee = 0;
                //账本销账可用金额
                long canUseFee = depositServiceImpl.getSimpleCanUseMoney(tradeCommInfo, deposit, bill.getUserId(), bill.getCycleId());

                //计算账单实际销账金额
                if (billBalance > canUseFee) {
                    useFee = canUseFee;
                } else {
                    useFee = billBalance;
                }

                //计算账本销账使用的原预存款金额和本次缴费金额
                if ((deposit.getMoney() - deposit.getImpFee()) > useFee) {
                    impFee = useFee;
                    deposit.setImpFee(deposit.getImpFee() + useFee);
                } else {
                    impFee = deposit.getMoney() - deposit.getImpFee();
                    //帐单大于0存折小于0或者帐单小于0 否则当存折金额小于0并且小于USEFEE的时候有BUG.
                    if (useFee < 0) {
                        impFee = useFee;
                    }
                    deposit.setImpFee(deposit.getImpFee() + impFee);
                    deposit.setUseRecvFee(deposit.getUseRecvFee() + (useFee - impFee));
                }

                //剩余本金是否大于使用金额(先销本金后销滞纳金)
                long leftBalance = bill.getBalance() - bill.getCurrWriteOffBalance();
                //账目项滞纳金金额
                long tmpLateFee = bill.getLateBalance() + bill.getNewLateFee() - bill.getDerateFee();
                //滞纳金剩余未销金额
                long tmpLeftLateBalance = tmpLateFee - bill.getCurrWriteOffLate();
                // 滞纳金使用金额
                long lateUse = 0;
                //该账目完全销账
                if (useFee == billBalance) {
                    bill.setCurrWriteOffBalance(bill.getCurrWriteOffBalance() + leftBalance);
                    lateUse = useFee - leftBalance;
                    bill.setCurrWriteOffLate(bill.getCurrWriteOffLate() + lateUse);
                } else if (0 == tmpLeftLateBalance) {
                    //或者没有滞纳金（可能有滞纳金减免)
                    if (leftBalance > useFee) {
                        bill.setCurrWriteOffBalance(bill.getCurrWriteOffBalance() + useFee);
                    } else {
                        //剩余本金小于 使用金额,必定有滞纳金参与了销帐
                        bill.setCurrWriteOffBalance(bill.getCurrWriteOffBalance() + leftBalance);
                        lateUse = useFee - leftBalance;
                        bill.setCurrWriteOffLate(bill.getCurrWriteOffLate() + lateUse);
                    }
                } else {
                    //该账目不完全销账,计算拆分比例
                    double lateRatio = (double) tmpLateFee / (bill.getBalance() + tmpLateFee);
                    lateUse = (long) (useFee * lateRatio);
                    if (lateUse > tmpLeftLateBalance) {
                        lateUse = tmpLeftLateBalance;
                    }
                    bill.setCurrWriteOffLate(bill.getCurrWriteOffLate() + lateUse);
                    bill.setCurrWriteOffBalance(bill.getCurrWriteOffBalance() + useFee - lateUse);
                }

                //0帐单，或者销帐帐单 产生销帐日志
                if (useFee != 0 || (billBalance == 0)) {
                    //第一次不生成对象
                    genWriteOffLogInfo(tradeCommInfo, bill, impFee, (useFee - impFee), lateUse, deposit);
                    bill.setImpFee(bill.getImpFee() + impFee);
                    if ('2' == bill.getCanpayTag()) {
                        //冲抵实时话费金额
                        deposit.setImpRealFee(deposit.getImpRealFee() + useFee);
                        deposit.setRealFeeRecv(deposit.getRealFeeRecv() + useFee - impFee);
                    }

                    //使用月限额帐本金额
                    if ('1' == deposit.getLimitMode()) {
                        depositServiceImpl.useLimitFeeDeposit(tradeCommInfo.getCurrLimitFeeDepositLog(), deposit, bill.getUserId(), bill.getCycleId(), useFee, impFee);
                    }
                }
            }
        }
    }

    /**
     * 生成销账日志
     *
     * @param tradeCommInfo
     * @param bill          本次销账账目
     * @param impFee        本次销账使用账本原预存宽金额
     * @param useRecvFee    本次销账使用充值金额
     * @param lateUse       滞纳金销账金额
     * @param deposit       本次销账账本
     */
    private void genWriteOffLogInfo(TradeCommInfo tradeCommInfo, Bill bill, long impFee, long useRecvFee,
                                    long lateUse, AccountDeposit deposit) {
        //更新账单销账后的状态
        billServiceImpl.setPayTag(bill);
        if (CollectionUtils.isEmpty(tradeCommInfo.getWriteOffLogs())) {
            tradeCommInfo.setWriteOffLogs(new ArrayList<>());
        }
        List<WriteOffLog> writeOffLogList = tradeCommInfo.getWriteOffLogs();

        Account account = tradeCommInfo.getAccount();
        List<AccountDeposit> acctDepositList = tradeCommInfo.getAccountDeposits();
        //虚拟帐本按比例销帐需要拆分
        if ('1' == deposit.getVirtualTag()) {
            //账本按比例销账实际分摊销账金额
            Map<String, Long> depositWriteOffFee = genDepositWriteOffFee(acctDepositList, tradeCommInfo.getVirtualRel(),
                    deposit.getAcctBalanceId(), (impFee + useRecvFee));
            //滞纳金累计销账金额
            long writeOffLateSum = 0;
            //本金累计销账金额
            long billBalanceSum = 0;
            for (String acctBalanceId : depositWriteOffFee.keySet()) {
                long rateFee = depositWriteOffFee.get(acctBalanceId);
                //避免0帐单销帐日志(按比例销账时，0账单销账没有日志)
                if (rateFee == 0) {
                    continue;
                }
                //更新账本销账金额信息
                long tmpImpFee = 0;
                AccountDeposit refDeposit = depositServiceImpl.getAcctDepositByAcctBalanceId(acctDepositList, acctBalanceId);
                if ((refDeposit.getMoney() - refDeposit.getImpFee()) > rateFee) {
                    tmpImpFee = rateFee;
                    refDeposit.setImpFee(refDeposit.getImpFee() + rateFee);
                } else {
                    tmpImpFee = refDeposit.getMoney() - refDeposit.getImpFee();
                    //帐单大于0存折小于0 或者 帐单小于0 否则当存折金额小于0并且小于USEFEE的时候有BUG.
                    if (rateFee < 0) {
                        tmpImpFee = rateFee;
                    }
                    refDeposit.setImpFee(refDeposit.getImpFee() + tmpImpFee);
                    refDeposit.setUseRecvFee(refDeposit.getUseRecvFee() + rateFee - tmpImpFee);
                }

                //冲抵实时话费金额
                if ('2' == bill.getCanpayTag()) {
                    refDeposit.setImpRealFee(refDeposit.getImpRealFee() + rateFee);
                    refDeposit.setRealFeeRecv(refDeposit.getRealFeeRecv() + rateFee - tmpImpFee);
                } else {
                    //设置账单参与销往月账，在线信控同步充值记录使用
                    refDeposit.setWriteOffOweFee(true);
                }
                //库外信控不生成销账日志，但是虚拟账本需要更新按比例销账后，每个账本的实际销账情况
                if (tradeCommInfo.isOuterCredit()) {
                    continue;
                }
                //剩余本金是否大于使用金额(先销本金后销滞纳金)
                long leftBalance = bill.getBalance() - (bill.getCurrWriteOffBalance() - impFee - useRecvFee + lateUse) - billBalanceSum;
                long tmpLateUse = 0;
                //剩余本金是否大于使用金额或者是负账单
                if ((leftBalance > rateFee) || rateFee < 0) {
                    billBalanceSum += rateFee;
                } else {
                    //剩余本金小于 使用金额,必定有滞纳金参与了销帐
                    billBalanceSum += leftBalance;
                    tmpLateUse = rateFee - leftBalance;
                }
                writeOffLateSum += tmpLateUse;

                //销账后账单欠费
                long newBillBlance = bill.getBalance() - (bill.getCurrWriteOffBalance() - impFee - useRecvFee + lateUse) - billBalanceSum;
                //销账前账单欠费
                long oldBillBalance = newBillBlance + rateFee - tmpLateUse;
                //销账后剩余滞纳金
                long newLateBalance = bill.getLateBalance() + bill.getNewLateFee() - bill.getDerateFee() - (bill.getCurrWriteOffLate() - lateUse) - writeOffLateSum;
                //销账前滞纳金
                long oldLateBalance = newLateBalance + tmpLateUse;
                writeOffLogList.add(genWriteOffLog(bill, account, refDeposit, rateFee,
                            tmpImpFee, oldBillBalance, newBillBlance, oldLateBalance, newLateBalance));
            }
        } else {
            //库外信控不生成销账日志
            if(tradeCommInfo.isOuterCredit()) {
                return;
            }
            //在线信控同步充值记录使用
            if ('2' != bill.getCanpayTag()) {
                deposit.setWriteOffOweFee(true);
            }
            //销账后账单欠费
            long newBillBlance = bill.getBalance() - bill.getCurrWriteOffBalance();
            //销账前账单欠费
            long oldBillBalance = newBillBlance + impFee + useRecvFee - lateUse;
            //销账前滞纳金
            long oldLateBalance = bill.getLateBalance() + bill.getNewLateFee() - bill.getDerateFee() - bill.getCurrWriteOffLate() + lateUse;
            //销账后剩余滞纳金
            long newLateBalance = oldLateBalance - lateUse;
            writeOffLogList.add(genWriteOffLog(bill, account, deposit, (impFee + useRecvFee),
                    impFee, oldBillBalance, newBillBlance, oldLateBalance, newLateBalance));
        }
    }

    /**
     * 生成销账日志记录
     *
     * @param bill         本次销账账单
     * @param account      账户
     * @param deposit      本次销账账本
     * @param writeOffFee  本次账本总销账金额
     * @param impFee       本次销账使用账本原预存款金额
     * @param oldBalance   账单缴费前欠费
     * @param newBalance   账单缴费后欠费
     * @param oldLafateFee 账单本次缴费应销滞纳金
     * @param newLateFee   销账后剩余滞纳金
     * @return 销账日志对象
     */
    private WriteOffLog genWriteOffLog(Bill bill, Account account, AccountDeposit deposit, long writeOffFee,
                                       long impFee, long oldBalance, long newBalance, long oldLafateFee, long newLateFee) {
        WriteOffLog writeOffLog = new WriteOffLog();
        if (StringUtil.isEmptyCheckNullStr(bill.getProvinceCode())) {
            writeOffLog.setProvinceCode(account.getProvinceCode());
        } else {
            writeOffLog.setProvinceCode(bill.getProvinceCode());
        }

        if (StringUtil.isEmptyCheckNullStr(bill.getEparchyCode())) {
            writeOffLog.setEparchyCode(account.getEparchyCode());
        } else {
            writeOffLog.setEparchyCode(bill.getEparchyCode());
        }

        if (StringUtil.isEmptyCheckNullStr(bill.getNetTypeCode())) {
            writeOffLog.setNetTypeCode(account.getNetTypeCode());
        } else {
            writeOffLog.setNetTypeCode(bill.getNetTypeCode());
        }

        writeOffLog.setAreaCode(bill.getRsrvInfo1());
        writeOffLog.setAcctId(bill.getAcctId());
        writeOffLog.setUserId(bill.getUserId());
        writeOffLog.setCycleId(bill.getCycleId());
        writeOffLog.setBillId(bill.getBillId());
        writeOffLog.setSerialNumber(bill.getSerialNumber());
        writeOffLog.setIntegrateItemCode(bill.getIntegrateItemCode());
        writeOffLog.setOldPaytag(bill.getOldPayTag());
        writeOffLog.setNewPaytag(bill.getPayTag());
        writeOffLog.setCanPaytag(bill.getCanpayTag());
        writeOffLog.setFee(bill.getFee());
        writeOffLog.setWriteoffFee(writeOffFee);
        writeOffLog.setImpFee(impFee);
        writeOffLog.setNewBalance(newBalance);
        writeOffLog.setOldBalance(oldBalance);
        writeOffLog.setAcctBalanceId(deposit.getAcctBalanceId());
        writeOffLog.setDepositCode(deposit.getDepositCode());
        writeOffLog.setLateFee(bill.getLateFee());
        writeOffLog.setLateBalance(bill.getLateBalance());
        writeOffLog.setLatecalDate(bill.getLateCalDate());
        // 本次新增的滞纳金
        writeOffLog.setNewLateFee(bill.getNewLateFee());
        writeOffLog.setOldLateBalance(oldLafateFee);
        writeOffLog.setNewLateBalance(newLateFee);
        // 减免滞纳金
        writeOffLog.setDerateLateFee(bill.getDerateFee());
        writeOffLog.setCancelTag('0');
        writeOffLog.setDrecvTimes(1);
        if (writeOffLog.getCycleId() > 200901) {
            //由于老系统的非现金冲抵通过负帐单实现，所以填写writeoffFee1,writeoffFee2的时候不填写。
            if ('2' == deposit.getDepositTypeCode() || '3' == deposit.getDepositTypeCode()) {
                //普通非现金,特殊非现金(赠款)
                bill.setWriteoffFee1(bill.getWriteoffFee1() + writeOffLog.getWriteoffFee());
            } else if ('1' == deposit.getDepositTypeCode()) {
                //特殊现金(协议)
                bill.setWriteoffFee2(bill.getWriteoffFee2() + writeOffLog.getWriteoffFee());
            }

        }
        return writeOffLog;
    }


    /**
     * 账本按比例销账做销账金额分摊
     *
     * @param acctDepositList      账户账本列表
     * @param virtualRelMap        账本比例关系
     * @param virtualAcctBalanceId 虚拟账本实例标识
     * @param writeOffFee          虚拟账本总销账金额
     * @return 账本实际销账金额
     */
    private Map<String, Long> genDepositWriteOffFee(List<AccountDeposit> acctDepositList, Map<String, Map<String, Long>> virtualRelMap,
                                                    String virtualAcctBalanceId, long writeOffFee) {
        //账本实际分摊金额
        Map<String, Long> depositWriteOffFee = new TreeMap<>();
        //虚拟关系
        if (!virtualRelMap.containsKey(virtualAcctBalanceId)) {
            throw new SkyArkException("虚拟帐本错误!acctBalanceId=" + virtualAcctBalanceId);
        }
        Map<String, Long> acctDepositRateMap = virtualRelMap.get(virtualAcctBalanceId);
        //账本按比例分摊实际销账总金额
        long factRateFee = 0;
        //acctBalanceId下的小先使用
        for (String acctBalanceId : acctDepositRateMap.keySet()) {
            AccountDeposit refDeposit = depositServiceImpl.getAcctDepositByAcctBalanceId(acctDepositList, acctBalanceId);
            //账本应该分摊金额
            long tmpUseFee = (long) (writeOffFee * ((double) acctDepositRateMap.get(acctBalanceId) / 100));
            //账本实际销账金额
            long factWriteOffFee = 0;
            if (depositServiceImpl.getRemainMoney(refDeposit) >= tmpUseFee) {
                factWriteOffFee = tmpUseFee;
            } else {
                factWriteOffFee = depositServiceImpl.getRemainMoney(refDeposit);
            }
            depositWriteOffFee.put(acctBalanceId, factWriteOffFee);
            factRateFee += factWriteOffFee;
        }

        //总销账金额-账本按比例分摊销账总金额后剩余的未分摊金额
        long oddment = writeOffFee - factRateFee;
        if (oddment != 0) {
            //未分摊金额用账本可用预存款再次分摊
            for (String acctBalanceId : depositWriteOffFee.keySet()) {
                AccountDeposit refDeposit = depositServiceImpl.getAcctDepositByAcctBalanceId(acctDepositList, acctBalanceId);
                //账本可用金额
                long tmpLeft = depositServiceImpl.getRemainMoney(refDeposit);
                long usedMoney = depositWriteOffFee.get(acctBalanceId) + oddment;
                if (tmpLeft >= usedMoney) {
                    depositWriteOffFee.put(acctBalanceId, usedMoney);
                    oddment = 0;
                } else {
                    oddment = usedMoney - tmpLeft;
                    depositWriteOffFee.put(acctBalanceId, tmpLeft);
                }
            }
            if (oddment != 0) {
                throw new SkyArkException("按比例拆分发生错误!acctBalanceId=" + virtualAcctBalanceId);
            }
        }

        return depositWriteOffFee;
    }


    /**
     * 生成结余信息
     *
     * @param tradeCommInfo
     * @param recvFlag
     */
    private void genSimpleBalance(TradeCommInfo tradeCommInfo, boolean recvFlag) {
        //销账快照表
        WriteSnapLog writeSnapLog = tradeCommInfo.getWriteSnapLog();
        //账户应缴金额
        long snapSpayFee = writeSnapLog.getSpayFee();
        //账户原结余
        long snapAllBalance = writeSnapLog.getAllBalance();
        //快照日志信息初始化
        writeSnapLog.init();
        Account account = tradeCommInfo.getAccount();
        User mainUser = tradeCommInfo.getMainUser();
        List<Bill> billList = tradeCommInfo.getBills();
        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        //用户结余对象
        Map<String, UserBalance> userBalance = genUserBalance(billList, depositList, tradeCommInfo.getPayUsers());
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //地市当前账期
        int curCycleId = writeOffRuleInfo.getCurCycle().getCycleId();

        //根据账单销账情况，更新用户结余和销账快照信息
        updateWriteSnapAndUserBalaneInfo(billList, writeSnapLog, userBalance, curCycleId);

        //需要考虑，没有最大开张账期的情况下，执行操作不会出问题
        int maxAcctCycleId = writeOffRuleInfo.getMaxAcctCycle().getCycleId();
        String sysdate = writeOffRuleInfo.getSysdate();
        //帐户结余(刨除分业务帐本，刨除私有帐本，刨除不算信控的帐本)
        long acctNewBalance = 0;
        //信控模式为1的特殊帐本余额，此类帐本如果有余额，并且有往月欠费 结余＝acctNotCashFee - 往月欠费
        long acctNewNotCashFee = 0;
        //统一余额播报信息
        UniBalanceInfo uniBalanceInfo = new UniBalanceInfo();

        //统一余额播报分省控制在账户当前可用余额中剔除部分账本余额
        boolean rejectSpeDeposit = false;
        //需要剔除的账本
        String[] rejectSpeDeposits = null;
        //需要剔除的专项账本金额
        long rejectFee = 0;
        CommPara commPara = writeOffRuleInfo.getCommpara(ASM_BALAN_BORD);
        if (commPara != null && "1".equals(commPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())) {
            rejectSpeDeposit = true;
            rejectSpeDeposits = commPara.getParaCode2().split("\\|");
        }

        for (AccountDeposit deposit : depositList) {
            //无效的和不能销任何帐本的存折
            boolean unAvialDeposit = false;
            //按用户缴费计算账户公有和缴费用户私有账本，按账户缴费计算全部
            boolean canCalcBalance = false;
            //过滤无效账本
            if (('0' != deposit.getValidTag() && '3' != deposit.getValidTag())
                    || sysdate.compareTo(deposit.getEndDate()) > 0) {
                continue;
            }
            //过滤未生效账本处理
            if (sysdate.compareTo(deposit.getStartDate()) < 0) {
                //未生效账本不算结余，但是需要按照统一余额播报规则统计余额
                if ('0' == deposit.getDepositTypeCode() || '1' == deposit.getDepositTypeCode()) {
                    //未生效现金累加到普通冻结预存款
                    uniBalanceInfo.setFrozenOrdPreFee(uniBalanceInfo.getFrozenOrdPreFee() + depositServiceImpl.getOddEvenMoney(deposit));
                } else {
                    //未生效赠款累加到普通冻结赠款
                    uniBalanceInfo.setFrozenOrdGrants(uniBalanceInfo.getFrozenOrdGrants() + depositServiceImpl.getOddEvenMoney(deposit));
                }
                continue;
            }

            //校验账本是否无效
            if (deposit.getEndCycleId() < curCycleId && deposit.getImpFee() <= 0) {
                continue;
            }

            //不能销任何账
            if (writeOffRuleInfo.isCannotUseDeposit(deposit.getDepositCode())) {
                unAvialDeposit = true;
            }

            //校验账本是否可参与计算余额
            if (tradeCommInfo.getWriteOffMode() != 2 || deposit.getPrivateTag() == '0'
                    || deposit.getUserId().equals(mainUser.getUserId())) {
                canCalcBalance = true;
            }

            //过滤无效的和不能销任何帐本的存折
            if (unAvialDeposit && canCalcBalance && deposit.getEndCycleId() >= curCycleId) {
                //销往月欠费后剩余金额(没有扣减账本冻结金额)
                long tmpNewMoney = depositServiceImpl.getNewDepositMoney(deposit);
                if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode()) || '1' == deposit.getPrivateTag()) {
                    if ('0' == deposit.getDepositTypeCode() || '1' == deposit.getDepositTypeCode()) {
                        //专项冻结预存款
                        uniBalanceInfo.setFrozenSpePreFee(uniBalanceInfo.getFrozenSpePreFee() + tmpNewMoney);
                        deposit.setDepositTypeCode('1');
                    } else if ('2' == deposit.getDepositTypeCode() || '3' == deposit.getDepositTypeCode()) {
                        //专项冻结赠款
                        uniBalanceInfo.setFrozenSpeGrants(uniBalanceInfo.getFrozenSpeGrants() + tmpNewMoney);
                        deposit.setDepositTypeCode('3');
                    }
                } else {
                    if ('0' == deposit.getDepositTypeCode() || '1' == deposit.getDepositTypeCode()) {
                        //普通冻结预存款
                        uniBalanceInfo.setFrozenOrdPreFee(uniBalanceInfo.getFrozenOrdPreFee() + tmpNewMoney);
                        deposit.setDepositTypeCode('1');
                    } else if ('2' == deposit.getDepositTypeCode() || '3' == deposit.getDepositTypeCode()) {
                        //普通冻结赠款
                        uniBalanceInfo.setFrozenOrdGrants(uniBalanceInfo.getFrozenOrdGrants() + tmpNewMoney);
                        deposit.setDepositTypeCode('3');
                    }
                }
                continue;
            }
            //帐本剩余可用金额
            long remainMoney = depositServiceImpl.getSimpleLeftMoney(tradeCommInfo, deposit, deposit.getUserId(), maxAcctCycleId, curCycleId);
            //账本剩余可用金额，后面统计可用余额使用
            deposit.setLeftCanUse(remainMoney);
            //账本缴费前金额
            long oldMoney = remainMoney + deposit.getUseRecvFee() - deposit.getRecvFee() + deposit.getImpFee();
            //账本缴费后金额
            long newMoney = remainMoney + deposit.getImpRealFee();
            //限额账本扣除可用金额，剩余金额需要纳入冻结款 (例如26账本，账本总金额80，限额50，销账40，剩余30需要纳入冻结款)
            long limitDepositLeftMoney = depositServiceImpl.getOddEvenMoney(deposit) - remainMoney - deposit.getImpFee() - deposit.getUseRecvFee();
            if (!writeOffRuleInfo.isCannotUseDeposit(deposit.getDepositCode())) {
                writeSnapLog.setAllMoney(writeSnapLog.getAllMoney() + oldMoney);
                writeSnapLog.setAllNewMoney(writeSnapLog.getAllNewMoney() + newMoney);
            }
            //账户原预存款销账金额
            writeSnapLog.setaImpFee(writeSnapLog.getaImpFee() + deposit.getImpFee());

            //可用账本计算账户余额
            if (canCalcBalance && !writeOffRuleInfo.isCannotUseDeposit(deposit.getDepositCode())) {
                uniBalanceInfo.setAllMoney(uniBalanceInfo.getAllMoney() + oldMoney);
                uniBalanceInfo.setAllNewMoney(uniBalanceInfo.getAllNewMoney() + newMoney);
                //专项余额
                if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode()) || '1' == deposit.getPrivateTag()) {
                    if ('0' == deposit.getDepositTypeCode() || '1' == deposit.getDepositTypeCode()) {
                        //专项可用预存款
                        uniBalanceInfo.setAvailSpePreFee(uniBalanceInfo.getAvailSpePreFee() + newMoney);
                        //专项冻结预存款
                        uniBalanceInfo.setFrozenSpePreFee(uniBalanceInfo.getFrozenSpePreFee() + limitDepositLeftMoney);
                        deposit.setDepositTypeCode('0');
                    } else if ('2' == deposit.getDepositTypeCode() || '3' == deposit.getDepositTypeCode()) {
                        //专项可用赠款
                        uniBalanceInfo.setAvailSpeGrants(uniBalanceInfo.getAvailSpeGrants() + newMoney);
                        //专项冻结赠款
                        uniBalanceInfo.setFrozenSpeGrants(uniBalanceInfo.getFrozenSpeGrants() + limitDepositLeftMoney);
                        deposit.setDepositTypeCode('2');
                    }
                } else {
                    //普通余额
                    if ('0' == deposit.getDepositTypeCode() || '1' == deposit.getDepositTypeCode()) {
                        //普通可用预存款
                        uniBalanceInfo.setAvailOrdiPreFee(uniBalanceInfo.getAvailOrdiPreFee() + newMoney);
                        //普通冻结预存款
                        uniBalanceInfo.setFrozenOrdPreFee(uniBalanceInfo.getFrozenOrdPreFee() + limitDepositLeftMoney);
                        deposit.setDepositTypeCode('0');
                    } else if ('2' == deposit.getDepositTypeCode() || '3' == deposit.getDepositTypeCode()) {
                        //普通可用赠款
                        uniBalanceInfo.setAvailOrdGrants(uniBalanceInfo.getAvailOrdGrants() + newMoney);
                        //普通冻结赠款
                        uniBalanceInfo.setFrozenOrdGrants(uniBalanceInfo.getFrozenOrdGrants() + limitDepositLeftMoney);
                        deposit.setDepositTypeCode('2');
                    }
                }
            }

            if ('1' == deposit.getIfBalance() && '1' == deposit.getCreditMode()) {
                acctNewNotCashFee += remainMoney;
            }

            //处理私有帐本帐本余额
            boolean specialFlag = false;
            if ('1' == deposit.getPrivateTag() || '2' == deposit.getPrivateTag()) {
                specialFlag = true;
            }

            logger.debug("remainMoney = " + remainMoney);

            //更新用户结余
            for (Map.Entry<String, UserBalance> it : userBalance.entrySet()) {
                if (it.getValue().getDefaultPay() == '1' && it.getValue().getBalance() >= 0
                        && '1' == deposit.getIfBalance()) {
                    if (specialFlag) {
                        //私有账本只累计用户自己的结余
                        if ('1' == deposit.getPrivateTag() && deposit.getUserId().equals(it.getKey())) {
                            it.getValue().setBalance(it.getValue().getBalance() + remainMoney);
                        }
                    } else {
                        //账户不存在欠费，公有账本可用余额才计入用户结余
                        if (writeSnapLog.getAllNewBalance() == 0) {
                            it.getValue().setBalance(it.getValue().getBalance() + remainMoney);
                        }
                    }
                }
            }

            //账户没有欠费，非特殊帐本,并且账本余额算入结余
            if (writeSnapLog.getAllNewBalance() == 0 && '1' == deposit.getIfBalance() && !specialFlag) {
                acctNewBalance += remainMoney;
            }

            //累加需要剔除的特殊账本可用余额
            if (rejectSpeDeposit && '1' == deposit.getIfBalance() && !specialFlag) {
                for (String depositCode : rejectSpeDeposits) {
                    if (depositCode.equals(String.valueOf(deposit.getDepositCode()))) {
                        rejectFee += deposit.getLeftCanUse();
                    }
                }
            }
        }

        logger.info("缴费后账户结余:" + acctNewBalance);

        if (writeSnapLog.getAllNewBalance() == 0) {
            writeSnapLog.setAllNewBalance(acctNewBalance);
        } else {
            //信控模式为1的特殊帐本余额，此类帐本如果有余额，并且有往月欠费,结余＝acctNotCashFee - 往月欠费
            if (acctNewNotCashFee > 0) {
                writeSnapLog.setAllNewBalance(writeSnapLog.getAllNewBalance() + acctNewNotCashFee);
            }
        }

        int defaultCount = 0;
        long defaultSingleBalance = 0;
        for (Map.Entry<String, UserBalance> it : userBalance.entrySet()) {
            //用户存在往月欠费(实时结余小于0),就设置为帐户余额
            if (it.getValue().getBalance() <= 0 && '1' == it.getValue().getDefaultPay()) {
                it.getValue().setBalance(writeSnapLog.getAllNewBalance());
                //广东托收的用户结余刨除往月欠费，此处应记录账户往月欠费
                it.getValue().setNewBoweFee(writeSnapLog.getAllNewBOweFee());
            }

            if ('1' == it.getValue().getDefaultPay()) {
                defaultCount++;
                defaultSingleBalance = it.getValue().getBalance();
            }
        }

        //如果是非合帐户用户,如果用户结余>0，帐户结余和用户结余一样(不管有没有私有帐本)!
        if(!tradeCommInfo.isOuterCredit()) {
            if (1 == defaultCount && defaultSingleBalance > 0) {
                writeSnapLog.setAllNewBalance(defaultSingleBalance);
            }
        }

        logger.info("缴费销账快照入库实际结余:" + writeSnapLog.getAllNewBalance());

        //缴费模式
        if (recvFlag) {
            writeSnapLog.setAllBalance(snapAllBalance);
            writeSnapLog.setSpayFee(snapSpayFee);
        } else {
            //欠费查询模式
            writeSnapLog.setAllBalance(writeSnapLog.getAllNewBalance());
            if (writeSnapLog.getAllBalance() < 0) {
                writeSnapLog.setSpayFee(-writeSnapLog.getAllBalance());
            } else {
                writeSnapLog.setSpayFee(0);
            }
        }

        //设置账户当前可用余额
        if (rejectSpeDeposit) {
            if (writeSnapLog.getAllNewBalance() > 0) {
                writeSnapLog.setCurrentAvlFee(writeSnapLog.getAllNewBalance() - rejectFee);
            } else {
                writeSnapLog.setCurrentAvlFee(writeSnapLog.getAllNewBalance());
            }
        } else {
            writeSnapLog.setCurrentAvlFee(writeSnapLog.getAllNewBalance());
        }

        logger.info("账户当前可用余额:" + writeSnapLog.getCurrentAvlFee());

        writeSnapLog.setAcctId(account.getAcctId());
        writeSnapLog.setProvinceCode(account.getProvinceCode());
        writeSnapLog.setEparchyCode(account.getEparchyCode());
        logger.debug("销账快照数据: " + writeSnapLog.toString());

        //设置抵扣后帐本往月欠费,供库外信控使用
        if (tradeCommInfo.isOuterCredit()) {
            long oldGeneralOweFee = 0;
            int maxOpenCycleId = -1;
            //用户最大开张帐期列表
            Map<String, Integer> maxUserOpenCycleId = new HashMap<>();
            for (AccountDeposit deposit : depositList) {
                //过滤无效的账本，无效的账本可能oweFee不准
                if (sysdate.compareTo(deposit.getEndDate()) > 0) {
                    continue;
                }

                if ('1' == deposit.getPrivateTag() && deposit.getUserId().length() > 2) {
                    if (deposit.getStartCycleId() > curCycleId) {
                        continue;
                    }

                    if (!userBalance.containsKey(deposit.getUserId())) {
                        throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "没有对于的欠费用户列表!userId=" + deposit.getUserId());
                    }

                    if (!maxUserOpenCycleId.containsKey(deposit.getUserId())) {
                        maxUserOpenCycleId.put(deposit.getUserId(),1);
                    }

                    //没有获取开张帐期，或者小于开账帐期
                    if (maxUserOpenCycleId.containsKey(deposit.getUserId())
                            && maxUserOpenCycleId.get(deposit.getUserId()) < deposit.getOpenCycleId()) {
                        userBalance.get(deposit.getUserId()).setOldOweFee(deposit.getOweFee());
                        maxUserOpenCycleId.put(deposit.getUserId(),deposit.getOpenCycleId());
                    }
                    userBalance.get(deposit.getUserId()).setHasPrivate('1');
                } else {
                    if (deposit.getOpenCycleId() > maxOpenCycleId) {
                        oldGeneralOweFee = deposit.getOweFee();
                        maxOpenCycleId = deposit.getOpenCycleId();
                    }
                }
            }
            for (Map.Entry<String, UserBalance> userBalanceEntry : userBalance.entrySet()) {
                UserBalance tmpUserBalance = userBalanceEntry.getValue();
                if ('1' == tmpUserBalance.getDefaultPay()) {
                    //有私有的
                    if ('1' == tmpUserBalance.getHasPrivate()) {
                        //原来有往月欠费,否则就是取本次余额
                        if (tmpUserBalance.getOldOweFee() > 0 ) {
                            //本次有余额
                            if (tmpUserBalance.getBalance() >0 ) {
                                tmpUserBalance.setBalance(-tmpUserBalance.getOldOweFee());
                            } else {
                                tmpUserBalance.setBalance(tmpUserBalance.getBalance() - tmpUserBalance.getOldOweFee());
                            }
                            tmpUserBalance.setOweFee(tmpUserBalance.getOldOweFee());
                        }
                    }
                } else {
                    //存在未销的往月欠费，并且有不可销往月账的账本的结余,结余必须是往月欠费
                    if (oldGeneralOweFee > 0 && tmpUserBalance.getBalance() > 0) {
                        tmpUserBalance.setBalance(-oldGeneralOweFee + acctNewNotCashFee);
                    }
                    else
                    {
                        //如果实时话费的结余小于等于0，下面等式满足；如果实时话费的结余大于0,往月欠费=0也满足
                        tmpUserBalance.setBalance(tmpUserBalance.getBalance() - oldGeneralOweFee);
                    }
                    tmpUserBalance.setOweFee(oldGeneralOweFee);
                }
            }
        }

        for (AccountDeposit accountDeposit : depositList) {
            //保存原来的往月欠费给库外信控使用
            accountDeposit.setOweFee(writeSnapLog.getAllNewBOweFee());
            //用户结余大于0，私有账本往月欠费为0
            if ('1' == accountDeposit.getPrivateTag() && userBalance.containsKey(accountDeposit.getUserId())
                    && userBalance.get(accountDeposit.getUserId()).getBalance() >= 0) {
                accountDeposit.setOweFee(0);
            }
        }

        //广东托收用户参与实时信控,往月欠费不计入用户结余
        CommPara consignCommPara = writeOffRuleInfo.getCommpara(ASM_CONSIGN_USERBALANCE);
        String payMode = "|" + account.getPayModeCode() + "|";
        if (consignCommPara != null && "1".equals(consignCommPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(consignCommPara.getParaCode2())
                && consignCommPara.getParaCode2().contains(payMode)) {
            for (Map.Entry<String, UserBalance> tmpUserBalance : userBalance.entrySet()) {
                tmpUserBalance.getValue().setBalance(tmpUserBalance.getValue().getBalance()
                        + tmpUserBalance.getValue().getNewBoweFee());
            }
        }

        //设置用户结余信息
        tradeCommInfo.setUserBalance(userBalance);
        tradeCommInfo.setUniBalanceInfo(uniBalanceInfo);
    }

    /**
     * 根据账单，账本和付费用户生成用户结余列表
     *
     * @param billList
     * @param depositList
     * @param userList
     * @return
     */
    private Map<String, UserBalance> genUserBalance(List<Bill> billList, List<AccountDeposit> depositList, List<User> userList) {
        Map<String, UserBalance> userBalance = new HashMap<>();
        //根据账单生成用户结余对象
        if (!CollectionUtils.isEmpty(billList)) {
            for (Bill pBill : billList) {
                if (pBill.getUserId().length() > 2
                        && !userBalance.containsKey(pBill.getUserId())) {
                    UserBalance tmp = new UserBalance();
                    userBalance.put(pBill.getUserId(), tmp);
                }
            }
        }

        //根据账本生成用户结余对象
        if (!CollectionUtils.isEmpty(depositList)) {
            for (AccountDeposit accountDeposit : depositList) {
                if ('1' == accountDeposit.getPrivateTag()
                        && accountDeposit.getUserId().length() > 2
                        && !userBalance.containsKey(accountDeposit.getUserId())) {
                    UserBalance tmp = new UserBalance();
                    userBalance.put(accountDeposit.getUserId(), tmp);
                }
            }
        }

        //根据默认付费用户生成用户结余对象
        if (!CollectionUtils.isEmpty(userList)) {
            for (User user : userList) {
                if (!userBalance.containsKey(user.getUserId())) {
                    UserBalance tmp = new UserBalance();
                    tmp.setDefaultPay('1');
                    userBalance.put(user.getUserId(), tmp);
                } else {
                    userBalance.get(user.getUserId()).setDefaultPay('1');
                }
            }
        }
        return userBalance;
    }


    private void updateWriteSnapAndUserBalaneInfo(List<Bill> billList, WriteSnapLog writeSnapLog, Map<String, UserBalance> userBalance, int curCycleId) {
        //缴费前账户欠费之和
        long oldOweFee = 0;
        //缴费后账户欠费之和
        long nowOweFee = 0;
        //设置用户欠费信息
        if (!CollectionUtils.isEmpty(billList)) {
            for (Bill pBill : billList) {
                //原始帐单欠费
                long oldBillBalance = billServiceImpl.getOldBillBalance(pBill);
                //销账后账单欠费
                long billBalance = billServiceImpl.getBillBalance(pBill);

                //设置销账快照表中欠费和实时费用值
                if ('2' == pBill.getCanpayTag()) {
                    if (pBill.getCycleId() == curCycleId) {
                        //当月实时账单
                        writeSnapLog.setCurRealFee(writeSnapLog.getCurRealFee() + oldBillBalance);
                    } else {
                        //往月实时账单
                        writeSnapLog.setPreRealFee(writeSnapLog.getPreRealFee() + oldBillBalance);
                    }
                } else {
                    //销账前欠费结余
                    writeSnapLog.setAllBOweFee(writeSnapLog.getAllBOweFee() + oldBillBalance);
                    //销账后欠费结余
                    writeSnapLog.setAllNewBOweFee(writeSnapLog.getAllNewBOweFee() + billBalance);
                }

                //冲抵前不算欠费帐目金额
                long oldNotOwe = 0;
                //冲抵后不算欠费帐目金额
                long nowNotOwe = 0;
                //不算欠费的账目项
                if (!WriteOffRuleStaticInfo.isOweItem(pBill.getIntegrateItemCode())) {
                    oldNotOwe = oldBillBalance - pBill.getImpFee();
                    nowNotOwe = billBalance;
                }

                if (userBalance.containsKey(pBill.getUserId())) {
                    UserBalance tmpUseBalance = userBalance.get(pBill.getUserId());
                    if ('1' == tmpUseBalance.getDefaultPay()) {
                        //设置剩余欠费
                        tmpUseBalance.setOweFee(tmpUseBalance.getOweFee() + billBalance - nowNotOwe);
                        //设置结余
                        tmpUseBalance.setBalance(tmpUseBalance.getBalance() - (billBalance - nowNotOwe));
                        //设置剩余往月欠费
                        if ('2' != pBill.getCanpayTag()) {
                            tmpUseBalance.setNewBoweFee(tmpUseBalance.getNewBoweFee() + billBalance - nowNotOwe);
                        }
                    }
                }
                oldOweFee += -(oldBillBalance - pBill.getImpFee() - oldNotOwe);
                nowOweFee += -(billBalance - nowNotOwe);
            }
        }

        //原实时结余
        if (oldOweFee < 0) {
            writeSnapLog.setAllBalance(oldOweFee);
        }

        //现实时结余
        if (nowOweFee < 0) {
            writeSnapLog.setAllNewBalance(nowOweFee);
        }
    }

    //清除数据
    private void regressData(TradeCommInfo tradeCommInfo) {
        //清除本次日志
        if (!CollectionUtils.isEmpty(tradeCommInfo.getWriteOffLogs())) {
            tradeCommInfo.getWriteOffLogs().clear();
        }

        if (!CollectionUtils.isEmpty(tradeCommInfo.getCurrLimitFeeDepositLog())) {
            tradeCommInfo.getCurrLimitFeeDepositLog().clear();
        }

        //生成的结余清零
        if (!CollectionUtils.isEmpty(tradeCommInfo.getUserBalance())) {
            tradeCommInfo.getUserBalance().clear();
        }
        depositServiceImpl.regressData(tradeCommInfo.getAccountDeposits());
        billServiceImpl.regressData(tradeCommInfo.getBills());
    }

    //更新滞纳金金额
    private void decLateFee(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }

        for (Bill pBill : bills) {
            //实时账单过滤
            if (pBill.getCanpayTag() != '2') {
                //本次实际产生的滞纳金
                pBill.setNewLateFee(pBill.getCurrWriteOffLate() + (pBill.getDerateFee() - pBill.getLateBalance()));
            }
        }
    }

    //获取滞纳金计算参数
    private String getLateCalBeginTime(int cycleId, LateCalPara rLateCalPara, int iniDays) {
        if (!WriteOffRuleStaticInfo.getAllMCycle().containsKey(cycleId)) {
            throw new SkyArkException("参数问题，请联系管理员!获取帐期失败!cycleId="+cycleId);
        }
        int calcIniDays = 0;
        if(iniDays < 0) {
            calcIniDays = rLateCalPara.getIniDays();
        }else{
            calcIniDays = rLateCalPara.getIniDays() > iniDays ? rLateCalPara.getIniDays() : iniDays;
        }
        return TimeUtil.dateAddDays(WriteOffRuleStaticInfo.getAllMCycle().get(cycleId).getCycEndTime(),(calcIniDays + 1));
    }

    private long derateLatefee(TradeCommInfo tradeCommInfo, String acctId, int cycleId, long lateFee) {
        //库外信控不做减免计算
        if(tradeCommInfo.isOuterCredit()) {
            return 0;
        }
        if (CollectionUtils.isEmpty(tradeCommInfo.getDerateLateFeeLogs())) {
            return 0;
        }
        long derateFee = 0;
        DerateLateFeeLog derateLog = getDerateLateFeeLog(
                tradeCommInfo.getDerateLateFeeLogs(), acctId, cycleId);
        if (derateLog == null) {
            return 0;
        }
        //按金额
        if (derateLog.getDerateRuleId() == 0) {
            if (derateLog.getDerateFee() - derateLog.getUsedDerateFee() > lateFee) {
                derateFee = lateFee;
            } else {
                derateFee = derateLog.getDerateFee() - derateLog.getUsedDerateFee();
            }
        } else if (derateLog.getDerateRuleId() == 1) {
            //按比例
            derateFee = lateFee * derateLog.getDerateFee() / 100;
            if (derateFee > lateFee) {
                derateFee = lateFee;
            }
        } else if (derateLog.getDerateRuleId() == 2) {
            //减免到
            if (lateFee + derateLog.getSumLateFee() > derateLog.getDerateFee()) {
                derateFee = lateFee + derateLog.getSumLateFee() - derateLog.getDerateFee() - derateLog.getUsedDerateFee();
            }
            derateLog.setSumLateFee(derateLog.getSumLateFee() + lateFee);
        }

        derateLog.setUsedDerateFee(derateLog.getUsedDerateFee() + derateFee);
        //表示已经使用
        derateLog.setUseTag('1');
        return derateFee;
    }

    private DerateLateFeeLog getDerateLateFeeLog(List<DerateLateFeeLog> derateLateFeeLogs, String acctId, int cycleId) {
        if (CollectionUtils.isEmpty(derateLateFeeLogs)) {
            return null;
        }
        for (DerateLateFeeLog derateLateFeeLog : derateLateFeeLogs) {
            if (acctId.equals(derateLateFeeLog.getAcctId()) && cycleId == derateLateFeeLog.getCycleId()) {
                return derateLateFeeLog;
            }
        }
        return null;
    }

    //生成存取款日志
    private void genAccessLog(TradeCommInfo tradeCommInfo) {
        Account account = tradeCommInfo.getAccount();
        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        List<AccessLog> accessLogList = new ArrayList<>();
        Map<String, Long> invoiceFee = tradeCommInfo.getInvoiceFeeMap();

        for (AccountDeposit deposit : depositList) {
            if (deposit.getRecvFee() != 0 || deposit.getIfInAccesslog() == '1') {
                AccessLog accessLog = new AccessLog();
                accessLog.setProvinceCode(account.getProvinceCode());
                accessLog.setEparchyCode(account.getEparchyCode());
                accessLog.setAcctId(deposit.getAcctId());
                accessLog.setAcctBalanceId(deposit.getAcctBalanceId());
                accessLog.setDepositCode(deposit.getDepositCode());
                accessLog.setOldBalance(deposit.getMoney());
                accessLog.setMoney(deposit.getRecvFee());
                accessLog.setNewBalance(accessLog.getOldBalance() + deposit.getRecvFee());
                accessLog.setAccessTag(deposit.getRecvFee() >= 0 ? '0' : '1');
                // 本次发票金额
                if (!CollectionUtils.isEmpty(invoiceFee) && invoiceFee.containsKey(accessLog.getAcctBalanceId())) {
                    accessLog.setInvoiceFee(invoiceFee.get(accessLog.getAcctBalanceId()));
                }
                accessLog.setCurTag('1'); // 本次缴费触发相关标志
                accessLog.setCancelTag('0');
                accessLogList.add(accessLog);
            }
        }

        for (AccountDeposit deposit : depositList) {
            if (deposit.getUseRecvFee() != 0 || deposit.getImpFee() != 0 || deposit.getImpRealFee() != 0) {
                //判断pAcctdeposit.impRealFee != 0的原因可能有往月负账单发生了销账，并且转换为预存冲抵了实时话费，需要转化为预存
                AccessLog accessLog = new AccessLog();
                accessLog.setProvinceCode(account.getProvinceCode());
                accessLog.setEparchyCode(account.getEparchyCode());
                accessLog.setAcctId(deposit.getAcctId());
                accessLog.setAcctBalanceId(deposit.getAcctBalanceId());
                accessLog.setDepositCode(deposit.getDepositCode());
                accessLog.setOldBalance(deposit.getMoney() + deposit.getRecvFee());
                long tmpMoney = deposit.getUseRecvFee() + deposit.getImpFee() - deposit.getImpRealFee();
                accessLog.setMoney(-tmpMoney);
                accessLog.setNewBalance(accessLog.getOldBalance() - tmpMoney);
                //帐本销账
                accessLog.setAccessTag('2');
                //销帐标志
                accessLog.setCurTag('0');
                accessLog.setCancelTag('0');
                if (accessLog.getMoney() != 0) {
                    accessLogList.add(accessLog);
                }
            }
        }

        List<WriteOffLog> writeOffLogs = tradeCommInfo.getWriteOffLogs();
        if (accessLogList.isEmpty() && !CollectionUtils.isEmpty(writeOffLogs)) {
            //0帐单参与了销帐,需要补
            for (int i = 0; i < writeOffLogs.size(); ++i) {
                int k = 0;
                for (; k < accessLogList.size(); k++) {
                    if (writeOffLogs.get(i).getAcctBalanceId().equals(accessLogList.get(k).getAcctBalanceId())) {
                        break;
                    }
                }

                if (k == accessLogList.size()) {
                    AccessLog accessLog = new AccessLog();
                    accessLog.setProvinceCode(account.getProvinceCode());
                    accessLog.setEparchyCode(account.getEparchyCode());

                    int j = 0;
                    for (; j < depositList.size(); ++j) {
                        if (writeOffLogs.get(i).getAcctBalanceId().equals(depositList.get(j).getAcctBalanceId())) {
                            break;
                        }
                    }

                    if (j == depositList.size()) {
                        throw new SkyArkException("销0帐单发生错误!acctBalanceId=" + writeOffLogs.get(i).getAcctBalanceId());
                    }
                    accessLog.setAcctId(depositList.get(j).getAcctId());
                    accessLog.setAcctBalanceId(depositList.get(j).getAcctBalanceId());
                    accessLog.setDepositCode(depositList.get(j).getDepositCode());
                    accessLog.setOldBalance(depositList.get(j).getMoney());
                    accessLog.setMoney(0);
                    accessLog.setNewBalance(accessLog.getOldBalance());
                    accessLog.setAccessTag('2');
                    // 销帐标志
                    accessLog.setCurTag('0');
                    accessLog.setCancelTag('0');
                    accessLogList.add(accessLog);
                }
            }
        }
        tradeCommInfo.setAccesslogs(accessLogList);
    }
}

package com.unicom.acting.fee.calc.service.impl;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.acting.fee.calc.domain.AcctBalanceRelSortRule;
import com.unicom.acting.fee.calc.domain.AcctDepositDefaultSortRule;
import com.unicom.acting.fee.calc.domain.AcctDepositPrivateFirstSortRule;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.DepositService;
import com.unicom.acting.fee.domain.CommPara;
import com.unicom.acting.fee.domain.DepositPriorRule;
import com.unicom.acting.fee.domain.LimitFeeDepositLog;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctBalanceRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 账本相关操作公共组件
 *
 * @author Wangkh
 */
@Service
public class DepositServiceImpl implements DepositService {
    private static final Logger logger = LoggerFactory.getLogger(DepositServiceImpl.class);

    //生成虚拟账本
    @Override
    public void genVirtualAcctBalanceId(TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getAccountDeposits())
                || CollectionUtils.isEmpty(tradeCommInfo.getAcctBalanceRels())) {
            return;
        }

        List<AccountDeposit> acctDepositList = tradeCommInfo.getAccountDeposits();
        List<AcctBalanceRel> acctBalanceRelList = tradeCommInfo.getAcctBalanceRels();
        //虚拟关系
        Map<String, Map<String, Long>> virtualRelMap = new TreeMap<>();
        if (!CollectionUtils.isEmpty(acctBalanceRelList)) {
            //按比例销账关系规则排序
            acctBalanceRelList.sort(new AcctBalanceRelSortRule());
            String cmpAcctBalanceId = acctBalanceRelList.get(0).getAcctBalanceId();
            //账本对应的销账分摊比例
            Map<String, Long> rateBalanceIdMap = new TreeMap<>();
            long money = 0;
            long recvFee = 0;
            int totalRate = 0;

            int ind = 1;
            for (AcctBalanceRel acctBalanceRel : acctBalanceRelList) {
                if (!cmpAcctBalanceId.equals(acctBalanceRel.getAcctBalanceId())) {
                    if (100 - totalRate < 0) {
                        throw new SkyArkException("acctBalanceId=" + cmpAcctBalanceId + "关联帐本按比例销帐，比例不合法!");
                    }

                    //如果没有acctBalanceId2账本的比例关系，不再生成虚拟账本
                    if (isDepositExist(acctDepositList, cmpAcctBalanceId) && !CollectionUtils.isEmpty(rateBalanceIdMap)) {
                        //记录需要更账本和比例关系
                        Map<String, Long> rateBalanceIdMapTmp = new TreeMap<>();
                        for (String acctBalanceId : rateBalanceIdMap.keySet()) {
                            rateBalanceIdMapTmp.put(acctBalanceId, rateBalanceIdMap.get(acctBalanceId));
                        }
                        rateBalanceIdMapTmp.put(cmpAcctBalanceId, (long) (100 - totalRate));

                        AccountDeposit mainDeposit = getAcctDepositByAcctBalanceId(acctDepositList, cmpAcctBalanceId);
                        AccountDeposit deposit = mainDeposit.clone();
                        mainDeposit.setVirtualTag('2');
                        //虚拟账本实例
                        String virtualAcctBalanceId = VIRTUAL_PREFIX + (ind++);
                        virtualRelMap.put(virtualAcctBalanceId, rateBalanceIdMapTmp);
                        deposit.setAcctBalanceId(virtualAcctBalanceId);
                        deposit.setMoney(deposit.getMoney() + money);
                        deposit.setRecvFee(deposit.getRecvFee() + recvFee);
                        deposit.setVirtualTag('1');
                        deposit.setLimitMode('0');
                        deposit.setLimitMoney(-1);
                        updateAcctDeposit(acctDepositList, deposit);
                    }

                    rateBalanceIdMap.clear();
                    money = 0;
                    recvFee = 0;
                    totalRate = 0;
                }

                //账本不存在不再生成虚拟账本
                if (!isDepositExist(acctDepositList, acctBalanceRel.getAcctBalanceId2())) {
                    continue;
                }

                rateBalanceIdMap.put(acctBalanceRel.getAcctBalanceId2(), (long) acctBalanceRel.getRate());
                totalRate += acctBalanceRel.getRate();
                AccountDeposit refDeposit = getAcctDepositByAcctBalanceId(acctDepositList, acctBalanceRel.getAcctBalanceId2());
                money += refDeposit.getMoney();
                recvFee += refDeposit.getRecvFee();
                if (refDeposit.getDepositCode() == 28 || refDeposit.getDepositCode() == 25) {
                    refDeposit.setVirtualTag('0');
                } else {
                    //为‘2’的账本计算的时候不处理
                    refDeposit.setVirtualTag('2');
                }
                cmpAcctBalanceId = acctBalanceRel.getAcctBalanceId();
            }

            if (rateBalanceIdMap.size() > 0) {
                if (100 - totalRate < 0) {
                    throw new SkyArkException("关联帐本按比例销帐，比例不合法!,ACCT_BALANCE_ID=" + cmpAcctBalanceId);
                }

                //账本不存在不再生成虚拟账本
                if (!isDepositExist(acctDepositList, cmpAcctBalanceId)) {
                    return;
                }

                rateBalanceIdMap.put(cmpAcctBalanceId, (long) (100 - totalRate));
                AccountDeposit mainDeposit = getAcctDepositByAcctBalanceId(acctDepositList, cmpAcctBalanceId);
                AccountDeposit deposit = mainDeposit.clone();
                mainDeposit.setVirtualTag('2');

                String virtualAcctBalanceId = VIRTUAL_PREFIX + ind;
                virtualRelMap.put(virtualAcctBalanceId, rateBalanceIdMap);
                deposit.setAcctBalanceId(virtualAcctBalanceId);
                deposit.setMoney(deposit.getMoney() + money );
                deposit.setRecvFee(deposit.getRecvFee() + recvFee);
                deposit.setVirtualTag('1');
                deposit.setLimitMode('0');
                deposit.setLimitMoney(-1);
                updateAcctDeposit(acctDepositList, deposit);
            }
            tradeCommInfo.setVirtualRel(virtualRelMap);
        }
    }

    //删除虚拟账本
    @Override
    public void delVirtualAcctBalanceId(List<AccountDeposit> accountDeposits, Map<String, Map<String, Long>> virtualRel) {
        if (CollectionUtils.isEmpty(virtualRel) || CollectionUtils.isEmpty(accountDeposits)) {
            return;
        }

        for (String virtualAcctBalanceId : virtualRel.keySet()) {
            for (Iterator<AccountDeposit> itr = accountDeposits.iterator(); itr.hasNext(); ) {
                AccountDeposit accountDeposit = itr.next();
                if (virtualAcctBalanceId.equals(accountDeposit.getAcctBalanceId())) {
                    accountDeposits.remove(accountDeposit);
                    break;
                }
            }
        }
    }

    //获取账本列表中该账本类型编码的所有账本列表
    @Override
    public List<AccountDeposit> getAcctDepositsByDepositCode(List<AccountDeposit> accountDeposits, int depositCode) {
        if (!CollectionUtils.isEmpty(accountDeposits)) {
            List<AccountDeposit> list = new ArrayList<>();
            for (AccountDeposit pAcctDeposit : accountDeposits) {
                if (depositCode == pAcctDeposit.getDepositCode()) {
                    list.add(pAcctDeposit);
                }
            }
            return list;
        }
        //返回empty
        return new ArrayList<>();
    }

    //获取账本列表中该账本类型编码的所有账本列表
    @Override
    public AccountDeposit getAcctDepositByDepositCode(List<AccountDeposit> accountDeposits, int depositCode) {
        if (!CollectionUtils.isEmpty(accountDeposits)) {
            for (AccountDeposit pAcctDeposit : accountDeposits) {
                if (depositCode == pAcctDeposit.getDepositCode()) {
                    return pAcctDeposit;
                }
            }
        }
        return null;
    }

    //获取账本列表中该账本实例标识的所有账本列表isDepositExist
    @Override
    public AccountDeposit getAcctDepositByAcctBalanceId(List<AccountDeposit> accountDeposits, String acctBalanceId) {
        if (!CollectionUtils.isEmpty(accountDeposits)) {
            int i = 0;
            for (; i < accountDeposits.size(); ++i) {
                if (accountDeposits.get(i).getAcctBalanceId().equals(acctBalanceId)) {
                    break;
                }
            }

            if (i == accountDeposits.size()) {
                throw new SkyArkException("帐本不存在,ACCT_BALANCE_ID=" + acctBalanceId);
            }
            return accountDeposits.get(i);
        }
        return null;
    }

    //更新账本列表  不直接使用LIST
    @Override
    public void updateAcctDeposit(List<AccountDeposit> accountDepositList, AccountDeposit deposit) {
        if (accountDepositList == null) {
            throw new SkyArkException("要更新的账本列表对象不能为空");
        }
        int i = 0;
        for (; i < accountDepositList.size(); ++i) {
            if (accountDepositList.get(i).getAcctBalanceId().equals(deposit.getAcctBalanceId())) {
                break;
            }
        }
        if (i == accountDepositList.size()) {
            accountDepositList.add(deposit);
        } else {
            accountDepositList.remove(i);
            accountDepositList.add(i, deposit);
        }
    }

    //校验账本是否存在
    @Override
    public boolean isDepositExist(List<AccountDeposit> accountDeposits, String acctBalanceId) {
        if (!CollectionUtils.isEmpty(accountDeposits)) {
            for (AccountDeposit accountDeposit : accountDeposits) {
                if (acctBalanceId.equals(accountDeposit.getAcctBalanceId())) {
                    return true;
                }
            }
        }
        return false;
    }

    //账本排序
    @Override
    public void accountDepositSort(List<AccountDeposit> accountDeposits, WriteOffRuleInfo writeOffRuleInfo) {
        if (writeOffRuleInfo == null || CollectionUtils.isEmpty(accountDeposits)) {
            return;
        }

        //账本优先级属性
        Map<Integer, DepositPriorRule> depositPriorRule = writeOffRuleInfo.getDepositPriorRuleMap();

        //更新账本列表的账本属性
        for (AccountDeposit accountDeposit : accountDeposits) {
            if (depositPriorRule.containsKey(accountDeposit.getDepositCode())) {
                DepositPriorRule pDepositPriorRule = depositPriorRule.get(accountDeposit.getDepositCode());
                //账本类型
                accountDeposit.setDepositTypeCode(pDepositPriorRule.getDepositTypeCode());
                //账本销账优先级
                accountDeposit.setPriority(pDepositPriorRule.getDepositPriority());
                //信控标志
                accountDeposit.setIfBalance(pDepositPriorRule.getIfBalance());
                //是否可以冲抵往月话费
                accountDeposit.setIfCalcOwe(pDepositPriorRule.getIfCalcOwe());
                //信控模式
                accountDeposit.setCreditMode(pDepositPriorRule.getCreditMode());
                //帐单项优先级标识
                accountDeposit.setItemPriorRuleId(-1);
                //帐本科目类型优先
                accountDeposit.setDepositTypePriority(pDepositPriorRule.getPriority());
            }
        }

        //账本列表排序
        CommPara rPCommPara = writeOffRuleInfo.getCommpara(ASM_DEPOSIT_PRIVATE_PRIORITY);
        if (rPCommPara != null && "1".equals(rPCommPara.getParaCode1())) {
            accountDeposits.sort(new AcctDepositPrivateFirstSortRule());
        } else {
            accountDeposits.sort(new AcctDepositDefaultSortRule());
        }
    }

    //获取账本剩余金额
    @Override
    public long getRemainMoney(AccountDeposit deposit) {
        return (deposit.getMoney() + deposit.getRecvFee() - deposit.getImpFee()
                - deposit.getUseRecvFee() - deposit.getFreezeFee());
    }

    /**
     * 获取账本奇偶月金额总和
     *
     * @param deposit
     * @return
     */
    @Override
    public long getOddEvenMoney(AccountDeposit deposit) {
        return deposit.getMoney();
    }

    /**
     * 销往月欠费后剩余金额，没有扣减账本冻结金额
     *
     * @param deposit
     * @return
     */
    @Override
    public long getNewDepositMoney(AccountDeposit deposit) {
        return deposit.getMoney() + deposit.getRecvFee()
                - deposit.getImpFee() - deposit.getUseRecvFee() + deposit.getImpRealFee();
    }

    //更新账本列表并排序
    @Override
    public void accountDepositUpAndSort(WriteOffRuleInfo writeOffRuleInfo, List<AccountDeposit> lists, AccountDeposit deposit) {
        updateAcctDeposit(lists, deposit);
        accountDepositSort(lists, writeOffRuleInfo);
    }

    //某类型账本是否可销某账目项销欠费
    @Override
    public boolean canPay(Map<Integer, Set<Integer>> depositItemLimitMap, int depositCode, int itemCode) {
        if (!CollectionUtils.isEmpty(depositItemLimitMap) && depositItemLimitMap.containsKey(depositCode)) {
            return depositItemLimitMap.get(depositCode).contains(itemCode);
        }
        return true;
    }

    //非个性化账本在某个账期剩余可用金额
    @Override
    public long getCanUseMoney(TradeCommInfo tradeCommInfo, AccountDeposit deposit, String userId, int cycleId) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getCurrLimitFeeDepositLog())) {
            tradeCommInfo.setCurrLimitFeeDepositLog(new ArrayList<>());
        }
        List<LimitFeeDepositLog> currLimitFeeDepositLog = tradeCommInfo.getCurrLimitFeeDepositLog();

        //账本限制金额
        long limitMoney = deposit.getLimitMoney();
        //账本无限定金额
        if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
            limitMoney = MAX_LIMIT_FEE;
        }

        //获取账本当前可用金额(扣减欠费和实时话费)
        long remainMoney = getRemainMoney(deposit);
        long tmpCanUse = 0;

        //没有余额共享规则(有两种情况 1：帐户级 2：用户级)
        boolean acctShareDeposit = false;
        if ('1' != deposit.getPrivateTag()
                || '1' == deposit.getPrivateTag() && userId.equals(deposit.getUserId())) {
            if ('1' != deposit.getPrivateTag()) {
                acctShareDeposit = true;
            }
            if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
                tmpCanUse = limitMoney;
            } else if ('1' == deposit.getLimitMode() || 'c' == deposit.getLimitMode() || 'd' == deposit.getLimitMode()) {
                //限金额
                if (acctShareDeposit) {
                    tmpCanUse = limitMoney - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), cycleId);
                } else {
                    tmpCanUse = limitMoney - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), userId, cycleId);
                }
            } else if ('2' == deposit.getLimitMode() || '3' == deposit.getLimitMode()) {
                //比例和最低消费额度达到门槛值才能销帐，直接返回最大限额(销帐完成后再处理)
                tmpCanUse = MAX_LIMIT_FEE;
            } else if ('b' == deposit.getLimitMode()) {
                //固定月限额可以结存上月使用
                if (acctShareDeposit) {
                    tmpCanUse = limitMoney + deposit.getLimitLeft() - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), cycleId);
                } else {
                    tmpCanUse = limitMoney + deposit.getLimitLeft() - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), userId, cycleId);
                }
            } else {
                throw new SkyArkException("不正确的限定方式!limitMode=" + deposit.getLimitMode());
            }
        }

        remainMoney = (remainMoney <= tmpCanUse ? remainMoney : tmpCanUse);
        return remainMoney;
    }

    /**
     * 非个性化账本在某个账期剩余可用金额
     *
     * @param tradeCommInfo
     * @param deposit
     * @param userId
     * @param cycleId
     * @return
     */
    @Override
    public long getSimpleCanUseMoney(TradeCommInfo tradeCommInfo, AccountDeposit deposit, String userId, int cycleId) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getCurrLimitFeeDepositLog())) {
            tradeCommInfo.setCurrLimitFeeDepositLog(new ArrayList<>());
        }

        List<LimitFeeDepositLog> currLimitFeeDepositLog = tradeCommInfo.getCurrLimitFeeDepositLog();

        //账本限制金额
        long limitMoney = deposit.getLimitMoney();
        //账本无限定金额
        if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
            limitMoney = MAX_LIMIT_FEE;
        }

        //获取账本剩余金额
        long remainMoney = getRemainMoney(deposit);
        long tmpCanUse = 0;

        //没有余额共享规则(有两种情况 1：帐户级 2：用户级) 修改一下名称
        if ('1' != deposit.getPrivateTag() || ('1' == deposit.getPrivateTag() && userId.equals(deposit.getUserId()))) {
            if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
                tmpCanUse = limitMoney;
            } else if ('1' == deposit.getLimitMode()) {
                //固定限额
                if ('1' != deposit.getPrivateTag()) {
                    tmpCanUse = limitMoney - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), cycleId);
                } else {
                    tmpCanUse = limitMoney - getUsedMoney(currLimitFeeDepositLog, deposit.getAcctBalanceId(), userId, cycleId);
                }
            } else {
                throw new SkyArkException("不正确的限定方式!limitMode=" + deposit.getLimitMode());
            }
        }
        return (remainMoney <= tmpCanUse ? remainMoney : tmpCanUse);
    }

    /**
     * 获取限额表中账本某账期某用户已使用销账金额
     *
     * @param currLimitFeeDepositLogList
     * @param acctBalanceId
     * @param userId
     * @param cycleId
     * @return
     */
    @Override
    public long getUsedMoney(List<LimitFeeDepositLog> currLimitFeeDepositLogList, String acctBalanceId, String userId, int cycleId) {
        if (CollectionUtils.isEmpty(currLimitFeeDepositLogList)) {
            return 0;
        }

        //账本销账已用额度
        long usedMoney = 0;
        if (cycleId > 0) {
            for (LimitFeeDepositLog currLimitFeeDepositLog : currLimitFeeDepositLogList) {
                if (acctBalanceId.equals(currLimitFeeDepositLog.getAcctBalanceId())
                        && cycleId == currLimitFeeDepositLog.getCycleId()
                        && userId.equals(currLimitFeeDepositLog.getUserId())) {
                    usedMoney += currLimitFeeDepositLog.getUsedMoney();
                }
            }
        } else {
            for (LimitFeeDepositLog currLimitFeeDepositLog : currLimitFeeDepositLogList) {
                if (acctBalanceId.equals(currLimitFeeDepositLog.getAcctBalanceId())
                        && userId.equals(currLimitFeeDepositLog.getUserId())) {
                    usedMoney += currLimitFeeDepositLog.getUsedMoney();
                }
            }
        }
        return usedMoney;
    }

    //获取限额表中账本某账期已使用销账金额
    @Override
    public long getUsedMoney(List<LimitFeeDepositLog> currLimitFeeDepositLogs, String acctBalanceId, int cycleId) {
        if (CollectionUtils.isEmpty(currLimitFeeDepositLogs)) {
            return 0;
        }

        //账本销账已用额度
        long usedMoney = 0;
        if (cycleId > 0) {
            for (LimitFeeDepositLog currLimitFeeDepositLog : currLimitFeeDepositLogs) {
                if (acctBalanceId.equals(currLimitFeeDepositLog.getAcctBalanceId())
                        && cycleId == currLimitFeeDepositLog.getCycleId()) {
                    usedMoney += currLimitFeeDepositLog.getUsedMoney();
                }
            }
        } else {
            for (LimitFeeDepositLog currLimitFeeDepositLog : currLimitFeeDepositLogs) {
                if (acctBalanceId.equals(currLimitFeeDepositLog.getAcctBalanceId())) {
                    usedMoney += currLimitFeeDepositLog.getUsedMoney();
                }
            }
        }
        return usedMoney;
    }

    //更新非个性化限额账本使用预存款金额销账信息
    @Override
    public void useLimitFeeDeposit(List<LimitFeeDepositLog> currLimitFeeDepositLogs, AccountDeposit deposit, String userId, int cycleId, long money, long impMoney) {
        if (currLimitFeeDepositLogs == null) {
            throw new SkyArkException("currLimitFeeDepositLogs没有创建");
        }

        int i = 0;
        for (; i < currLimitFeeDepositLogs.size(); ++i) {
            if (currLimitFeeDepositLogs.get(i).getAcctBalanceId().equals(deposit.getAcctBalanceId())
                    && currLimitFeeDepositLogs.get(i).getCycleId() == cycleId
                    && currLimitFeeDepositLogs.get(i).getUserId().equals(deposit.getUserId())) {
                break;
            }
        }

        if (i != currLimitFeeDepositLogs.size()) {
            currLimitFeeDepositLogs.get(i).setUsedMoney(currLimitFeeDepositLogs.get(i).getUsedMoney() + money);
            currLimitFeeDepositLogs.get(i).setImpMoney(currLimitFeeDepositLogs.get(i).getImpMoney() + impMoney);
        } else {
            LimitFeeDepositLog tmp = new LimitFeeDepositLog();
            tmp.setAcctBalanceId(deposit.getAcctBalanceId());
            tmp.setAcctId(deposit.getAcctId());
            tmp.setUserId(userId);
            tmp.setCycleId(cycleId);
            tmp.setUsedMoney(money);
            tmp.setImpMoney(impMoney);
            tmp.setEparchyCode(deposit.getEparchyCode());
            currLimitFeeDepositLogs.add(tmp);
        }
    }

    /**
     * 获取账本剩余可用余额
     *
     * @param tradeCommInfo
     * @param deposit
     * @param userId
     * @param maxAcctCycleId
     * @param curCycleId
     * @return
     */
    @Override
    public long getSimpleLeftMoney(TradeCommInfo tradeCommInfo, AccountDeposit deposit, String userId, int maxAcctCycleId, int curCycleId) {
        //账本限定金额
        long limitMoney = deposit.getLimitMoney();
        //账本无限定
        if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
            limitMoney = MAX_LIMIT_FEE;
        }
        long remainMoney = getRemainMoney(deposit);
        long tmpCanUse = 0;
        List<LimitFeeDepositLog> currLimitFeeDepositLogs = tradeCommInfo.getCurrLimitFeeDepositLog();
        if (CollectionUtils.isEmpty(currLimitFeeDepositLogs)) {
            currLimitFeeDepositLogs = new ArrayList<>();
        }
        //没有余额共享规则(有两种情况 1：帐户级 2：用户级)
        boolean acctShareFlag = false;
        if ('1' != deposit.getPrivateTag() || '1' == deposit.getPrivateTag() && userId.equals(deposit.getUserId())) {
            if ('1' != deposit.getPrivateTag()) {
                acctShareFlag = true;
            }
            if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
                tmpCanUse = limitMoney;
            } else if ('1' == deposit.getLimitMode()) {
                //固定限额
                for (int tmpCycle = TimeUtil.genCycle(maxAcctCycleId, 1); tmpCycle <= curCycleId; ) {
                    if (deposit.getStartCycleId() <= tmpCycle && tmpCycle <= deposit.getEndCycleId()) {
                        if (acctShareFlag) {
                            //公有账本剩余可用额度
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), tmpCycle);
                        } else {
                            //私有账本剩余可用额度
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), userId, tmpCycle);
                        }
                    }
                    tmpCycle = TimeUtil.genCycle(tmpCycle, 1);
                }
            } else {
                throw new SkyArkException("不正确的限定方式! limitMode= " + deposit.getLimitMode());
            }
        }
        remainMoney = (remainMoney <= tmpCanUse ? remainMoney : tmpCanUse);
        return remainMoney;
    }

    /**
     * 获取账本剩余可用余额
     *
     * @param tradeCommInfo
     * @param deposit
     * @param userId
     * @param maxAcctCycleId
     * @param curCycleId
     * @return
     */
    @Override
    public long getLeftMoney(TradeCommInfo tradeCommInfo, AccountDeposit deposit, String userId, int maxAcctCycleId, int curCycleId) {
        //账本限定金额
        long limitMoney = deposit.getLimitMoney();
        //账本无限定
        if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
            limitMoney = MAX_LIMIT_FEE;
        }
        //账本剩余金额
        long remainMoney = getRemainMoney(deposit);
        //账本当前可用金额
        long tmpCanUse = 0;
        List<LimitFeeDepositLog> currLimitFeeDepositLogs = tradeCommInfo.getCurrLimitFeeDepositLog();
        if (CollectionUtils.isEmpty(currLimitFeeDepositLogs)) {
            currLimitFeeDepositLogs = new ArrayList<>();
        }
        //没有余额共享规则(有两种情况 1：帐户级 2：用户级)
        boolean acctShareFlag = false;
        if ('1' != deposit.getPrivateTag() || '1' == deposit.getPrivateTag() && userId.equals(deposit.getUserId())) {
            if ('1' != deposit.getPrivateTag()) {
                acctShareFlag = true;
            }
            if ('0' == deposit.getLimitMode() || 'A' == deposit.getLimitMode()) {
                tmpCanUse = limitMoney;
            } else if ('1' == deposit.getLimitMode() || 'c' == deposit.getLimitMode() || 'd' == deposit.getLimitMode()) {
                //限金额
                int tmpCycle = TimeUtil.genCycle(maxAcctCycleId, 1);
                while (tmpCycle <= curCycleId) {
                    if (deposit.getStartCycleId() <= tmpCycle && tmpCycle <= deposit.getEndCycleId()) {
                        if (acctShareFlag) {
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), tmpCycle);
                        } else {
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), userId, tmpCycle);
                        }
                    }
                    tmpCycle = TimeUtil.genCycle(tmpCycle, 1);
                }
            } else if ('2' == deposit.getLimitMode() || '3' == deposit.getLimitMode()) {
                //比例和最低消费额度达到门槛值才能销帐，直接返回最大限额(销帐完成后再处理)
                tmpCanUse = MAX_LIMIT_FEE;
            } else if ('b' == deposit.getLimitMode()) {
                //固定金额可结转使用
                int tmpCycle = TimeUtil.genCycle(maxAcctCycleId, 1);
                while (tmpCycle <= curCycleId) {
                    if (deposit.getStartCycleId() <= tmpCycle && tmpCycle <= deposit.getEndCycleId()) {
                        if (acctShareFlag) {
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), tmpCycle);
                        } else {
                            tmpCanUse += limitMoney - getUsedMoney(currLimitFeeDepositLogs, deposit.getAcctBalanceId(), userId, tmpCycle);
                        }
                    }
                    tmpCycle = TimeUtil.genCycle(tmpCycle, 1);
                }
                tmpCanUse += deposit.getLimitLeft();
            } else {
                throw new SkyArkException("不正确的限定方式! limitMode= " + deposit.getLimitMode());
            }
        }
        remainMoney = (remainMoney <= tmpCanUse ? remainMoney : tmpCanUse);
        return remainMoney;
    }

    /**
     * 还原帐本为销账前状态
     *
     * @param depositList
     */
    @Override
    public void regressData(List<AccountDeposit> depositList) {
        if (CollectionUtils.isEmpty(depositList)) {
            return;
        }

        for (AccountDeposit deposit : depositList) {
            deposit.setImpFee(0);
            deposit.setUseRecvFee(0);
            deposit.setImpRealFee(0);
            //剩余可使用存折
            deposit.setLeftCanUse(0);
            deposit.setRealFeeRecv(0);
            deposit.setVirtualTag('0');
            //账本销往月账标识
            deposit.setWriteOffOweFee(false);
        }
    }


}

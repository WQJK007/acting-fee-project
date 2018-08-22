package com.unicom.acting.fee.calc.service;

import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.LimitFeeDepositLog;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DepositCalcService extends IBaseService {
    //生成虚拟账本
    void genVirtualAcctBalanceId(TradeCommInfo tradeCommInfo);

    //删除虚拟账本
    void delVirtualAcctBalanceId(List<FeeAccountDeposit> actsDeposits, Map<String, Map<String, Long>> virtualRel);

    //获取账本列表中该账本类型编码的所有账本列表
    List<FeeAccountDeposit> getAcctDepositsByDepositCode(List<FeeAccountDeposit> actsDeposits, int depositCode);

    //获取账本列表中该账本类型编码的所有账本列表
    FeeAccountDeposit getAcctDepositByDepositCode(List<FeeAccountDeposit> actsDeposits, int depositCode);

    //获取账本列表中该账本实例标识的所有账本列表isDepositExist
    FeeAccountDeposit getAcctDepositByAcctBalanceId(List<FeeAccountDeposit> actsDeposits, String acctBalanceId);

    //更新账本列表  不直接使用LIST
    void updateAcctDeposit(List<FeeAccountDeposit> actsDepositList, FeeAccountDeposit deposit);

    //校验账本是否存在
    boolean isDepositExist(List<FeeAccountDeposit> actsDeposits, String acctBalanceId);

    //账本排序
    void accountDepositSort(List<FeeAccountDeposit> actsDeposits, WriteOffRuleInfo writeOffRuleInfo);

    //获取账本剩余金额
    long getRemainMoney(FeeAccountDeposit deposit);

    /**
     * 获取账本奇偶月金额总和
     *
     * @param deposit
     * @return
     */
    long getOddEvenMoney(FeeAccountDeposit deposit);

    /**
     * 销往月欠费后剩余金额，没有扣减账本冻结金额
     *
     * @param deposit
     * @return
     */
    long getNewDepositMoney(FeeAccountDeposit deposit);

    //更新账本列表并排序
    void accountDepositUpAndSort(WriteOffRuleInfo writeOffRuleInfo, List<FeeAccountDeposit> lists, FeeAccountDeposit deposit);

    //某类型账本是否可销某账目项销欠费
    boolean canPay(Map<Integer, Set<Integer>> depositItemLimitMap, int depositCode, int itemCode);

    //非个性化账本在某个账期剩余可用金额
    long getCanUseMoney(TradeCommInfo tradeCommInfo, FeeAccountDeposit deposit, String userId, int cycleId);

    /**
     * 非个性化账本在某个账期剩余可用金额
     *
     * @param tradeCommInfo
     * @param deposit
     * @param userId
     * @param cycleId
     * @return
     */
    long getSimpleCanUseMoney(TradeCommInfo tradeCommInfo, FeeAccountDeposit deposit, String userId, int cycleId);

    /**
     * 获取限额表中账本某账期某用户已使用销账金额
     *
     * @param currLimitFeeDepositLogList
     * @param acctBalanceId
     * @param userId
     * @param cycleId
     * @return
     */
    long getUsedMoney(List<LimitFeeDepositLog> currLimitFeeDepositLogList, String acctBalanceId, String userId, int cycleId);

    //获取限额表中账本某账期已使用销账金额
    long getUsedMoney(List<LimitFeeDepositLog> currLimitFeeDepositLogs, String acctBalanceId, int cycleId);

    //更新非个性化限额账本使用预存款金额销账信息
    void useLimitFeeDeposit(List<LimitFeeDepositLog> currLimitFeeDepositLogs, FeeAccountDeposit deposit, String userId, int cycleId, long money, long impMoney);

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
    long getSimpleLeftMoney(TradeCommInfo tradeCommInfo, FeeAccountDeposit deposit, String userId, int maxAcctCycleId, int curCycleId);

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
    long getLeftMoney(TradeCommInfo tradeCommInfo, FeeAccountDeposit deposit, String userId, int maxAcctCycleId, int curCycleId);

    /**
     * 还原帐本为销账前状态
     *
     * @param depositList
     */
    void regressData(List<FeeAccountDeposit> depositList);
}

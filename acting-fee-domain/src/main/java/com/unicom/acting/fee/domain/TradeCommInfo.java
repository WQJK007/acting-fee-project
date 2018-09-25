package com.unicom.acting.fee.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
/**
 * 账务交易常用对象
 *
 * @author Wangkh
 */
public class TradeCommInfo {
    /**
     * 触发用户(如果输入号码缴费，就是该用户信息)
     */
    private User mainUser;
    /**
     * 本次交易付费用户列表
     */
    private List<User> payUsers;
    /**
     * 账户所有付费用户列表,欠费查询返回账本信息使用
     */
    private List<User> allPayUsers;
    /**
     * 付费账户
     */
    private Account account;
//    private FeeAccount feeAccount;
    /**
     * 账户账本
     */
    private List<FeeAccountDeposit> feeAccountDeposits;
    /**
     * 账本比例关系
     */
    private List<FeeAcctBalanceRel> feeAcctBalanceRels;
    /**
     * 账户账单
     */
    private List<FeeBill> feeBills;
    /**
     * 账户自定义缴费期
     */
    private FeeAcctPaymentCycle feeAcctPaymentCycle;
    /**
     * 负账单转换目的账本 移到calc组件中做判断   dealTag
     */
    private int negativeBillDeposit;
    /**
     * 交费日志对象  dealTag 移走
     */
    private FeePayLog feePayLog;
    /**
     * 存取款日志对象 dealTag 移走
     */
    private List<FeeAccessLog> accesslogs;
    /**
     * 销账日志对象
     */
    private List<FeeWriteOffLog> feeWriteOffLogs;
    /**
     * 销账快照对象
     */
    private FeeWriteSnapLog feeWriteSnapLog;
    /**
     * 用户结余信息
     * 合帐用户，对于有用户级余额共享的情况同一个帐户有多个结余
     */
    private Map<String, UserBalance> userBalance;
    /**
     *销账规则
     * 根据省份编码，地市编码，和交易账户网别获取具体的销账规则
     */
    private WriteOffRuleInfo writeOffRuleInfo;
    /**
     * 选择帐目销帐  dealTag 移走
     */
    private Set<Integer> chooseItem;
    /**
     * 选择帐期销帐  dealTag 移走
     */
    private Set<Integer> chooseCycleId;
    /**
     * 选择用户销帐  dealTag 移走
     */
    private Set<String> chooseUserId;
    /**
     * @see #depositItemLimitPtr 帐本限定列表<账本类型, <可销账目项列表>>
     */
    private Map<Integer, Set<Integer>> depositItemLimitPtr;
    /**
     * @see #virtualRel 虚拟帐本关系 <虚拟帐本ID,<关联帐本实例ID,比例> >
     */
    private Map<String, Map<String, Long>> virtualRel;
    /**
     * @see #currLimitFeeDepositLog 限额账本本次使用日志
     */
    private List<LimitFeeDepositLog> currLimitFeeDepositLog;
    /**
     * @see #hasBadBill 是否查询坏账欠费
     */
    private boolean hasBadBill;
    /**
     * 是否计算滞纳金
     */
    private boolean isCalcLateFee;

    /**
     *  是否触发信控
     */
    private boolean isFireCreditCtrl;
    /**
     * 滞纳金减免工单
     */
    private List<FeeDerateLateFeeLog> feeDerateLateFeeLogs;
    /**
     * 账本实例和可打金额关系
     */
    private Map<String, Long> invoiceFeeMap;
    /**
     * 销账方式
     */
    private int writeOffMode;
    /**
     * 是否抵扣期或补收期
     */
    private boolean isSpecialCycleStatus;

    /**
     * 交易员工对象
     */
    private Staff tradeStaff;
    /**
     * 统一余额播报信息
     */
    private UniBalanceInfo uniBalanceInfo;
    /**
     * 库外信控
     */
    private boolean outerCredit;

    public TradeCommInfo() {
        feeWriteSnapLog = new FeeWriteSnapLog();
    }

    public User getMainUser() {
        return mainUser;
    }

    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }

    public List<User> getPayUsers() {
        return payUsers;
    }

    public void setPayUsers(List<User> payUsers) {
        this.payUsers = payUsers;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //    public FeeAccount getFeeAccount() {
//        return feeAccount;
//    }
//
//    public void setFeeAccount(FeeAccount feeAccount) {
//        this.feeAccount = feeAccount;
//    }

    public List<FeeAccountDeposit> getFeeAccountDeposits() {
        return feeAccountDeposits;
    }

    public void setFeeAccountDeposits(List<FeeAccountDeposit> feeAccountDeposits) {
        this.feeAccountDeposits = feeAccountDeposits;
    }

    public List<FeeAcctBalanceRel> getFeeAcctBalanceRels() {
        return feeAcctBalanceRels;
    }

    public void setFeeAcctBalanceRels(List<FeeAcctBalanceRel> feeAcctBalanceRels) {
        this.feeAcctBalanceRels = feeAcctBalanceRels;
    }

    public FeeAcctPaymentCycle getFeeAcctPaymentCycle() {
        return feeAcctPaymentCycle;
    }

    public void setFeeAcctPaymentCycle(FeeAcctPaymentCycle feeAcctPaymentCycle) {
        this.feeAcctPaymentCycle = feeAcctPaymentCycle;
    }

    public List<FeeBill> getFeeBills() {
        return feeBills;
    }

    public void setFeeBills(List<FeeBill> feeBills) {
        this.feeBills = feeBills;
    }

    public FeeAcctPaymentCycle getActPaymentCycle() {
        return feeAcctPaymentCycle;
    }

    public void setActPaymentCycle(FeeAcctPaymentCycle feeAcctPaymentCycle) {
        this.feeAcctPaymentCycle = feeAcctPaymentCycle;
    }

    public int getNegativeBillDeposit() {
        return negativeBillDeposit;
    }

    public void setNegativeBillDeposit(int negativeBillDeposit) {
        this.negativeBillDeposit = negativeBillDeposit;
    }

    public FeePayLog getFeePayLog() {
        return feePayLog;
    }

    public void setFeePayLog(FeePayLog feePayLog) {
        this.feePayLog = feePayLog;
    }

    public List<FeeAccessLog> getAccesslogs() {
        return accesslogs;
    }

    public void setAccesslogs(List<FeeAccessLog> accesslogs) {
        this.accesslogs = accesslogs;
    }

    public List<FeeWriteOffLog> getFeeWriteOffLogs() {
        return feeWriteOffLogs;
    }

    public void setFeeWriteOffLogs(List<FeeWriteOffLog> feeWriteOffLogs) {
        this.feeWriteOffLogs = feeWriteOffLogs;
    }

    public FeeWriteSnapLog getFeeWriteSnapLog() {
        return feeWriteSnapLog;
    }

    public void setFeeWriteSnapLog(FeeWriteSnapLog feeWriteSnapLog) {
        this.feeWriteSnapLog = feeWriteSnapLog;
    }

    public Map<String, UserBalance> getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Map<String, UserBalance> userBalance) {
        this.userBalance = userBalance;
    }

    public WriteOffRuleInfo getWriteOffRuleInfo() {
        return writeOffRuleInfo;
    }

    public void setWriteOffRuleInfo(WriteOffRuleInfo writeOffRuleInfo) {
        this.writeOffRuleInfo = writeOffRuleInfo;
    }

    public Set<Integer> getChooseItem() {
        return chooseItem;
    }

    public void setChooseItem(Set<Integer> chooseItem) {
        this.chooseItem = chooseItem;
    }

    public Set<Integer> getChooseCycleId() {
        return chooseCycleId;
    }

    public void setChooseCycleId(Set<Integer> chooseCycleId) {
        this.chooseCycleId = chooseCycleId;
    }

    public Set<String> getChooseUserId() {
        return chooseUserId;
    }

    public void setChooseUserId(Set<String> chooseUserId) {
        this.chooseUserId = chooseUserId;
    }

    public Map<Integer, Set<Integer>> getDepositItemLimitPtr() {
        return depositItemLimitPtr;
    }

    public void setDepositItemLimitPtr(Map<Integer, Set<Integer>> depositItemLimitPtr) {
        this.depositItemLimitPtr = depositItemLimitPtr;
    }

    public Map<String, Map<String, Long>> getVirtualRel() {
        return virtualRel;
    }

    public void setVirtualRel(Map<String, Map<String, Long>> virtualRel) {
        this.virtualRel = virtualRel;
    }

    public List<LimitFeeDepositLog> getCurrLimitFeeDepositLog() {
        return currLimitFeeDepositLog;
    }

    public void setCurrLimitFeeDepositLog(List<LimitFeeDepositLog> currLimitFeeDepositLog) {
        this.currLimitFeeDepositLog = currLimitFeeDepositLog;
    }

    public boolean ifHasBadBill() {
        return hasBadBill;
    }

    public void setHasBadBill(boolean hasBadBill) {
        this.hasBadBill = hasBadBill;
    }

    public boolean isCalcLateFee() {
        return isCalcLateFee;
    }

    public void setCalcLateFee(boolean calcLateFee) {
        isCalcLateFee = calcLateFee;
    }

    public boolean isFireCreditCtrl() {
        return isFireCreditCtrl;
    }

    public void setFireCreditCtrl(boolean fireCreditCtrl) {
        isFireCreditCtrl = fireCreditCtrl;
    }

    public List<FeeDerateLateFeeLog> getFeeDerateLateFeeLogs() {
        return feeDerateLateFeeLogs;
    }

    public void setFeeDerateLateFeeLogs(List<FeeDerateLateFeeLog> feeDerateLateFeeLogs) {
        this.feeDerateLateFeeLogs = feeDerateLateFeeLogs;
    }

    public Map<String, Long> getInvoiceFeeMap() {
        return invoiceFeeMap;
    }

    public void setInvoiceFee(String acctBalanceId, long invoiceFee) {
        if (this.invoiceFeeMap == null) {
            this.invoiceFeeMap = new HashMap();
            this.invoiceFeeMap.put(acctBalanceId, invoiceFee);
        } else {
            if (this.invoiceFeeMap.containsKey(acctBalanceId)) {
                long fee = this.invoiceFeeMap.get(acctBalanceId);
                this.invoiceFeeMap.put(acctBalanceId, (fee + invoiceFee));
            } else {
                this.invoiceFeeMap.put(acctBalanceId, invoiceFee);
            }
        }
    }

    public void setReccFee(int paymentId, long recvFee) {
        if (this.getFeePayLog() == null) {
            this.feePayLog = new FeePayLog();
            this.feePayLog.setPaymentId(paymentId);
            this.feePayLog.setRecvFee(recvFee);
        } else {
            this.feePayLog.setPaymentId(paymentId);
            this.feePayLog.setRecvFee(recvFee);
        }
    }

    public int getWriteOffMode() {
        return writeOffMode;
    }

    public void setWriteOffMode(int writeOffMode) {
        this.writeOffMode = writeOffMode;
    }

    public boolean isSpecialCycleStatus() {
        return isSpecialCycleStatus;
    }

    public void setSpecialCycleStatus(boolean specialCycleStatus) {
        isSpecialCycleStatus = specialCycleStatus;
    }

    public void setInvoiceFeeMap(Map<String, Long> invoiceFeeMap) {
        this.invoiceFeeMap = invoiceFeeMap;
    }

    public Staff getTradeStaff() {
        return tradeStaff;
    }

    public void setTradeStaff(Staff tradeStaff) {
        this.tradeStaff = tradeStaff;
    }

    public UniBalanceInfo getUniBalanceInfo() {
        return uniBalanceInfo;
    }

    public void setUniBalanceInfo(UniBalanceInfo uniBalanceInfo) {
        this.uniBalanceInfo = uniBalanceInfo;
    }

    public List<User> getAllPayUsers() {
        return allPayUsers;
    }

    public void setAllPayUsers(List<User> allPayUsers) {
        this.allPayUsers = allPayUsers;
    }

    public boolean isOuterCredit() {
        return outerCredit;
    }

    public void setOuterCredit(boolean outerCredit) {
        this.outerCredit = outerCredit;
    }


    public boolean isHasBadBill() {
        return hasBadBill;
    }
}

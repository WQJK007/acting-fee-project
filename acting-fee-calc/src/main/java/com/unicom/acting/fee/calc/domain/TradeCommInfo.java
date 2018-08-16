package com.unicom.acting.fee.calc.domain;

import com.unicom.acting.fee.domain.*;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctBalanceRel;
import com.unicom.acts.pay.domain.AcctPaymentCycle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    /**
     * 账户账本
     */
    private List<AccountDeposit> accountDeposits;
    /**
     * 账本比例关系
     */
    private List<AcctBalanceRel> acctBalanceRels;
    /**
     * 账户账单
     */
    private List<Bill> bills;
    /**
     * 账户自定义缴费期
     */
    private AcctPaymentCycle acctPaymentCycle;
    /**
     * 负账单转换目的账本
     */
    private int negativeBillDeposit;
    /**
     * 交费日志对象
     */
    private PayLog payLog;
    /**
     * 存取款日志对象
     */
    private List<AccessLog> accesslogs;
    /**
     * 销账日志对象
     */
    private List<WriteOffLog> writeOffLogs;
    /**
     * 销账快照对象
     */
    private WriteSnapLog writeSnapLog;
    /**
     * 用户结余信息
     * 合帐用户，对于有用户级余额共享的情况同一个帐户有多个结余,或者分业务信控
     */
    private Map<String, UserBalance> userBalance;
    /**
     * 销账规则
     * 根据省份编码，地市编码，和交易账户网别获取具体的销账规则
     */
    private WriteOffRuleInfo writeOffRuleInfo;
    /**
     * 选择帐目销帐
     */
    private Set<Integer> chooseItem;
    /**
     * 选择帐期销帐
     */
    private Set<Integer> chooseCycleId;
    /**
     * 选择用户销帐
     */
    private Set<String> chooseUserId;
    /**
     * 帐本限定列表<账本类型, <可销账目项列表>>
     */
    private Map<Integer, Set<Integer>> depositItemLimitPtr;
    /**
     * 虚拟帐本关系 <虚拟帐本ID,<关联帐本实例ID,比例> >
     */
    private Map<String, Map<String, Long>> virtualRel;
    /**
     * 限额账本本次使用日志
     */
    private List<LimitFeeDepositLog> currLimitFeeDepositLog;
    /**
     * 是否存在坏账缴费
     */
    private boolean hasBadBill;
    /**
     * 是否触发信控
     */
    private boolean isFireCreditCtrl;
    /**
     * 是否计算滞纳金
     */
    private boolean isCalcLateFee;
    /**
     * 是否发送短信
     */
    private boolean isSendSms;
    /**
     * 增量出账账户
     */
    private boolean isAddAccount;
    /**
     * 滞纳金减免工单
     */
    private List<DerateLateFeeLog> derateLateFeeLogs;
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
     * 是否存在抵扣期DMN工单
     */
    private boolean existsPayLogDmn;
    /**
     * 是否存在外围对账工单
     */
    private boolean existsTradeCheck;
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
    /**
     * 账务后台交易工单表对象
     */
    private PayLogDmn payLogDmn;
    /**
     * 缴费其他日志表对象
     */
    private PayOtherLog payOtherLog;
    /**
     * 省份代收费日志
     */
    private List<CLPayLog> clPayLogs;
    /**
     * /短信入库列表 临时创建，运行稳定可以不用记录
     */
    private List<NoticeInfo> noticeInfoList;
    /**
     * 异步工单
     */
    private AsynWork asynWork;


    public TradeCommInfo() {
        writeSnapLog = new WriteSnapLog();
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

    public List<AccountDeposit> getAccountDeposits() {
        return accountDeposits;
    }

    public void setAccountDeposits(List<AccountDeposit> accountDeposits) {
        this.accountDeposits = accountDeposits;
    }

    public List<AcctBalanceRel> getAcctBalanceRels() {
        return acctBalanceRels;
    }

    public void setAcctBalanceRels(List<AcctBalanceRel> acctBalanceRels) {
        this.acctBalanceRels = acctBalanceRels;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public AcctPaymentCycle getAcctPaymentCycle() {
        return acctPaymentCycle;
    }

    public void setAcctPaymentCycle(AcctPaymentCycle acctPaymentCycle) {
        this.acctPaymentCycle = acctPaymentCycle;
    }

    public int getNegativeBillDeposit() {
        return negativeBillDeposit;
    }

    public void setNegativeBillDeposit(int negativeBillDeposit) {
        this.negativeBillDeposit = negativeBillDeposit;
    }

    public PayLog getPayLog() {
        return payLog;
    }

    public void setPayLog(PayLog payLog) {
        this.payLog = payLog;
    }

    public List<AccessLog> getAccesslogs() {
        return accesslogs;
    }

    public void setAccesslogs(List<AccessLog> accesslogs) {
        this.accesslogs = accesslogs;
    }

    public List<WriteOffLog> getWriteOffLogs() {
        return writeOffLogs;
    }

    public void setWriteOffLogs(List<WriteOffLog> writeOffLogs) {
        this.writeOffLogs = writeOffLogs;
    }

    public WriteSnapLog getWriteSnapLog() {
        return writeSnapLog;
    }

    public void setWriteSnapLog(WriteSnapLog writeSnapLog) {
        this.writeSnapLog = writeSnapLog;
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

    public boolean isFireCreditCtrl() {
        return isFireCreditCtrl;
    }

    public void setFireCreditCtrl(boolean fireCreditCtrl) {
        isFireCreditCtrl = fireCreditCtrl;
    }

    public boolean isCalcLateFee() {
        return isCalcLateFee;
    }

    public void setCalcLateFee(boolean calcLateFee) {
        isCalcLateFee = calcLateFee;
    }

    public boolean isSendSms() {
        return isSendSms;
    }

    public void setSendSms(boolean sendSms) {
        isSendSms = sendSms;
    }

    public boolean isAddAccount() {
        return isAddAccount;
    }

    public void setAddAccount(boolean addAccount) {
        isAddAccount = addAccount;
    }

    public List<DerateLateFeeLog> getDerateLateFeeLogs() {
        return derateLateFeeLogs;
    }

    public void setDerateLateFeeLogs(List<DerateLateFeeLog> derateLateFeeLogs) {
        this.derateLateFeeLogs = derateLateFeeLogs;
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
        if (this.getPayLog() == null) {
            this.payLog = new PayLog();
            this.payLog.setPaymentId(paymentId);
            this.payLog.setRecvFee(recvFee);
        } else {
            this.payLog.setPaymentId(paymentId);
            this.payLog.setRecvFee(recvFee);
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

    public boolean isExistsPayLogDmn() {
        return existsPayLogDmn;
    }

    public void setExistsPayLogDmn(boolean existsPayLogDmn) {
        this.existsPayLogDmn = existsPayLogDmn;
    }

    public boolean isExistsTradeCheck() {
        return existsTradeCheck;
    }

    public void setExistsTradeCheck(boolean existsTradeCheck) {
        this.existsTradeCheck = existsTradeCheck;
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

    public PayLogDmn getPayLogDmn() {
        return payLogDmn;
    }

    public void setPayLogDmn(PayLogDmn payLogDmn) {
        this.payLogDmn = payLogDmn;
    }

    public PayOtherLog getPayOtherLog() {
        return payOtherLog;
    }

    public void setPayOtherLog(PayOtherLog payOtherLog) {
        this.payOtherLog = payOtherLog;
    }

    public List<CLPayLog> getClPayLogs() {
        return clPayLogs;
    }

    public void setClPayLogs(List<CLPayLog> clPayLogs) {
        this.clPayLogs = clPayLogs;
    }

    public List<NoticeInfo> getNoticeInfoList() {
        return noticeInfoList;
    }

    public void setNoticeInfoList(List<NoticeInfo> noticeInfoList) {
        this.noticeInfoList = noticeInfoList;
    }

    public AsynWork getAsynWork() {
        return asynWork;
    }

    public void setAsynWork(AsynWork asynWork) {
        this.asynWork = asynWork;
    }

    public boolean isHasBadBill() {
        return hasBadBill;
    }
}

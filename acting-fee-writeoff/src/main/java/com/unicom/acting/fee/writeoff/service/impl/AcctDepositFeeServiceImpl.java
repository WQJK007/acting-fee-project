package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.DepositService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.AcctDepositFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acts.pay.dao.DepositDao;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctBalanceRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 账本表和账本销账关系表通过JDBC方式操作
 *
 * @author Administrators
 */
@Service
public class AcctDepositFeeServiceImpl implements AcctDepositFeeService {
    private static final Logger logger = LoggerFactory.getLogger(AcctDepositFeeServiceImpl.class);
    @Autowired
    private DepositDao depositDao;
    @Autowired
    private DepositService depositService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    @Override
    public List<AccountDeposit> getAcctDepositByAcctId(String acctId, String provinceCode) {
        return depositDao.getAcctDepositByAcctId(acctId, provinceCode);
    }

    @Override
    public List<AcctBalanceRel> getAcctBalanceRelByAcctId(String acctId, String provinceCode) {
        return depositDao.getAcctBalanceRelByAcctId(acctId, provinceCode);
    }

    @Override
    public void genAcctDepositByPayLogDmn(TradeCommInfoIn tradeCommInfoIn, PayLogDmn payLogDmn, TradeCommInfo tradeCommInfo) {
        //异常信息提示
        if (tradeCommInfo.getAccount() == null || tradeCommInfo.getWriteOffRuleInfo() == null) {
            return;
        }
        Account account = tradeCommInfo.getAccount();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        PaymentDeposit paymentDeposit = writeOffRuleInfo.getPaymentDeposit(
                payLogDmn.getPaymentId(), payLogDmn.getPayFeeModeCode());

        if (paymentDeposit == null) {
            throw new SkyArkException("没有配置储值方式和帐本科目关系! paymentId = "
                    + payLogDmn.getPaymentId() + ",payFeeModeCode = "
                    + payLogDmn.getPayFeeModeCode());
        }

        //帐本使用级别
        char privateTag = payLogDmn.getPrivateTag();
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = paymentDeposit.getPrivateTag();
        }

        //账本生效时间
        String startDate = payLogDmn.getStartDate();
        if (StringUtil.isEmptyCheckNullStr(startDate)) {
            startDate = writeOffRuleInfo.getSysdate();
        }

        if (startDate.length() < 10 || startDate.substring(0, 4).compareTo("2008") < 0) {
            throw new SkyArkException("帐本生效时间不合法!startDate=" + startDate);
        }

        AccountDeposit accountDeposit = new AccountDeposit();
        boolean find = false;
        List<AccountDeposit> deposits = depositService.getAcctDepositsByDepositCode(
                tradeCommInfo.getAccountDeposits(), paymentDeposit.getDepositCode());
        if (!CollectionUtils.isEmpty(deposits)) {
            for (AccountDeposit pAcctDeposit : deposits) {
                if (pAcctDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && writeOffRuleInfo.depositIfUnite(pAcctDeposit.getDepositCode())
                        && pAcctDeposit.getActionCode() <= 0
                        && pAcctDeposit.getValidTag() == tradeCommInfoIn.getValidTag() //帐本状态一样
                        && '0' == pAcctDeposit.getLimitMode() //无限额
                        && '0' == StringUtil.firstOfString(tradeCommInfoIn.getLimitMode())  //没有月限额
                        && ('1' != pAcctDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pAcctDeposit.getPrivateTag() && '1' == privateTag
                        && pAcctDeposit.getUserId().equals(payLogDmn.getUserId()))) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    accountDeposit = pAcctDeposit;
                    break;
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            //账本序列
            String acctBalanceId = sysCommOperFeeService.getSequence(tradeCommInfoIn.getEparchyCode(),
                    ActPayPubDef.SEQ_ACCTBALANCE_ID, tradeCommInfoIn.getProvinceCode());
            if (acctBalanceId == null || "".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
            }

            accountDeposit.setAcctBalanceId(acctBalanceId);
            accountDeposit.setAcctId(account.getAcctId());
            accountDeposit.setUserId(payLogDmn.getUserId());
            accountDeposit.setDepositCode(paymentDeposit.getDepositCode());
            accountDeposit.setInitMoney(0);
            accountDeposit.setRecvFee(0);

            //可作为发票金额
            if ('1' == paymentDeposit.getInvoiceTag()) {
                accountDeposit.setInvoiceFee(payLogDmn.getRecvFee());
            }

            //设置限额方式
            accountDeposit.setLimitMode('0');

            if (tradeCommInfoIn.getLimitMoney() <= 0 || tradeCommInfoIn.getLimitMoney() >= ActPayPubDef.MAX_LIMIT_FEE) {
                accountDeposit.setLimitMoney(-1);
            } else {
                accountDeposit.setLimitMoney(tradeCommInfoIn.getLimitMoney()); //限额
            }

            //设置账本销账账期范围
            if (ActPayPubDef.MAX_MONTH_NUM == payLogDmn.getMonths() || payLogDmn.getMonths() <= 0) {
                accountDeposit.setStartCycleId(ActPayPubDef.MIN_CYCLE_ID);
                accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
            } else {
                int startCycleId = Integer.parseInt(startDate.substring(0, 4) + startDate.substring(5, 7));
                accountDeposit.setStartCycleId(startCycleId);
                accountDeposit.setEndCycleId(TimeUtil.genCycle(accountDeposit.getStartCycleId(), (payLogDmn.getMonths() - 1)));
                if (accountDeposit.getEndCycleId() > ActPayPubDef.MAX_CYCLE_ID) {
                    accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
                }
            }

            //帐本生效开始时间
            accountDeposit.setStartDate(startDate);
            if (accountDeposit.getEndCycleId() >= ActPayPubDef.MAX_CYCLE_ID) {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(accountDeposit.getEndCycleId()).getCycEndTime());
            } else {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(accountDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            accountDeposit.setEparchyCode(account.getEparchyCode());
            accountDeposit.setProvinceCode(account.getProvinceCode());
            accountDeposit.setPrivateTag(payLogDmn.getPrivateTag());
            accountDeposit.setVersionNo(1);    //更新版本号
            accountDeposit.setNewFlag('1');  //新增标志
            accountDeposit.setValidTag(tradeCommInfoIn.getValidTag());
        } else {
            //可作为发票金额
            if ('1' == paymentDeposit.getInvoiceTag()) {
                accountDeposit.setInvoiceFee(accountDeposit.getInvoiceFee() + payLogDmn.getRecvFee());
            }
        }


        //进入临时时表的数据直接累计到对应存折上
        accountDeposit.setMoney(accountDeposit.getMoney() + payLogDmn.getRecvFee());
//        if (writeOffRuleInfo.getParity() == ActPayPubDef.ODD) {
//            //奇数月余额
//            accountDeposit.setOddMoney(accountDeposit.getOddMoney() + payLogDmn.getRecvFee());
//        } else {
//            //偶数月余额
//            accountDeposit.setEvenMoney(accountDeposit.getEvenMoney() + payLogDmn.getRecvFee());
//        }
        //更新账本列表
        depositService.accountDepositUpAndSort(writeOffRuleInfo, tradeCommInfo.getAccountDeposits(), accountDeposit);
    }

}

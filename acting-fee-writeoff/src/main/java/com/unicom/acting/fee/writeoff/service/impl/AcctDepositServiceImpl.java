package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.fee.dao.FeeAccountDepositDao;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.AcctDepositService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
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
public class AcctDepositServiceImpl implements AcctDepositService {
    private static final Logger logger = LoggerFactory.getLogger(AcctDepositServiceImpl.class);
    @Autowired
    private FeeAccountDepositDao feeAccountDepositDao;
    @Autowired
    private DepositCalcService depositCalcService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    @Override
    public List<FeeAccountDeposit> getAcctDepositByAcctId(String acctId) {
        return feeAccountDepositDao.getAcctDepositByAcctId(acctId);
    }

    @Override
    public List<FeeAcctBalanceRel> getAcctBalanceRelByAcctId(String acctId) {
        return feeAccountDepositDao.getAcctBalanceRelByAcctId(acctId);
    }

    @Override
    public void genAcctDepositByPayLogDmn(TradeCommInfoIn tradeCommInfoIn, FeePayLogDmn feePayLogDmn, TradeCommInfo tradeCommInfo) {
        //异常信息提示
        if (tradeCommInfo.getAccount() == null || tradeCommInfo.getWriteOffRuleInfo() == null) {
            return;
        }
        Account feeAccount = tradeCommInfo.getAccount();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        PaymentDeposit paymentDeposit = writeOffRuleInfo.getPaymentDeposit(
                feePayLogDmn.getPaymentId(), feePayLogDmn.getPayFeeModeCode());

        if (paymentDeposit == null) {
            throw new SkyArkException("没有配置储值方式和帐本科目关系! paymentId = "
                    + feePayLogDmn.getPaymentId() + ",payFeeModeCode = "
                    + feePayLogDmn.getPayFeeModeCode());
        }

        //帐本使用级别
        char privateTag = feePayLogDmn.getPrivateTag();
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = paymentDeposit.getPrivateTag();
        }

        //账本生效时间
        String startDate = feePayLogDmn.getStartDate();
        if (StringUtil.isEmptyCheckNullStr(startDate)) {
            startDate = writeOffRuleInfo.getSysdate();
        }

        if (startDate.length() < 10 || startDate.substring(0, 4).compareTo("2008") < 0) {
            throw new SkyArkException("帐本生效时间不合法!startDate=" + startDate);
        }

        FeeAccountDeposit feeAccountDeposit = new FeeAccountDeposit();
        boolean find = false;
        List<FeeAccountDeposit> deposits = depositCalcService.getAcctDepositsByDepositCode(
                tradeCommInfo.getFeeAccountDeposits(), paymentDeposit.getDepositCode());
        if (!CollectionUtils.isEmpty(deposits)) {
            for (FeeAccountDeposit pFeeAccountDeposit : deposits) {
                if (pFeeAccountDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && writeOffRuleInfo.depositIfUnite(pFeeAccountDeposit.getDepositCode())
                        && pFeeAccountDeposit.getActionCode() <= 0
                        && pFeeAccountDeposit.getValidTag() == tradeCommInfoIn.getValidTag() //帐本状态一样
                        && '0' == pFeeAccountDeposit.getLimitMode() //无限额
                        && '0' == StringUtil.firstOfString(tradeCommInfoIn.getLimitMode())  //没有月限额
                        && ('1' != pFeeAccountDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pFeeAccountDeposit.getPrivateTag() && '1' == privateTag
                        && pFeeAccountDeposit.getUserId().equals(feePayLogDmn.getUserId()))) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    feeAccountDeposit = pFeeAccountDeposit;
                    break;
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            //账本序列
            String acctBalanceId = sysCommOperFeeService.getActsSequence(ActingFeePubDef.SEQ_ACCTBALANCEID_TABNAME,
                    ActingFeePubDef.SEQ_ACCTBALANCEID_COLUMNNAME , tradeCommInfoIn.getProvinceCode());
            if (acctBalanceId == null || "".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCEID_TABNAME");
            }

            feeAccountDeposit.setAcctBalanceId(acctBalanceId);
            feeAccountDeposit.setAcctId(feeAccount.getAcctId());
            feeAccountDeposit.setUserId(feePayLogDmn.getUserId());
            feeAccountDeposit.setDepositCode(paymentDeposit.getDepositCode());
            feeAccountDeposit.setInitMoney(0);
            feeAccountDeposit.setRecvFee(0);

            //可作为发票金额
            if ('1' == paymentDeposit.getInvoiceTag()) {
                feeAccountDeposit.setInvoiceFee(feePayLogDmn.getRecvFee());
            }

            //设置限额方式
            feeAccountDeposit.setLimitMode('0');

            if (tradeCommInfoIn.getLimitMoney() <= 0 || tradeCommInfoIn.getLimitMoney() >= ActingFeePubDef.MAX_LIMIT_FEE) {
                feeAccountDeposit.setLimitMoney(-1);
            } else {
                feeAccountDeposit.setLimitMoney(tradeCommInfoIn.getLimitMoney()); //限额
            }

            //设置账本销账账期范围
            if (ActingFeePubDef.MAX_MONTH_NUM == feePayLogDmn.getMonths() || feePayLogDmn.getMonths() <= 0) {
                feeAccountDeposit.setStartCycleId(ActingFeePubDef.MIN_CYCLE_ID);
                feeAccountDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
            } else {
                int startCycleId = Integer.parseInt(startDate.substring(0, 4) + startDate.substring(5, 7));
                feeAccountDeposit.setStartCycleId(startCycleId);
                feeAccountDeposit.setEndCycleId(TimeUtil.genCycle(feeAccountDeposit.getStartCycleId(), (feePayLogDmn.getMonths() - 1)));
                if (feeAccountDeposit.getEndCycleId() > ActingFeePubDef.MAX_CYCLE_ID) {
                    feeAccountDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
                }
            }

            //帐本生效开始时间
            feeAccountDeposit.setStartDate(startDate);
            if (feeAccountDeposit.getEndCycleId() >= ActingFeePubDef.MAX_CYCLE_ID) {
                feeAccountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(feeAccountDeposit.getEndCycleId()).getCycEndTime());
            } else {
                feeAccountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(feeAccountDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            feeAccountDeposit.setEparchyCode(feeAccount.getEparchyCode());
            feeAccountDeposit.setProvinceCode(feeAccount.getProvinceCode());
            feeAccountDeposit.setPrivateTag(feePayLogDmn.getPrivateTag());
            feeAccountDeposit.setVersionNo(1);    //更新版本号
            feeAccountDeposit.setNewFlag('1');  //新增标志
            feeAccountDeposit.setValidTag(tradeCommInfoIn.getValidTag());
        } else {
            //可作为发票金额
            if ('1' == paymentDeposit.getInvoiceTag()) {
                feeAccountDeposit.setInvoiceFee(feeAccountDeposit.getInvoiceFee() + feePayLogDmn.getRecvFee());
            }
        }

        //进入临时时表的数据直接累计到对应存折上
        feeAccountDeposit.setMoney(feeAccountDeposit.getMoney() + feePayLogDmn.getRecvFee());
        //更新账本列表
        depositCalcService.accountDepositUpAndSort(writeOffRuleInfo, tradeCommInfo.getFeeAccountDeposits(), feeAccountDeposit);
    }

}

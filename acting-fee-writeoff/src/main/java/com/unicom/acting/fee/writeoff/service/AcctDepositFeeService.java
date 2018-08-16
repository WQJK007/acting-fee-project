package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.PayLogDmn;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctBalanceRel;

import java.util.List;

/**
 * 账本表和账本销账关系表资源操作
 *
 * @author Administrators
 */
public interface AcctDepositFeeService extends IBaseService {
    /**
     * 根据账户标识查询账本
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    List<AccountDeposit> getAcctDepositByAcctId(String acctId, String provinceCode);

    /**
     * 根据账户标识查询账本销账关系
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    List<AcctBalanceRel> getAcctBalanceRelByAcctId(String acctId, String provinceCode);

    void genAcctDepositByPayLogDmn(TradeCommInfoIn tradeCommInfoIn, PayLogDmn payLogDmn, TradeCommInfo tradeCommInfo);
}

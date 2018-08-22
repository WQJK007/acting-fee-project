package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 账本表和账本销账关系表资源操作
 *
 * @author wangkh
 */
public interface AcctDepositService extends IBaseService {
    /**
     * 根据账户标识查询账本
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    List<FeeAccountDeposit> getAcctDepositByAcctId(String acctId, String provinceCode);

    /**
     * 根据账户标识查询账本销账关系
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    List<FeeAcctBalanceRel> getAcctBalanceRelByAcctId(String acctId, String provinceCode);

    void genAcctDepositByPayLogDmn(TradeCommInfoIn tradeCommInfoIn, FeePayLogDmn feePayLogDmn, TradeCommInfo tradeCommInfo);
}

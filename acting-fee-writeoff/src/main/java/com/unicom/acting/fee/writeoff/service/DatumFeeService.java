package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acts.pay.domain.AcctPaymentCycle;

/**
 * 三户资料相关资料访问
 *
 * @author Administrators
 */
public interface DatumFeeService extends IBaseService {
    /**
     * 查询三户资料
     *
     * @param tradeCommInfoIn
     * @return
     */
    UserDatumInfo getUserDatumByMS(TradeCommInfoIn tradeCommInfoIn);

    /**
     * 查询账户自定义缴费期
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    AcctPaymentCycle getAcctPaymentCycle(String acctId, String provinceCode);

    /**
     * 特定缴费期
     *
     * @param cycle
     * @return
     */
    boolean isSpecialRecvState(Cycle cycle);

    /**
     * 是否坏账账户
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    boolean isBadBillUser(String acctId, String provinceCode);

    /**
     * 是否免滞纳金计算用户
     *
     * @param userId
     * @param acctId
     * @param provinceCode
     * @return
     */
    boolean isNoCalcLateFeeUser(String userId, String acctId, String provinceCode);
}

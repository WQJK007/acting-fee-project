package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.FeeAcctPaymentCycle;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;

/**
 * 三户资料相关资料访问
 *
 * @author wangkh
 */
public interface DatumService extends IBaseService {
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
     * @return
     */
    FeeAcctPaymentCycle getAcctPaymentCycle(String acctId);

    /**
     * 是否坏账账户
     *
     * @param acctId
     * @return
     */
    boolean isBadBillUser(String acctId);

    /**
     * 是否免滞纳金计算用户
     *
     * @param userId
     * @param acctId
     * @return
     */
    boolean isNoCalcLateFeeUser(String userId, String acctId);
}

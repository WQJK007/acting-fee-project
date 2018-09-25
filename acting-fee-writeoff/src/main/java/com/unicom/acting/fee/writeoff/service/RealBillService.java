package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 实时账单查询服务
 *
 * @author Wangkh
 */
public interface RealBillService extends IBaseService {
    /**
     * 查询内存库实时账单
     *
     * @param tradeCommInfoIn
     * @param acctId
     * @param userId
     * @param startCycleId
     * @param endCycleId
     * @return
     */
    List<FeeBill> getRealBillFromMemDB(TradeCommInfoIn tradeCommInfoIn, String acctId, String userId, int startCycleId, int endCycleId);
}

package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.skyark.component.service.IBaseService;


/**
 * @author Wangkh
 */
public interface OweFeeCommService extends IBaseService {
    /**
     * 欠费查询结果
     * @param feeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    TradeCommInfoOut genOweFeeCommInfoOut(FeeCommInfoIn feeCommInfoIn, TradeCommInfo tradeCommInfo);
}

package com.unicom.acting.fee.owefee.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 欠费查询公共方法
 *
 * @author wangkh
 */
public interface OweFeeService extends IBaseService {
    /**
     * 欠费查询核心方法
     *
     * @param feeCommInfoIn
     * @param tradeCommInfo
     * @throws SkyArkException
     */
    void queryOweFee(FeeCommInfoIn feeCommInfoIn, TradeCommInfo tradeCommInfo) throws SkyArkException;

    /**
     * 欠费查询核心方法
     *
     * @param feeCommInfoIn
     * @param tradeCommInfo
     * @return
     * @throws SkyArkException
     */
    TradeCommInfoOut queryOweFee(FeeCommInfoIn feeCommInfoIn) throws SkyArkException;
}

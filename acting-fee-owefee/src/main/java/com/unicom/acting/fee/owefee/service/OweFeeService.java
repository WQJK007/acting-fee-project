package com.unicom.acting.fee.owefee.service;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.domain.TradeCommInfoOut;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 欠费查询公共方法
 *
 * @author Administrators
 */
public interface OweFeeService extends IBaseService {
    /**
     * 欠费查询核心方法
     *
     * @param tradeCommInfoIn
     * @return
     */
    TradeCommInfoOut queryOweFee(TradeCommInfoIn tradeCommInfoIn) throws SkyArkException;

    ;
}

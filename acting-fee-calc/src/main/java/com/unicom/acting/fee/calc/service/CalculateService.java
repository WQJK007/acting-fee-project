package com.unicom.acting.fee.calc.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.skyark.component.service.IBaseService;

public interface CalculateService extends IBaseService {
    /**
     * 模拟销账结余计算
     *
     * @param tradeCommInfo
     */
    void calc(TradeCommInfo tradeCommInfo);

    /**
     * 交易销账结余计算
     *
     * @param tradeCommInfo
     */
    void recvCalc(TradeCommInfo tradeCommInfo);
}

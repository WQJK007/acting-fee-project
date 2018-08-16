package com.unicom.acting.fee.calc.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;

public interface CalculateService extends IBaseService {
    /**
     * @see #ASM_BALAN_BORD
     * 分省控制哪些账本不包含在账户当前可用余额中
     */
    String ASM_BALAN_BORD = "ASM_BALAN_BORD";

    /**
     * @see #ASM_CONSIGN_USERBALANCE
     * 广东托收用户参与实时信控,往月欠费不计入用户结余
     */
    String ASM_CONSIGN_USERBALANCE = "ASM_CONSIGN_USERBALANCE";

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

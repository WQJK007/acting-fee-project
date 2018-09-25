package com.unicom.acting.fee.batch.domain;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;

/**************************************
 * @Created on 2018-8-30.      ******
 * @Author: zhengp11            ******
 * @Version: 1.0                ******
 * @Function:                   ******
 *************************************/
public class BatchLinkInfo {

    private FeeCommInfoIn tradeCommInfoIn;
    private TradeCommInfo tradeCommInfo;

    public FeeCommInfoIn getTradeCommInfoIn() {
        return tradeCommInfoIn;
    }

    public void setTradeCommInfoIn(FeeCommInfoIn tradeCommInfoIn) {
        this.tradeCommInfoIn = tradeCommInfoIn;
    }

    public TradeCommInfo getTradeCommInfo() {
        return tradeCommInfo;
    }

    public void setTradeCommInfo(TradeCommInfo tradeCommInfo) {
        this.tradeCommInfo = tradeCommInfo;
    }

}

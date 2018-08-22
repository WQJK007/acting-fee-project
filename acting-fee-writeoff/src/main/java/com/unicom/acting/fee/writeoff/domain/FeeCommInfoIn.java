package com.unicom.acting.fee.writeoff.domain;


/**
 * 欠费查询请求参数
 *
 * @author Wangkh
 */
public class FeeCommInfoIn extends TradeCommInfoIn {
    private String targetData; //2加上帐单信息  3加上销帐日志
    private String tradeEnter; // 0 不校验大合账 1 校验大合账

    public FeeCommInfoIn() {
        targetData = "0";
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
    }

    public String getTradeEnter() {
        return tradeEnter;
    }

    public void setTradeEnter(String tradeEnter) {
        this.tradeEnter = tradeEnter;
    }

    @Override
    public String toString() {
        return "FeeCommInfoIn{" +
                "targetData='" + targetData + '\'' +
                ", tradeEnter='" + tradeEnter + '\'' +
                '}';
    }
}

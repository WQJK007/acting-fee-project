package com.unicom.acting.fee.writeoff.service;


import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

/**
 * 销账相关公共方法
 * @author Administrators
 */
public interface WriteOffFeeService extends IBaseService {

    /**
     * 查询三户资料
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void genUserDatumInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 获取地市账期信息
     * @param tradeCommInfo
     * @param eparchyCode
     * @param provinceCode
     */
    void genEparchyCycleInfo(TradeCommInfo tradeCommInfo, String eparchyCode, String provinceCode);

    /**
     * 获取账本资源
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getAcctBalance(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 获取账单
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getOweBill(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 特殊缴费校验
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void specialTradeCheck(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 是否计算滞纳金
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    boolean ifCalcLateFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 加载滞纳金减免工单
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getDerateFeeLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 加载账户自定义缴费期
     * @param tradeCommInfo
     * @param acctId
     * @param provinceCode
     */
    void getAcctPaymentCycle(TradeCommInfo tradeCommInfo, String acctId, String provinceCode);
}
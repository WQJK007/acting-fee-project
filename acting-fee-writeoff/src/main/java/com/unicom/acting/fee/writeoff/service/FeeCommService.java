package com.unicom.acting.fee.writeoff.service;


import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 费用功能公共方法
 *
 * @author Administrators
 */
public interface FeeCommService extends IBaseService {

    /**
     * 查询三户资料
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getUserDatumInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 获取地市账期信息
     *
     * @param tradeCommInfo
     * @param eparchyCode   账户归属地市编码
     * @param provinceCode  账户归属省份编码
     */
    void getEparchyCycleInfo(TradeCommInfo tradeCommInfo, String eparchyCode, String provinceCode);

    /**
     * 获取账户地市销账规则
     *
     * @param writeOffRuleInfo
     * @param provinceCode
     * @param eparchyCode
     * @param netTypeCode
     */
    void getWriteOffRule(WriteOffRuleInfo writeOffRuleInfo, String provinceCode, String eparchyCode, String netTypeCode);


    /**
     * 获取账本资源
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getAcctBalance(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 获取账单
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getOweBill(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 业务特殊校验
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void specialBusiCheck(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 是否存在托收在途账单
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    boolean ifBillConsigning(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 是否托收账户
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    boolean ifConsignPayMode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 是否存在预打记录
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    boolean ifPrePrintInvoice(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 电子赠款停机状态不能赠送
     *
     * @param mainUser
     * @param paymentId
     * @param writeOffRuleInfo
     */
    void elecPresentLimit(User mainUser, int paymentId, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 是否计算滞纳金
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    boolean ifCalcLateFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 加载滞纳金减免工单
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void getFeeDerateLateFeeLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 加载账户自定义缴费期
     *
     * @param tradeCommInfo
     * @param acctId
     */
    void getAcctPaymentCycle(TradeCommInfo tradeCommInfo, String acctId);
}

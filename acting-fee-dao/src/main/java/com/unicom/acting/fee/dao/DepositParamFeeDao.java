package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.DepositLimitRule;
import com.unicom.acting.fee.domain.DepositPriorRule;
import com.unicom.acting.fee.domain.PaymentDeposit;

import java.util.List;

/**
 * 账本相关参数表数据库操作，目前主要包括TD_B_DEPOSITPRIORRULE，TD_B_DEPOSITLIMITRULE，TD_B_PAYMENT_DEPOSIT查询操作
 *
 * @author Wangkh
 */
public interface DepositParamFeeDao extends IBaseDao {
    //获取账本科目优先级全集
    List<DepositPriorRule> getDepositPriorRule(String provinceCode);

    //获取账本科目限定规则全集
    List<DepositLimitRule> getDepositLimitRule(String provinceCode);

    //获取储值方式和账本科目对应关系全集
    List<PaymentDeposit> getPaymentDeposit(String provinceCode);
}

package com.unicom.acting.fee.domain;

/**
 * 费用查询相关参数常量
 *
 * @author Wangkh
 */
public class ActingFeeCommparaDef {
    private ActingFeeCommparaDef() {
    }

    /**
     * @see #ASM_DEPOSIT_PRIVATE_PRIORITY
     * 私有账本使用排序规则
     */
    public static final String ASM_DEPOSIT_PRIVATE_PRIORITY = "ASM_DEPOSIT_PRIVATE_PRIORITY";

    /**
     * @see #ASM_BALAN_BORD
     * 分省控制哪些账本不包含在账户当前可用余额中
     */
    public static final String ASM_BALAN_BORD = "ASM_BALAN_BORD";

    /**
     * @see #ASM_CONSIGN_USERBALANCE
     * 广东托收用户参与实时信控,往月欠费不计入用户结余
     */
    public static final String ASM_CONSIGN_USERBALANCE = "ASM_CONSIGN_USERBALANCE";
}

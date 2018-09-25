package com.unicom.acting.fee.domain;

/**
 * 用费计算费用查询常量
 *
 * @author Wangkh
 */
public class ActingFeePubDef {

    private ActingFeePubDef() {
    }

    /**
     * @see #DEFAULT_PROVINCE_CODE 销账规则使用默认省份编码
     */
    public static final String DEFAULT_PROVINCE_CODE = "ZZZZ";
    /**
     * @see #DEFAULT_EPARCHY_CODE 销账规则使用默认地市编码
     */
    public static final String DEFAULT_EPARCHY_CODE = "ZZZZ";
    /**
     * @see #DEFAULT_NET_TYPE_CODE 销账规则使用默认网别编码
     */
    public static final String DEFAULT_NET_TYPE_CODE = "ZZ";

    /**
     * @see #DEPOSIT_PRIOR_RULE_TYPE 帐本科目优先地市规则
     */
    public static final char DEPOSIT_PRIOR_RULE_TYPE = '0';

    /**
     * @see #DEPOSIT_LIMIT_RULE_TYPE 帐本科目限定地市规则
     */
    public static final char DEPOSIT_LIMIT_RULE_TYPE = '1';
    /**
     * @see #ITEM_PRIOR_RULE_TYPE 帐目优先地市规则
     */
    public static final char ITEM_PRIOR_RULE_TYPE = '2';
    /**
     * @see #PAYMENT_DEPOSIT_RULE_TYPE 储值方式和帐本类型
     */
    public static final char PAYMENT_DEPOSIT_RULE_TYPE = '3';

    /**
     * @see #LATEFEE_CALCPARA_RULE_TYPE  滞纳金计算规则
     */
    public static final char LATEFEE_CALCPARA_RULE_TYPE = 'B';


    //####序列类型####
    /**
     * @see #SEQ_ACCTBALANCEID_TABNAME 账本序列表名
     */
    public static final String SEQ_ACCTBALANCEID_TABNAME = "TF_F_ACCOUNTDEPOSIT";
    /**
     * @see #SEQ_ACCTBALANCEID_COLUMNNAME 账本序列列名
     */
    public static final String SEQ_ACCTBALANCEID_COLUMNNAME = "ACCT_BALANCE_ID";

    /**
     * @see #SEQ_BILLID_COLUMNNAME 账单流水
     */
    public static final String SEQ_BILLID_TABNAME = "TS_B_BILL";
    /**
     * @see #SEQ_BILLID_COLUMNNAME 账单流水
     */
    public static final String SEQ_BILLID_COLUMNNAME = "BILL_ID";
    /**
     * @see #SEQ_TRADE_ID 30位交易流水
     * 待定账务交易流水
     */
    public static final String SEQ_TRADE_ID = "SEQ_TRADE_ID";


    //交费账期
    /**
     * @see #MIN_CYCLE_ID 最小销账账期
     */
    public static final int MIN_CYCLE_ID = 198001;
    /**
     * @see #MAX_CYCLE_ID 最大销账账期
     */
    public static final int MAX_CYCLE_ID = 203001;
    /**
     * @see #MAX_MONTH_NUM 最大销账账期数
     */
    public static final int MAX_MONTH_NUM = 240;

    /**
     * @see #MAX_LIMIT_FEE 最大销账限额
     */
    public static final long MAX_LIMIT_FEE = 99999999999L;

    /**
     * @see #VIRTUAL_PREFIX 虚拟账本实例标识
     */
    public static final String VIRTUAL_PREFIX = "V1000";

    //调用的微服务
    //三户资料查询微服务
    public static final String QRY_USER_DATUM = "userdatumjdbc";
    //查询实时账单微服务
    public static final String QRY_REAL_BILL = "realbilljdbc";
    //获取序列微服务
    public static final String GET_SEQUENCE = "getorcsqe";


}

package com.unicom.acting.fee.domain;

public class ActPayPubDef {
    private ActPayPubDef() {
    }

    //RDS参数库
    public static final String ACT_RDS_DBCONN = "rds_para";
    //RDS工单库1
    public static final String ACT_RDS_DBCONN_ORDER1 = "rds_order1";
    //RDS工单库1
    public static final String ACT_RDS_DBCONN_ORDER2= "rds_order2";
    //账务中心数据库
    public static final String ACTING_DRDS_DBCONN = "acting";
    //账户中心数据库
    public static final String ACTS_DRDS_DBCONN = "acts";

    //默认省份、地市、网别值，获取销账规则使用
    public static final String DEFAULT_PROVINCE_CODE = "ZZZZ";
    public static final String DEFAULT_EPARCHY_CODE = "ZZZZ";
    public static final String DEFAULT_NET_TYPE_CODE = "ZZ";

    //地市销账规则类型
    public static final char DEPOSIT_PRIOR_RULE_TYPE = '0';  //帐本科目优先地市规则
    public static final char DEPOSIT_LIMIT_RULE_TYPE = '1';  //帐本科目限定地市规则
    public static final char ITEM_PRIOR_RULE_TYPE = '2';  //帐目优先地市规则
    public static final char PAYMENT_DEPOSIT_RULE_TYPE = '3';  //储值方式和帐本类型
    public static final char LATEFEE_CALCPARA_RULE_TYPE = 'B'; //滞纳金计算规则

    //奇偶月类型
    public static final int EVEN = 0;   //偶数月
    public static final int ODD = 1;    //奇数月

    //序列类型
    public static final String SEQ_TRADE_ID = "SEQ_TRADE_ID"; //交易流水
    public static final String SEQ_CHARGE_ID = "SEQ_CHARGE_ID"; //交费流水
    public static final String SEQ_ACCESS_ID = "SEQ_ACCESS_ID";  //生成存取款日志流水
    public static final String SEQ_WRITEOFF_ID = "SEQ_WRITEOFF_ID";  //生成销账日志流水
    public static final String SEQ_ACCTBALANCE_ID = "SEQ_ACCTBALANCE_ID"; //账本序列
    public static final String SEQ_BILL_ID = "SEQ_BILL_ID";     //账单流水
    public static final String SEQ_SMSSEND_ID = "SEQ_SMSSEND_ID";   //短信工单流水

    //交费账期
    public static final int MIN_CYCLE_ID = 198001;    //最小销账账期
    public static final int MAX_CYCLE_ID = 203001;  //最大销账账期
    public static final int MAX_MONTH_NUM = 240;    //最大销账账期数

    //限额
    public static final long MAX_LIMIT_FEE = 99999999999L;    //最大销账限额

    //虚拟账本实例标识
    public static final String VIRTUAL_PREFIX = "V1000";
    //大合帐默认付费用户数
    public static final int MUTIACCT_PAYRELATION_DEFAULTNUM = 100;

    //信控模式
    //发送信控开机工单
    public static final String JIAOFEI_TO_CREDIT = "JIAOFEI_TO_CREDIT";
    //不发送信控开机工单
    public static final String RECV_TO_CREDIT = "RECV_TO_CREDIT";
    //触发信控工单类型
    public static final String RECVCREDIT_TRADE_TYPE_DEFAULT = "8000";
    public static final String RECVCREDIT_TRADE_TYPE_BIGACCT = "8001";


    //按总额清退
    public static final String BACK_BY_ALLMONEY = "0";
    //按特定账本科目类型清退
    public static final String BACK_BY_DEPOSITCODE = "1";
    //按账本实例清退
    public static final String BACK_BY_ACCTBALANCEID = "2";


    //调用的微服务
    //三户资料查询微服务
    public static final String QRY_USER_DATUM = "userdatumjdbc";
    //查询实时账单微服务
    public static final String QRY_REAL_BILL = "realbilljdbc";
    //获取序列微服务
    public static final String GET_SEQUENCE = "getorcsqe";

    //用户属性查询
    public static final String QRY_USER_PARAM = "userinfo/getuserparam";

    //用户属性查询
    public static final String QRY_USER_RELATION = "userinfo/getuserrelation";

    //非抵扣期
    public static final String ORDER_CYCLE_STATUS = "0";

    //抵扣期
    public static final String DRECV_CYCLE_STATUS = "1";

    //回检TF_B_PAYLOG
    public static final String CHECK_TYPE_PAYLOG = "0";

    //回检TF_B_PAYLOG_DMN
    public static final String CHECK_TYPE_PAYLOGDMN = "1";

    //回检TF_B_ASYN_WORK
    public static final String CHECK_TYPE_ASYNWORK = "2";

    //Topic类型
    public static final String TRADE_MQTYPE_ORDER = "DEVACT_SYNCDEPO_TEST1";


}

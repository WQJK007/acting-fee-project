package com.unicom.acting.fee.writeoff.domain;


/**
 * 费用交易参数类，主要包括交易需要入参
 *
 * @author wangkh
 */
public class TradeCommInfoIn {
    //灰度参数
    private String headerGray;

    //交易用户信息
    private String serialNumber;            //服务号码
    private String netTypeCode;            //网别类型
    private String removeTag;             //在网状态,默认是0
    private String userId;                //用户标识
    private String acctId;                //账户标识
    private String eparchyCode;           //地市编码
    private String provinceCode;          //省份编码

    //交易信息
    private String tradeId;                //省份交易流水
    private String chargeId;               //缴费流水
    private String inputChargeId;          //外围传入的缴费流水
    private String xOriginChargeId;       //原缴费流水
    private String tradeTime;              //交易时间
    private long tradeFee;               //交易金额
    private String channelId;              //渠道编码
    private int paymentId;                //储值方式
    private int paymentOp;               //操作方式
    private int payFeeModeCode;          //支付方式

    //账本相关信息字段
    private String limitMode;             //限额方式，默认是0
    private long limitMoney;            //限额，默认是-1
    private String depositStartDate;     //账本生效时间
    private int months;                   //账本有效账期数，默认是240
    private int amonths;                   //账本有效账期数，默认是0
    private int billStartCycleId;       //账本销账开始账期，默认是198001
    private int billEndCycleId;         //账本销账结束账期，默认是203001
    private String privateTag;            //账本公私有标识 0公有 1私有
    private String invoiceTag;            //是否增加可打金额 0不增加 1增加
    private long invoiceFee;            //可打金额
    private char validTag;                 //有效标识

    //特殊交易信息字段
    private String badTypeCode;           // 坏帐缴费标志
    private String isRound;               //是否规整缴费     0不规整，1规整到角，2规整到元  默认是0
    private String writeoffMode;          //缴费方式  2按用户缴费，其他是按照账户缴费，默认是1
    private String recoverTag;            //缴费复机 0不复机 1复机   默认是1
    private boolean fireCreditCtrl;      //是否触发信控 false不触发信控 true触发信控 默认是true
    private boolean smsType;             // 短信标志 false不发送短信 true发送短信 默认是true
    private String remark;                //备注
    private boolean isDepositRecv;   //选择账本缴费
    private String acctBalanceId;               //账本实例标识
    private int depositCode;                    //账本类型
    private String relChargeId;         //关联交易流水
    private String reasonCode;      //原因编码
    private boolean bigAcctRecvFee;   //大合帐用户缴费
    private String qryBillType;     //账单查询模式 默认交易查询 1欠费查询
    /**
     * @see #cancleTag 返销标识
     */
    private char cancleTag;
    /**
     * @see #batchDealTag 1代表批处理
     */
    private String batchDealTag;


    public TradeCommInfoIn() {
        removeTag = "0";
        limitMode = "0";
        limitMoney = -1;
        months = 240;
        amonths = 0;
        billStartCycleId = 198001;
        billEndCycleId = 203001;
        isRound = "0";
        writeoffMode = "1";
        recoverTag = "1";
        fireCreditCtrl = true;
        smsType = true;
        validTag = '0';
        depositCode = -1;
        cancleTag = '0';
    }

    public String getHeaderGray() {
        return headerGray;
    }

    public void setHeaderGray(String headerGray) {
        this.headerGray = headerGray;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getInputChargeId() {
        return inputChargeId;
    }

    public void setInputChargeId(String inputChargeId) {
        this.inputChargeId = inputChargeId;
    }

    public String getxOriginChargeId() {
        return xOriginChargeId;
    }

    public void setxOriginChargeId(String xOriginChargeId) {
        this.xOriginChargeId = xOriginChargeId;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public long getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(long tradeFee) {
        this.tradeFee = tradeFee;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentOp() {
        return paymentOp;
    }

    public void setPaymentOp(int paymentOp) {
        this.paymentOp = paymentOp;
    }

    public int getPayFeeModeCode() {
        return payFeeModeCode;
    }

    public void setPayFeeModeCode(int payFeeModeCode) {
        this.payFeeModeCode = payFeeModeCode;
    }

    public String getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(String limitMode) {
        this.limitMode = limitMode;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getDepositStartDate() {
        return depositStartDate;
    }

    public void setDepositStartDate(String depositStartDate) {
        this.depositStartDate = depositStartDate;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getAmonths() {
        return amonths;
    }

    public void setAmonths(int amonths) {
        this.amonths = amonths;
    }

    public int getBillStartCycleId() {
        return billStartCycleId;
    }

    public void setBillStartCycleId(int billStartCycleId) {
        this.billStartCycleId = billStartCycleId;
    }

    public int getBillEndCycleId() {
        return billEndCycleId;
    }

    public void setBillEndCycleId(int billEndCycleId) {
        this.billEndCycleId = billEndCycleId;
    }

    public String getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(String privateTag) {
        this.privateTag = privateTag;
    }

    public String getInvoiceTag() {
        return invoiceTag;
    }

    public void setInvoiceTag(String invoiceTag) {
        this.invoiceTag = invoiceTag;
    }

    public long getInvoiceFee() {
        return invoiceFee;
    }

    public void setInvoiceFee(long invoiceFee) {
        this.invoiceFee = invoiceFee;
    }

    public char getValidTag() {
        return validTag;
    }

    public void setValidTag(char validTag) {
        this.validTag = validTag;
    }

    public String getBadTypeCode() {
        return badTypeCode;
    }

    public void setBadTypeCode(String badTypeCode) {
        this.badTypeCode = badTypeCode;
    }

    public String getIsRound() {
        return isRound;
    }

    public void setIsRound(String isRound) {
        this.isRound = isRound;
    }

    public String getWriteoffMode() {
        return writeoffMode;
    }

    public void setWriteoffMode(String writeoffMode) {
        this.writeoffMode = writeoffMode;
    }

    public String getRecoverTag() {
        return recoverTag;
    }

    public void setRecoverTag(String recoverTag) {
        this.recoverTag = recoverTag;
    }

    public boolean isFireCreditCtrl() {
        return fireCreditCtrl;
    }

    public void setFireCreditCtrl(boolean fireCreditCtrl) {
        this.fireCreditCtrl = fireCreditCtrl;
    }

    public boolean isSmsType() {
        return smsType;
    }

    public void setSmsType(boolean smsType) {
        this.smsType = smsType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDepositRecv() {
        return isDepositRecv;
    }

    public void setDepositRecv(boolean depositRecv) {
        isDepositRecv = depositRecv;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public String getRelChargeId() {
        return relChargeId;
    }

    public void setRelChargeId(String relChargeId) {
        this.relChargeId = relChargeId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public boolean isBigAcctRecvFee() {
        return bigAcctRecvFee;
    }

    public void setBigAcctRecvFee(boolean bigAcctRecvFee) {
        this.bigAcctRecvFee = bigAcctRecvFee;
    }

    public String getQryBillType() {
        return qryBillType;
    }

    public void setQryBillType(String qryBillType) {
        this.qryBillType = qryBillType;
    }

    public char getCancleTag() {
        return cancleTag;
    }

    public void setCancleTag(char cancleTag) {
        this.cancleTag = cancleTag;
    }

    public String getBatchDealTag() {
        return batchDealTag;
    }

    public void setBatchDealTag(String batchDealTag) {
        this.batchDealTag = batchDealTag;
    }

    @Override
    public String toString() {
        return "TradeCommInfoIn{" +
                "headerGray='" + headerGray + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", removeTag='" + removeTag + '\'' +
                ", userId='" + userId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", inputChargeId='" + inputChargeId + '\'' +
                ", xOriginChargeId='" + xOriginChargeId + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", tradeFee=" + tradeFee +
                ", channelId='" + channelId + '\'' +
                ", paymentId=" + paymentId +
                ", paymentOp=" + paymentOp +
                ", payFeeModeCode=" + payFeeModeCode +
                ", limitMode='" + limitMode + '\'' +
                ", limitMoney=" + limitMoney +
                ", depositStartDate='" + depositStartDate + '\'' +
                ", months=" + months +
                ", amonths=" + amonths +
                ", billStartCycleId=" + billStartCycleId +
                ", billEndCycleId=" + billEndCycleId +
                ", privateTag='" + privateTag + '\'' +
                ", invoiceTag='" + invoiceTag + '\'' +
                ", invoiceFee=" + invoiceFee +
                ", validTag=" + validTag +
                ", badTypeCode='" + badTypeCode + '\'' +
                ", isRound='" + isRound + '\'' +
                ", writeoffMode='" + writeoffMode + '\'' +
                ", recoverTag='" + recoverTag + '\'' +
                ", fireCreditCtrl=" + fireCreditCtrl +
                ", smsType=" + smsType +
                ", remark='" + remark + '\'' +
                ", isDepositRecv=" + isDepositRecv +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", depositCode=" + depositCode +
                ", relChargeId='" + relChargeId + '\'' +
                ", reasonCode='" + reasonCode + '\'' +
                ", bigAcctRecvFee=" + bigAcctRecvFee +
                ", qryBillType='" + qryBillType + '\'' +
                ", cancleTag=" + cancleTag +
                ", batchDealTag='" + batchDealTag + '\'' +
                '}';
    }
}

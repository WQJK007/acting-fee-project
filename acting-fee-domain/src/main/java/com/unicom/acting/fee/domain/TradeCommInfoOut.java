package com.unicom.acting.fee.domain;

import java.util.List;

/**
 * 费用交易参数类，主要包括交易结果输出参数
 */
public class TradeCommInfoOut {
    private String serialNumber;    //交易服务号码
    private String netTypeCode;     //网别
    private String userId;          //交易用户标识
    private String acctId;          //交易账户标识
    private String payName;             //交易账户名称
    private String payModeCode;         //账户付费方式
    private int curCycleId;             //当前账期
    private int maxAcctCycleId;         //当前最大开账账期
    private String eparchyCode;         //账户归属地市
    private String provinceCode;        //交易省份编码
    private String chargeId;            //缴费流水
    private String payChargeId;         //缴费流水
    private String outerTradeId;        //外围缴费流水
    private String recvFee;             //交易金额
    private String extendTag;           //异地交易标识
    private String brandCode;           //用户品牌
    private String spayFee;             //交易后应缴金额
    private String allMoney;            //原预存款
    private String allNewMoney;         //现预存款
    private String allBalance;          //原实时结余
    private String allNewBalance;       //现实时结余
    private String aimpFee;             //原预存款销账金额
    private String allBOweFee;          //原欠费金额
    private String allNewBOweFee;       //现欠费金额
    private String preRealFee;          //上个账期实时费用
    private String curRealFee;          //当前账期实时费用
    private String allROweFee;          //实时费用
    private String conTactType;         //余额播报模式
    private String rsrvStr18;           //账户当前可用余额
    private String acctBalanceId1;     //交易账本
    private String rsrvDate;            //交易时间 格式YYYYMMDDHH24MISS
    private String resFee;              //账户冻结预存款
    private String bankFee;             //账本清退总金额
    private String transFee;            //账本可转总费用

    private String creditValue;         //信用度
    private UniBalanceInfo unifiedBalanceInfo;//统一余额对象
    private String newBalance;           //账户结余
    private String productType;          //手机合约用户类型
    private String transMoney;           //合约包用户该月返还话费
    private String compTag;              //是否合账用户
    private String boweTag;               //是否有欠费
    private String totalSpayFee;          //ECS接口应交费用
    private List<DetailDepositInfo> detailDepositInfos;   //账本明细信息
    private List<DetailBillInfo> detailBillInfos;   //账单明细信息
    private List<SubDetailBillInfo> subDetailBillInfos;//子账单明细
    private String oldFee = "0";//上上个月欠费
    private String intfFee01 = "0";//本月应交
    private String canConsignTag = "0";
    private String consignFee;
    private String lateFee;//总滞纳金
    private String specialfeeConTactType;         //专项款播报模式

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getConsignFee() {
        return consignFee;
    }

    public void setConsignFee(String consignFee) {
        this.consignFee = consignFee;
    }

    public String getCanConsignTag() {
        return canConsignTag;
    }

    public void setCanConsignTag(String canConsignTag) {
        this.canConsignTag = canConsignTag;
    }

    public String getOldFee() {
        return oldFee;
    }

    public void setOldFee(String oldFee) {
        this.oldFee = oldFee;
    }

    public String getIntfFee01() {
        return intfFee01;
    }

    public void setIntfFee01(String intfFee01) {
        this.intfFee01 = intfFee01;
    }

    public String getIntfFee02() {
        return intfFee02;
    }

    public void setIntfFee02(String intfFee02) {
        this.intfFee02 = intfFee02;
    }

    private String intfFee02 = "0";//逾期欠费


    public String getBankFee() {
        return bankFee;
    }

    public void setBankFee(String bankFee) {
        this.bankFee = bankFee;
    }

    public String getTransFee() {
        return transFee;
    }

    public void setTransFee(String transFee) {
        this.transFee = transFee;
    }

    public List<DetailDepositInfo> getDetailDepositInfos() {
        return detailDepositInfos;
    }

    public void setDetailDepositInfos(List<DetailDepositInfo> detailDepositInfos) {
        this.detailDepositInfos = detailDepositInfos;
    }

    public List<DetailBillInfo> getDetailBillInfos() {
        return detailBillInfos;
    }

    public void setDetailBillInfos(List<DetailBillInfo> detailBillInfos) {
        this.detailBillInfos = detailBillInfos;
    }

    public String getTotalSpayFee() {
        return totalSpayFee;
    }

    public void setTotalSpayFee(String totalSpayFee) {
        this.totalSpayFee = totalSpayFee;
    }

    public String getBoweTag() {
        return boweTag;
    }

    public void setBoweTag(String boweTag) {
        this.boweTag = boweTag;
    }

    public String getCompTag() {
        return compTag;
    }

    public void setCompTag(String compTag) {
        this.compTag = compTag;
    }

    public String getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(String transMoney) {
        this.transMoney = transMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode;
    }

    public int getCurCycleId() {
        return curCycleId;
    }

    public void setCurCycleId(int curCycleId) {
        this.curCycleId = curCycleId;
    }

    public int getMaxAcctCycleId() {
        return maxAcctCycleId;
    }

    public void setMaxAcctCycleId(int maxAcctCycleId) {
        this.maxAcctCycleId = maxAcctCycleId;
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

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getPayChargeId() {
        return payChargeId;
    }

    public void setPayChargeId(String payChargeId) {
        this.payChargeId = payChargeId;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
    }

    public String getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(String recvFee) {
        this.recvFee = recvFee;
    }

    public String getExtendTag() {
        return extendTag;
    }

    public void setExtendTag(String extendTag) {
        this.extendTag = extendTag;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getSpayFee() {
        return spayFee;
    }

    public void setSpayFee(String spayFee) {
        this.spayFee = spayFee;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getAllNewMoney() {
        return allNewMoney;
    }

    public void setAllNewMoney(String allNewMoney) {
        this.allNewMoney = allNewMoney;
    }

    public String getAllBalance() {
        return allBalance;
    }

    public void setAllBalance(String allBalance) {
        this.allBalance = allBalance;
    }

    public String getAllNewBalance() {
        return allNewBalance;
    }

    public void setAllNewBalance(String allNewBalance) {
        this.allNewBalance = allNewBalance;
    }

    public String getAimpFee() {
        return aimpFee;
    }

    public void setAimpFee(String aimpFee) {
        this.aimpFee = aimpFee;
    }

    public String getAllBOweFee() {
        return allBOweFee;
    }

    public void setAllBOweFee(String allBOweFee) {
        this.allBOweFee = allBOweFee;
    }

    public String getAllNewBOweFee() {
        return allNewBOweFee;
    }

    public void setAllNewBOweFee(String allNewBOweFee) {
        this.allNewBOweFee = allNewBOweFee;
    }

    public String getPreRealFee() {
        return preRealFee;
    }

    public void setPreRealFee(String preRealFee) {
        this.preRealFee = preRealFee;
    }

    public String getCurRealFee() {
        return curRealFee;
    }

    public void setCurRealFee(String curRealFee) {
        this.curRealFee = curRealFee;
    }

    public String getAllROweFee() {
        return allROweFee;
    }

    public void setAllROweFee(String allROweFee) {
        this.allROweFee = allROweFee;
    }

    public String getConTactType() {
        return conTactType;
    }

    public void setConTactType(String conTactType) {
        this.conTactType = conTactType;
    }

    public String getRsrvStr18() {
        return rsrvStr18;
    }

    public void setRsrvStr18(String rsrvStr18) {
        this.rsrvStr18 = rsrvStr18;
    }

    public String getAcctBalanceId1() {
        return acctBalanceId1;
    }

    public void setAcctBalanceId1(String acctBalanceId1) {
        this.acctBalanceId1 = acctBalanceId1;
    }

    public String getRsrvDate() {
        return rsrvDate;
    }

    public void setRsrvDate(String rsrvDate) {
        this.rsrvDate = rsrvDate;
    }

    public String getResFee() {
        return resFee;
    }

    public void setResFee(String resFee) {
        this.resFee = resFee;
    }

    public UniBalanceInfo getUnifiedBalanceInfo() {
        return unifiedBalanceInfo;
    }

    public void setUnifiedBalanceInfo(UniBalanceInfo unifiedBalanceInfo) {
        this.unifiedBalanceInfo = unifiedBalanceInfo;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(String newBalance) {
        this.newBalance = newBalance;
    }

    public List<SubDetailBillInfo> getSubDetailBillInfos() {
        return subDetailBillInfos;
    }

    public void setSubDetailBillInfos(List<SubDetailBillInfo> subDetailBillInfos) {
        this.subDetailBillInfos = subDetailBillInfos;
    }

    public String getSpecialfeeConTactType() {
        return specialfeeConTactType;
    }

    public void setSpecialfeeConTactType(String specialfeeConTactType) {
        this.specialfeeConTactType = specialfeeConTactType;
    }

    @Override
    public String toString() {
        return "TradeCommInfoOut{" +
                "serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", userId='" + userId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", payName='" + payName + '\'' +
                ", payModeCode='" + payModeCode + '\'' +
                ", curCycleId=" + curCycleId +
                ", maxAcctCycleId=" + maxAcctCycleId +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", payChargeId='" + payChargeId + '\'' +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", recvFee='" + recvFee + '\'' +
                ", extendTag='" + extendTag + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", spayFee='" + spayFee + '\'' +
                ", allMoney='" + allMoney + '\'' +
                ", allNewMoney='" + allNewMoney + '\'' +
                ", allBalance='" + allBalance + '\'' +
                ", allNewBalance='" + allNewBalance + '\'' +
                ", aimpFee='" + aimpFee + '\'' +
                ", allBOweFee='" + allBOweFee + '\'' +
                ", allNewBOweFee='" + allNewBOweFee + '\'' +
                ", preRealFee='" + preRealFee + '\'' +
                ", curRealFee='" + curRealFee + '\'' +
                ", allROweFee='" + allROweFee + '\'' +
                ", conTactType='" + conTactType + '\'' +
                ", rsrvStr18='" + rsrvStr18 + '\'' +
                ", acctBalanceId1='" + acctBalanceId1 + '\'' +
                ", rsrvDate='" + rsrvDate + '\'' +
                ", resFee='" + resFee + '\'' +
                ", bankFee='" + bankFee + '\'' +
                ", transFee='" + transFee + '\'' +
                ", creditValue='" + creditValue + '\'' +
                ", unifiedBalanceInfo=" + unifiedBalanceInfo +
                ", newBalance='" + newBalance + '\'' +
                ", productType='" + productType + '\'' +
                ", transMoney='" + transMoney + '\'' +
                ", compTag='" + compTag + '\'' +
                ", boweTag='" + boweTag + '\'' +
                ", totalSpayFee='" + totalSpayFee + '\'' +
                ", detailDepositInfos=" + detailDepositInfos +
                ", detailBillInfos=" + detailBillInfos +
                ", subDetailBillInfos=" + subDetailBillInfos +
                ", oldFee='" + oldFee + '\'' +
                ", intfFee01='" + intfFee01 + '\'' +
                ", canConsignTag='" + canConsignTag + '\'' +
                ", consignFee='" + consignFee + '\'' +
                ", lateFee='" + lateFee + '\'' +
                ", intfFee02='" + intfFee02 + '\'' +
                '}';
    }
}



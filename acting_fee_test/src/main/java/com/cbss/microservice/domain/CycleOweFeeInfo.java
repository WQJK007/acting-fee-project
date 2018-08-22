package com.cbss.microservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.act.framework.common.CbssPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分账期欠费信息
 *
 * @author shaob
 */
@JsonNaming(CbssPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CycleOweFeeInfo {
    @ApiModelProperty(name = "CYCLE_ID", value = "账期标识",required = true,example = "201805" )
    private String cycleId;
    @ApiModelProperty(name = "PAY_NAME", value = "发票客户名称" ,required = true,example = "张三")
    private String payName;
    @ApiModelProperty(name = "SERVICE_CLASS_CODE", value = "网别" ,required = true,example = "1000")
    private String serviceClassCode;
    @ApiModelProperty(name = "AREA_CODE", value = "长途区号", example = "0010" )
    private String areaCode;
    @ApiModelProperty(name = "SERIAL_NUMBER", value = "服务号码", required = true,example = "18612345678")
    private String serialNumber;
    @ApiModelProperty(name = "USER_ID", value = "用户标识", required = true,example = "11222111122212121")
    private String userId;
    @ApiModelProperty(name = "PROVINCE_CODE", value = "归属省份", required = true,example = "11")
    private String provinceCode;
    @ApiModelProperty(name = "EPARCHY_CODE", value = "归属地市", required = true,example = "0010")
    private String eparchyCode;
    @ApiModelProperty(name = "FEE", value = "该账期应收", required = true,example = "2000")
    private String fee;
    @ApiModelProperty(name = "RECEIVED_FEE", value = "已收费用", required = true,example = "1200")
    private String receivedFee;
    @ApiModelProperty(name = "LATE_FEE", value = "本次滞纳金合计", required = true,example = "1100")
    private String lateFee;
    @ApiModelProperty(name = "RECEIVED_LATE_FEE", value = "已收滞纳金", required = true,example = "11")
    private String receivedLateFee;
    @ApiModelProperty(name = "BALANCE", value = "本次欠费合计", required = true,example = "11")
    private String balance;
    @ApiModelProperty(name = "BAD_DEBT_TAG", value = "呆坏账标识", required = true,example = "0")
    private String badDebtTag;
    @ApiModelProperty(name = "BAD_DEBT_REMARK", value = "呆坏账描述", required = true,example = "呆坏账描述")
    private String badDebtRemark;
    @ApiModelProperty(name = "CONSIGN_TAG", value = "托收标识",example = "0")
    private String consignTag;
    @ApiModelProperty(name = "CONSIGN_REMARK", value = "托收标识",example = "11")
    private String consignRemark;
    @ApiModelProperty(name = "ITEM_INFO", value = "费用项明细" )
    private List<ItemInfo> itemInfo;

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getReceivedFee() {
        return receivedFee;
    }

    public void setReceivedFee(String receivedFee) {
        this.receivedFee = receivedFee;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getReceivedLateFee() {
        return receivedLateFee;
    }

    public void setReceivedLateFee(String receivedLateFee) {
        this.receivedLateFee = receivedLateFee;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBadDebtTag() {
        return badDebtTag;
    }

    public void setBadDebtTag(String badDebtTag) {
        this.badDebtTag = badDebtTag;
    }

    public String getBadDebtRemark() {
        return badDebtRemark;
    }

    public void setBadDebtRemark(String badDebtRemark) {
        this.badDebtRemark = badDebtRemark;
    }

    public String getConsignTag() {
        return consignTag;
    }

    public void setConsignTag(String consignTag) {
        this.consignTag = consignTag;
    }

    public String getConsignRemark() {
        return consignRemark;
    }

    public void setConsignRemark(String consignRemark) {
        this.consignRemark = consignRemark;
    }

    public List<ItemInfo> getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(List<ItemInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    @Override
    public String toString() {
        return "CycleOweFeeInfo{" +
                "cycleId='" + cycleId + '\'' +
                ", payName='" + payName + '\'' +
                ", serviceClassCode='" + serviceClassCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", userId='" + userId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", fee='" + fee + '\'' +
                ", receivedFee='" + receivedFee + '\'' +
                ", lateFee='" + lateFee + '\'' +
                ", receivedLateFee='" + receivedLateFee + '\'' +
                ", balance='" + balance + '\'' +
                ", badDebtTag='" + badDebtTag + '\'' +
                ", badDebtRemark='" + badDebtRemark + '\'' +
                ", consignTag='" + consignTag + '\'' +
                ", consignRemark='" + consignRemark + '\'' +
                ", itemInfo=" + itemInfo +
                '}';
    }
}

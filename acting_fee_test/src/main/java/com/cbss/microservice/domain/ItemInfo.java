package com.cbss.microservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.act.framework.common.CbssPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

/**
 * 费用项明细
 *
 * @author shaob
 */
@JsonNaming(CbssPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemInfo {
    @ApiModelProperty(name = "INTEGRATE_ITEM_CODE", value = "账目编码",required = true,example = "10010")
    private String integrateItemCode;
    @ApiModelProperty(name = "INTEGRATE_ITEM", value = "账目科目名称",required = true,example = "通话费" )
    private String integrateItem;
    @ApiModelProperty(name = "UPPER_ACCTITEM_CODE", value = "上级账目科目编码",example = "1000")
    private String upperAcctitemCode;
    @ApiModelProperty(name = "INTEGRATE_ITEM_CODE_TYPE", value = "科目类型：0:主业、1：非主业" ,required = true, example = "0")
    private String integrateItemCodeType;
    @ApiModelProperty(name = "FEE", value = "应收金额", required = true,example = "122")
    private String fee;
    @ApiModelProperty(name = "RECEIVED_FEE", value = "已收金额", required = true,example = "200")
    private String receivedFee;
    @ApiModelProperty(name = "BALANCE", value = "本次欠费", required = true,example = "200")
    private String balance;

    public String getIntegrateItemCode() {
        return integrateItemCode;
    }

    public void setIntegrateItemCode(String integrateItemCode) {
        this.integrateItemCode = integrateItemCode;
    }

    public String getIntegrateItem() {
        return integrateItem;
    }

    public void setIntegrateItem(String integrateItem) {
        this.integrateItem = integrateItem;
    }

    public String getUpperAcctitemCode() {
        return upperAcctitemCode;
    }

    public void setUpperAcctitemCode(String upperAcctitemCode) {
        this.upperAcctitemCode = upperAcctitemCode;
    }

    public String getIntegrateItemCodeType() {
        return integrateItemCodeType;
    }

    public void setIntegrateItemCodeType(String integrateItemCodeType) {
        this.integrateItemCodeType = integrateItemCodeType;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

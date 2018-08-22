package com.cbss.microservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.act.framework.common.CbssPropertyNamingStrategy;
import io.swagger.annotations.ApiModelProperty;

/**
 * 欠费查询-应答
 *
 * @author shaob
 */
@JsonNaming(CbssPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class QryOweFeeInfoRsp {
    @ApiModelProperty(name = "OWE_FEE_INFO", value = "欠费信息")
    private OweFeeInfo oweFeeInfo;

    public OweFeeInfo getOweFeeInfo() {
        return oweFeeInfo;
    }

    public void setOweFeeInfo(OweFeeInfo oweFeeInfo) {
        this.oweFeeInfo = oweFeeInfo;
    }

    @Override
    public String toString() {
        return "QryOweFeeInfoRsp{" +
                "oweFeeInfo=" + oweFeeInfo +
                '}';
    }
}

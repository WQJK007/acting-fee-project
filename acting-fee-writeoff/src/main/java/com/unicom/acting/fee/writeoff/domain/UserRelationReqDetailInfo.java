package com.unicom.acting.fee.writeoff.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 融合类型编码
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRelationReqDetailInfo {
    private String realtionTypeCode;

    public String getRealtionTypeCode() {
        return realtionTypeCode;
    }

    public void setRealtionTypeCode(String realtionTypeCode) {
        this.realtionTypeCode = realtionTypeCode;
    }

    @Override
    public String toString() {
        return "RelationTypeInfo{" +
                "realtionTypeCode='" + realtionTypeCode + '\'' +
                '}';
    }
}

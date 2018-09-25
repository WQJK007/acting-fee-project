package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 销账规则服务
 * 主要包括销账规则参数加载，获取交易账户的销账规则参数
 *
 * @author Administrators
 */
public interface WriteOffRuleService extends IBaseService {
    /**
     * 销账参数初始化
     */
    void loadWriteOffParam();

    /**
     * 返销参数加载
     */
    void CancelFeeloadParam();

    /**
     * 根据地市，省份和网别获取对应的销账规则
     *
     * @param writeOffRuleInfo
     * @param eparchyCode
     * @param provinceCode
     * @param netTypeCode
     */
    void getWriteOffRule(WriteOffRuleInfo writeOffRuleInfo, String eparchyCode, String provinceCode, String netTypeCode);
}

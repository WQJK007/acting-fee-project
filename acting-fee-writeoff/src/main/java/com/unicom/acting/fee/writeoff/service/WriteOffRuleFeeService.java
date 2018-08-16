package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 销账规则参数加载服务
 *
 * @author Administrators
 */
public interface WriteOffRuleFeeService extends IBaseService {
    /**
     * 销账参数初始化
     *
     * @param provinceCode
     */
    void loadWriteOffParam(String provinceCode);

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

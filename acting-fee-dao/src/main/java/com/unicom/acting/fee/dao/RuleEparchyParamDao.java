package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.LateCalPara;
import com.unicom.acting.fee.domain.RuleEparchy;

import java.util.List;

/**
 * 地市规则配置参数表相关数据操作，主要包括TD_B_RULE_EPARCHY和TD_B_LATECALPARA表查询
 *
 * @author Wangkh
 */
public interface RuleEparchyParamDao extends IBaseDao {
    /**
     * 获取地市销账规则全集
     *
     * @param provinceCode
     * @return
     */
    List<RuleEparchy> getRuleEparchy(String provinceCode);

    /**
     * 获取滞纳金计算规则全集
     *
     * @param provinceCode
     * @return
     */
    List<LateCalPara> getLateCalPara(String provinceCode);
}

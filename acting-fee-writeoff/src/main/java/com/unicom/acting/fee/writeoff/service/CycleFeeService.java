package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.Cycle;

/**
 * 账期类参数相关操作
 *
 * @author Administrators
 */
public interface CycleFeeService extends IBaseService {
    /**
     * 获取地市当前账期
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    Cycle getCurCycle(String eparchyCode, String provinceCode);

    /**
     * 获取地市当前账期从缓存中读取
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    Cycle getCacheCurCycle(String eparchyCode, String provinceCode);

    /**
     * 获取地市当前最大开账账期从缓存中读取
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    Cycle getCacheMaxAcctCycle(String eparchyCode, String provinceCode);

    /**
     * 获取地市当前最大开账账期
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    Cycle getMaxAcctCycle(String eparchyCode, String provinceCode);

    /**
     * 抵扣期
     *
     * @param curCycle
     * @return
     */
    boolean isDrecvPeriod(Cycle curCycle);

    /**
     * 补收期
     *
     * @param curCycle
     * @return
     */
    boolean isPatchDrecvPeriod(Cycle curCycle);


}

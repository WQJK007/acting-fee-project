package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.Cycle;

/**
 * 账期类参数相关操作
 *
 * @author wangkh
 */
public interface CycleService extends IBaseService {
    /**
     * 获取地市当前账期
     *
     * @param eparchyCode
     * @return
     */
    Cycle getCurCycle(String eparchyCode);

    /**
     * 获取地市当前账期从缓存中读取
     *
     * @param eparchyCode
     * @return
     */
    Cycle getCacheCurCycle(String eparchyCode);

    /**
     * 获取地市当前最大开账账期从缓存中读取
     *
     * @param eparchyCode
     * @return
     */
    Cycle getCacheMaxAcctCycle(String eparchyCode);

    /**
     * 获取地市当前最大开账账期
     *
     * @param eparchyCode
     * @return
     */
    Cycle getMaxAcctCycle(String eparchyCode);

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

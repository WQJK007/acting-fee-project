package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.Cycle;

import java.util.List;

/**
 * 账期相关参数表数据查询
 * 现在主要包括TD_B_CYCLE，TD_B_CYCLE_EPARCHY表查询操作
 *
 * @author Wangkh
 */
public interface CycleDao extends IBaseDao {
    /**
     * 获取账期参数全集
     *
     * @return
     */
    List<Cycle> getCycle();

    /**
     * 获取当前账期
     *
     * @param eparchyCode
     * @return
     */
    Cycle getCurCycle(String eparchyCode);

    /**
     * 获取当前最大开账账期
     *
     * @param eparchyCode
     * @return
     */
    Cycle getMaxAcctCycle(String eparchyCode);
}

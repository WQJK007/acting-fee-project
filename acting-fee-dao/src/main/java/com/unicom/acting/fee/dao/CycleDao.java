package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.Cycle;

import java.util.List;

/**
 * 账期相关参数表数据操作，现在主要包括TD_B_CYCLE，TD_B_CYCLE_EPARCHY表查询操作
 *
 * @author Wangkh
 */
public interface CycleDao extends IBaseDao {
    //获取账期参数全集
    List<Cycle> getCycle(String provinceCode);

    //获取当前账期
    Cycle getCurCycle(String eparchyCode, String provinceCode);

    //获取当前最大开账账期
    Cycle getMaxAcctCycle(String eparchyCode, String provinceCode);
}

package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.DerateLateFeeLog;

import java.util.List;

/**
 * TF_B_DERATELATEFEELOG表相关的数据操作
 *
 * @author Wangkh
 */
public interface DerateLateFeeLogFeeDao extends IBaseDao {
    /**
     * 获取滞纳金减免工单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    List<DerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId, String provinceCode);
}

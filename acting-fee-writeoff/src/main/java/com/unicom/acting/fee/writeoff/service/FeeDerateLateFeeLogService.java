package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.domain.FeeDerateLateFeeLog;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 滞纳金减免日志资源操作
 *
 * @author Wangkh
 */
public interface FeeDerateLateFeeLogService extends IBaseService {
    /**
     * 后期账户滞纳金减免工单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @return
     */
    List<FeeDerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId);
}

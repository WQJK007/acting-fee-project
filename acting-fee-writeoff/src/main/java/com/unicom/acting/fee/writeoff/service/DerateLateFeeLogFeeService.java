package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.DerateLateFeeLog;

import java.util.List;

/**
 * 滞纳金减免日志资源操作
 *
 * @author Wangkh
 */
public interface DerateLateFeeLogFeeService extends IBaseService {
    /**
     * 后期账户滞纳金减免工单
     *
     * @param acctId
     * @param startCycleId
     * @param endCycleId
     * @param provinceCode
     * @return
     */
    List<DerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId, String provinceCode);
}

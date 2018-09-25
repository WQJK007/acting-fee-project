package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.FeeDerateLateFeeLogDao;
import com.unicom.acting.fee.domain.FeeDerateLateFeeLog;
import com.unicom.acting.fee.writeoff.service.FeeDerateLateFeeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 滞纳金减免日志资源通过JDBC方式操作
 */
@Service
public class FeeDerateLateFeeLogServiceImpl implements FeeDerateLateFeeLogService {
    @Autowired
    private FeeDerateLateFeeLogDao feeDerateLateFeeLogDao;

    @Override
    public List<FeeDerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId) {
        return feeDerateLateFeeLogDao.getDerateLateFeeLog(acctId, startCycleId, endCycleId);
    }
}

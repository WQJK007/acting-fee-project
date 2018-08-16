package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.DerateLateFeeLogFeeDao;
import com.unicom.acting.fee.domain.DerateLateFeeLog;
import com.unicom.acting.fee.writeoff.service.DerateLateFeeLogFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 滞纳金减免日志资源通过JDBC方式操作
 */
@Service
public class DerateLateFeeLogFeeServiceImpl implements DerateLateFeeLogFeeService {
    @Autowired
    private DerateLateFeeLogFeeDao derateLateFeeLogFeeDao;

    @Override
    public List<DerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        return derateLateFeeLogFeeDao.getDerateLateFeeLog(acctId, startCycleId, endCycleId, provinceCode);
    }
}

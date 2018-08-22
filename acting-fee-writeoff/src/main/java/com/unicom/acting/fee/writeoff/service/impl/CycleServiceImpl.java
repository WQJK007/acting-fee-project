package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.CycleDao;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.fee.writeoff.service.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 账期类参数相关操作,通过JDBC方式访问数据库
 *
 * @author Administrators
 */
@Service
public class CycleServiceImpl implements CycleService {
    @Autowired
    private CycleDao cycleDao;


    @Override
    public Cycle getCurCycle(String eparchyCode, String provinceCode) {
        return cycleDao.getCurCycle(eparchyCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_getcurcycle")
    public Cycle getCacheCurCycle(String eparchyCode, String provinceCode) {
        return cycleDao.getCurCycle(eparchyCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_getmaxcycle")
    public Cycle getCacheMaxAcctCycle(String eparchyCode, String provinceCode) {
        return cycleDao.getMaxAcctCycle(eparchyCode, provinceCode);
    }

    @Override
    public Cycle getMaxAcctCycle(String eparchyCode, String provinceCode) {
        return cycleDao.getMaxAcctCycle(eparchyCode, provinceCode);
    }

    @Override
    public boolean isDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 5 || getMonthAcctStatus(curCycle) < 0
                || getMonthAcctStatus(curCycle) >= 20 && getMonthAcctStatus(curCycle) < 90);
    }

    @Override
    public boolean isPatchDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 8 || getMonthAcctStatus(curCycle) == 90);
    }

    /**
     * 当前账期状态
     *
     * @param curCycle
     * @return
     */
    private long getMonthAcctStatus(Cycle curCycle) {
        return Long.valueOf(curCycle.getMonthAcctStatus());
    }
}

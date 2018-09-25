package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.CycleDao;
import com.unicom.acting.fee.domain.ActingFeeCommparaDef;
import com.unicom.acting.fee.domain.CommPara;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.fee.writeoff.service.CycleService;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 账期类参数相关操作,通过JDBC方式访问数据库
 *
 * @author wangkh
 */
@Service
public class CycleServiceImpl implements CycleService {
    @Autowired
    private CycleDao cycleDao;


    @Override
    public Cycle getCurCycle(String eparchyCode) {
        return cycleDao.getCurCycle(eparchyCode);
    }

    @Override
    @Cacheable(value = "actingfee_getcurcycle")
    public Cycle getCacheCurCycle(String eparchyCode) {
        return cycleDao.getCurCycle(eparchyCode);
    }

    @Override
    @Cacheable(value = "actingfee_getmaxcycle")
    public Cycle getCacheMaxAcctCycle(String eparchyCode) {
        return cycleDao.getMaxAcctCycle(eparchyCode);
    }

    @Override
    public Cycle getMaxAcctCycle(String eparchyCode) {
        return cycleDao.getMaxAcctCycle(eparchyCode);
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

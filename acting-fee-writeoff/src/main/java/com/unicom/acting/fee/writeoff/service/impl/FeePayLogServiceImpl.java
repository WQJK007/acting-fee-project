package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.FeePayLogDmnDao;
import com.unicom.acting.fee.domain.FeeDiscntDeposit;
import com.unicom.acting.fee.domain.FeePayLogDmn;
import com.unicom.acting.fee.writeoff.service.FeePayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表通过JDBC方式操作
 *
 * @author Administrators
 */
@Service
public class FeePayLogServiceImpl implements FeePayLogService {
    @Autowired
    private FeePayLogDmnDao feePayLogDmnDao;

    @Override
    public List<FeePayLogDmn> getPayLogDmnByAcctId(String acctId, String getMode, String provinceCode) {
        return feePayLogDmnDao.getPaylogDmnByAcctId(acctId, getMode, provinceCode);
    }

    @Override
    public List<FeeDiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId) {
        return feePayLogDmnDao.getUserDiscntDepositByUserId(acctId, userId, provinceId);
    }

}

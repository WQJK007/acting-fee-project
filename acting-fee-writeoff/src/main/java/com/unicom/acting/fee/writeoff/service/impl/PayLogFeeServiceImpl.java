package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.PayLogFeeDao;
import com.unicom.acting.fee.domain.DiscntDeposit;
import com.unicom.acting.fee.domain.PayLogDmn;
import com.unicom.acting.fee.writeoff.service.PayLogFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表通过JDBC方式操作
 *
 * @author Administrators
 */
@Service
public class PayLogFeeServiceImpl implements PayLogFeeService {
    @Autowired
    private PayLogFeeDao payLogFeeDao;

    @Override
    public List<PayLogDmn> getPayLogDmnByAcctId(String acctId, String getMode, String provinceCode) {
        return payLogFeeDao.getPaylogDmnByAcctId(acctId, getMode, provinceCode);
    }

    @Override
    public List<DiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId) {
        return payLogFeeDao.getUserDiscntDepositByUserId(acctId, userId, provinceId);
    }

}

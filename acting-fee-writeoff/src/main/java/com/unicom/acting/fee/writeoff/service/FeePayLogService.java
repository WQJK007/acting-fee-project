package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.FeeDiscntDeposit;
import com.unicom.acting.fee.domain.FeePayLogDmn;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表资源操作
 *
 * @author Administrators
 */
public interface FeePayLogService extends IBaseService {
    /**
     * 根据账户标识查询账务后台工单表记录
     *
     * @param acctId
     * @param getMode
     * @param dbType
     * @param provinceCode
     * @return
     */
    List<FeePayLogDmn> getPayLogDmnByAcctId(String acctId, String getMode, String dbType, String provinceCode);

    /**
     * 用户活动实例
     *
     * @param acctId
     * @param userId
     * @return
     */
    List<FeeDiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId);
}

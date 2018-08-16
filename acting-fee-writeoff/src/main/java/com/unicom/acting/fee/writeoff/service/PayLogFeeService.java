package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.DiscntDeposit;
import com.unicom.acting.fee.domain.PayLogDmn;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表资源操作
 *
 * @author Administrators
 */
public interface PayLogFeeService extends IBaseService {
    /**
     * 根据账户标识查询账务后台工单表记录
     *
     * @param acctId       账户标识
     * @param getMode      记录处理状态
     * @param provinceCode 账户归属省份编码
     * @return 账务后台工单结果集
     */
    List<PayLogDmn> getPayLogDmnByAcctId(String acctId, String getMode, String provinceCode);

    /**
     * 用户活动实例
     *
     * @param acctId
     * @param userId
     * @param provinceId
     * @return
     */
    List<DiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId);
}

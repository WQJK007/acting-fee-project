package com.unicom.acting.fee.dao;

import com.unicom.acting.fee.domain.FeeAcctPaymentCycle;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 账户资料相关查询
 *
 * @author Wangkh
 */
public interface FeeAccountDao extends IBaseDao {
    /**
     * 账户自定义缴费期
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    FeeAcctPaymentCycle getAcctPaymentCycle(String acctId, String provinceCode);
}

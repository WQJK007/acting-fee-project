package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 系统常用公共操作方法
 *
 * @author Administrators
 */
public interface SysCommOperFeeService extends IBaseService {
    /**
     * 获取系统时间
     *
     * @param fmt
     * @return
     */
    String getSysdate(String fmt);

    /**
     * 获取30位的交易流水
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    String getTradeSequence(String eparchyCode, String provinceCode);

    /**
     * 获取单条流水
     *
     * @param eparchyCode
     * @param seqName
     * @param provinceCode
     * @return
     */
    String getSequence(String eparchyCode, String seqName, String provinceCode);

    /**
     * 获取指定数量的流水集
     *
     * @param eparchyCode
     * @param seqName
     * @param preCount
     * @param provinceCode
     * @return
     */
    List<String> getSequence(String eparchyCode, String seqName, long preCount, String provinceCode);
}

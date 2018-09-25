package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 系统常用公共操作方法
 *
 * @author Wangkh
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

    /**
     * 获取账户单条流水
     *
     * @param tabName
     * @param columnName
     * @param provinceCode
     * @return
     */
    String getActsSequence(String tabName, String columnName, String provinceCode);

    /**
     * 获取账户中心多条流水
     *
     * @param tabName
     * @param columnName
     * @param seqCount
     * @param provinceCode
     * @return
     */
    List<String> getActsSequence(String tabName, String columnName, int seqCount, String provinceCode);

    /**
     * 获取账务中心单条流水
     *
     * @param tabName
     * @param columnName
     * @param provinceCode
     * @return
     */
    String getActingSequence(String tabName, String columnName, String provinceCode);

    /**
     * 获取账务中心多条流水
     *
     * @param tabName
     * @param columnName
     * @param seqCount
     * @param provinceCode
     * @return
     */
    List<String> getActingSequence(String tabName, String columnName, int seqCount, String provinceCode);


}

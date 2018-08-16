package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.CommPara;

import java.util.List;

/**
 * 帐务通用参数相关数据库操作，现在包括TD_B_COMMPARA表查询操作
 *
 * @author Wangkh
 */
public interface CommparaFeeDao extends IBaseDao {
    /**
     * 获取公共参数全集
     *
     * @param provinceCode
     * @return
     */
    List<CommPara> getCommpara(String provinceCode);

    /**
     * 查询时间戳
     *
     * @param tagCode
     * @param provinceCode
     * @return
     */
    String getParamTimeStamp(String tagCode, String provinceCode);

    /**
     * 查询账管公共参数表参数配置
     *
     * @param paraCode
     * @param provinceCode
     * @param eparchyCode
     * @param provinceId
     * @return
     */
    CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode, String provinceId);

    /**
     * 提供LIKE方式查询账管公共参数表参数配置
     *
     * @param paraCode
     * @param provinceCode
     * @param eparchyCode
     * @param provinceId
     * @return
     */
    CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode, String provinceId);

    /**
     * 根据地市编码查询地市归属省份编码
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    String getProvCodeByEparchyCode(String eparchyCode, String provinceCode);

    /**
     * 查询网别类型的父级类型是移网或固网
     *
     * @param netTypeCode
     * @param provinceCode
     * @return
     */
    String getParentTypeCode(String netTypeCode, String provinceCode);
}

package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.CommPara;

import java.util.List;

/**
 * 帐务通用参数相关数据库操作，现在包括TD_B_COMMPARA表查询操作
 *
 * @author Wangkh
 */
public interface CommparaDao extends IBaseDao {
    /**
     * 获取公共参数全集
     *
     * @return
     */
    List<CommPara> getCommpara();

    /**
     * 查询时间戳
     *
     * @param tagCode
     * @return
     */
    String getParamTimeStamp(String tagCode);

    /**
     * 查询账管公共参数表参数配置
     *
     * @param paraCode
     * @param provinceCode
     * @param eparchyCode
     * @return
     */
    CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode);

    /**
     * 提供LIKE方式查询账管公共参数表参数配置
     *
     * @param paraCode
     * @param provinceCode
     * @param eparchyCode
     * @return
     */
    CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode);

    /**
     * 根据地市编码查询地市归属省份编码
     *
     * @param eparchyCode
     * @return
     */
    String getProvCodeByEparchyCode(String eparchyCode);

    /**
     * 查询网别类型的父级类型是移网或固网
     *
     * @param netTypeCode
     * @return
     */
    String getParentTypeCode(String netTypeCode);
}

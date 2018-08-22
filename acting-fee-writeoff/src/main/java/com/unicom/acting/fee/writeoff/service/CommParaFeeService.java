package com.unicom.acting.fee.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.CommPara;

/**
 * 帐务常用参数公共操作，主要包括以下数据表
 * TD_B_COMMPARA,TD_B_EPARCHY,TD_S_NETCODE
 *
 * @author wangkh
 */
public interface CommParaFeeService extends IBaseService {
    /**
     * 获取时间戳
     *
     * @param tagCode
     * @param provinceCode
     * @return
     */
    String getParamTimeStamp(String tagCode, String provinceCode);

    /**
     * 根据编码获取参数对象
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
     * 网别类型
     *
     * @param netTypeCode
     * @param provinceCode
     * @return
     */
    String getParentTypeCode(String netTypeCode, String provinceCode);

    /**
     * 获取地市对应的省份编码
     *
     * @param eparchyCode
     * @param provinceCode
     * @return
     */
    String getProvCodeByEparchyCode(String eparchyCode, String provinceCode);
}

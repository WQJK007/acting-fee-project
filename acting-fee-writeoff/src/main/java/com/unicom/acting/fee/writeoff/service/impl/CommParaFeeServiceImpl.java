package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.CommparaDao;
import com.unicom.acting.fee.domain.CommPara;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 帐务通用参数公共通过JDBC操作
 *
 * @author Administrators
 */
@Service
public class CommParaFeeServiceImpl implements CommParaFeeService {
    @Autowired
    private CommparaDao commparaDao;

    @Override
    public String getParamTimeStamp(String tagCode, String provinceCode) {
        return commparaDao.getParamTimeStamp(tagCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_getcommpara")
    public CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        return commparaDao.getCommpara(paraCode, provinceCode, eparchyCode, provinceId);
    }

    @Override
    public CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        return commparaDao.getCommparaByLike(paraCode, provinceCode, eparchyCode, provinceId);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_nettypeparentcode")
    public String getParentTypeCode(String netTypeCode, String provinceCode) {
        return commparaDao.getParentTypeCode(netTypeCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_provcodebyeparchy")
    public String getProvCodeByEparchyCode(String eparchyCode, String provinceCode) {
        return commparaDao.getProvCodeByEparchyCode(eparchyCode, provinceCode);
    }
}

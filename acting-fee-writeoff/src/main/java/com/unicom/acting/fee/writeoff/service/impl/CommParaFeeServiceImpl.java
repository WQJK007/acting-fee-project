package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.fee.dao.CommparaFeeDao;
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
    private CommparaFeeDao commparaFeeDao;

    @Override
    public String getParamTimeStamp(String tagCode, String provinceCode) {
        return commparaFeeDao.getParamTimeStamp(tagCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_getcommpara")
    public CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        return commparaFeeDao.getCommpara(paraCode, provinceCode, eparchyCode, provinceId);
    }

    @Override
    public CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        return commparaFeeDao.getCommparaByLike(paraCode, provinceCode, eparchyCode, provinceId);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_nettypeparentcode")
    public String getParentTypeCode(String netTypeCode, String provinceCode) {
        return commparaFeeDao.getParentTypeCode(netTypeCode, provinceCode);
    }

    @Override
    @Cacheable(value = "actingfeewriteoff_provcodebyeparchy")
    public String getProvCodeByEparchyCode(String eparchyCode, String provinceCode) {
        return commparaFeeDao.getProvCodeByEparchyCode(eparchyCode, provinceCode);
    }
}

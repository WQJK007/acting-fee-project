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
    public String getParamTimeStamp(String tagCode) {
        return commparaDao.getParamTimeStamp(tagCode);
    }

    @Override
    @Cacheable(value = "actingfee_getcommpara")
    public CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode) {
        return commparaDao.getCommpara(paraCode, provinceCode, eparchyCode);
    }

    @Override
    public CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode) {
        return commparaDao.getCommparaByLike(paraCode, provinceCode, eparchyCode);
    }

    @Override
    @Cacheable(value = "actingfee_netparentcode")
    public String getParentTypeCode(String netTypeCode) {
        return commparaDao.getParentTypeCode(netTypeCode);
    }

    @Override
    @Cacheable(value = "actingfee_provcode")
    public String getProvCodeByEparchyCode(String eparchyCode) {
        return commparaDao.getProvCodeByEparchyCode(eparchyCode);
    }
}

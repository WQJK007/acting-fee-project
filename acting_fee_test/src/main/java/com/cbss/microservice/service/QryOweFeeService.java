package com.cbss.microservice.service;

import com.cbss.microservice.domain.QryOweFeeInfoRsp;
import com.unicom.act.framework.exception.CbssException;
import com.unicom.act.framework.service.IBaseService;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

import java.util.List;

/**
 * 欠费查询微服务
 *
 * @author shaob
 */
public interface QryOweFeeService extends IBaseService {

    /**
     * 调用欠费核心微服务
     *
     * @param tradeCommInfoIn
     * @return
     */
    List<QryOweFeeInfoRsp> qryOweFeeInfo(TradeCommInfoIn tradeCommInfoIn)  throws CbssException;


}

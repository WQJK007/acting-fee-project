package com.unicom.acting.fee.writeoff.service;

import com.unicom.acting.fee.writeoff.domain.*;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 三户资料，用户属性，融合关系查询服务
 * 需要调动用户和客户中心微服务实现功能
 *
 * @author Wangkh
 */
public interface DatumParamRelService extends IBaseService {
    /**
     * 调用账务中心微服务查询三户资料
     *
     * @param tradeCommInfoIn
     * @return
     */
    UserDatumInfo getUserDatum(TradeCommInfoIn tradeCommInfoIn);

    /**
     * 用户属性获取
     *
     * @param detailInfos
     * @param headerGray
     * @return
     */
    List<UserParamRsp> getUserParam(List<UserParamReqDetailInfo> detailInfos, String headerGray);


    /**
     * 用户融合关系查询
     *
     * @param userId
     * @param memberRoleId
     * @param islike
     * @param relationTypes
     * @param headerGray
     * @return
     */
    List<UserRelationRsp> getUserRelation(String userId, String memberRoleId,
                                          boolean islike, List<UserRelationReqDetailInfo> relationTypes,
                                          String headerGray);


}

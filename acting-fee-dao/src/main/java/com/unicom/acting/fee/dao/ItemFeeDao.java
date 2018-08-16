package com.unicom.acting.fee.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.CompItem;
import com.unicom.acting.fee.domain.DetailItem;
import com.unicom.acting.fee.domain.ItemPriorRule;

import java.util.List;

/**
 * 项目项相关参数表数据库操作，
 * 主要包括TD_B_DETAILITEM,TD_B_COMPITEM,TD_B_ITEMPRIORRULE表的查询
 *
 * @author Wangkh
 */
public interface ItemFeeDao extends IBaseDao {
    //获取明细账目项全集
    List<DetailItem> getDetailItem(String provinceCode);

    //获取组合账目项全集
    List<CompItem> getCompItem(String provinceCode);

    //获取账目项优先级全集
    List<ItemPriorRule> getItemPriorRule(String provinceCode);
}

package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.ItemParamDao;
import com.unicom.acting.fee.domain.CompItem;
import com.unicom.acting.fee.domain.DetailItem;
import com.unicom.acting.fee.domain.ItemPriorRule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ItemParamDaoImpl extends JdbcBaseDao implements ItemParamDao {
    public List<DetailItem> getDetailItem(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ITEM_ID,ITEM_NAME,ITEM_USE_TYPE,ADDUP_ELEM_TYPE,ITEM_CLASS,");
        sql.append("OWE_TAG,LATEFEE_CALC_TAG FROM TD_B_DETAILITEM");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new DetailItemMapper());
    }

    public List<CompItem> getCompItem(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ITEM_ID,SUB_ITEM_ID,SUB_ITEM_NO FROM TD_B_COMPITEM");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new CompItemMapper());
    }

    public List<ItemPriorRule> getItemPriorRule(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ITEM_PRIOR_RULE_ID,ITEM_CODE,ITEM_PRIORITY FROM TD_B_ITEMPRIORRULE");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new ItemPriorRuleMapper());
    }

    //明细账目项结果集类
    class DetailItemMapper implements RowMapper<DetailItem> {
        @Override
        public DetailItem mapRow(ResultSet resultSet, int i) throws SQLException {
            DetailItem detailItem = new DetailItem();
            detailItem.setItemId(resultSet.getInt("ITEM_ID"));
            detailItem.setItemName(resultSet.getString("ITEM_NAME"));
            detailItem.setItemClass(StringUtil.firstOfString(resultSet.getString("ITEM_CLASS")));
            detailItem.setOweTag(StringUtil.firstOfString(resultSet.getString("OWE_TAG")));
            detailItem.setLatefeeCalcTag(StringUtil.firstOfString(resultSet.getString("LATEFEE_CALC_TAG")));
            return detailItem;
        }
    }

    //组合账目项结果集类
    class CompItemMapper implements RowMapper<CompItem> {
        @Override
        public CompItem mapRow(ResultSet resultSet, int i) throws SQLException {
            CompItem compItem = new CompItem();
            compItem.setItemId(resultSet.getInt("ITEM_ID"));
            compItem.setSubItemId(resultSet.getInt("SUB_ITEM_ID"));
            compItem.setSubItemNo(resultSet.getInt("SUB_ITEM_NO"));
            return compItem;
        }
    }

    //账目项优先级结果集类
    class ItemPriorRuleMapper implements RowMapper<ItemPriorRule> {
        @Override
        public ItemPriorRule mapRow(ResultSet resultSet, int i) throws SQLException {
            ItemPriorRule itemPriorRule = new ItemPriorRule();
            itemPriorRule.setItemPriorRuleId(resultSet.getInt("ITEM_PRIOR_RULE_ID"));
            itemPriorRule.setItemCode(resultSet.getInt("ITEM_CODE"));
            itemPriorRule.setItemPriority(resultSet.getInt("ITEM_PRIORITY"));
            return itemPriorRule;
        }
    }
}

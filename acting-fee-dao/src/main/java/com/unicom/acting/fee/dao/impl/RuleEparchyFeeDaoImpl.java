package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.RuleEparchyFeeDao;
import com.unicom.acting.fee.domain.LateCalPara;
import com.unicom.acting.fee.domain.RuleEparchy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RuleEparchyFeeDaoImpl extends JdbcBaseDao implements RuleEparchyFeeDao {
    @Override
    public List<RuleEparchy> getRuleEparchy(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT RULE_ID,PROVINCE_CODE,EPARCHY_CODE,");
        sql.append("NET_TYPE_CODE,RULE_TYPE,COND_DESC FROM TD_B_RULE_EPARCHY");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new RuleEparchyMapper());
    }

    @Override
    public List<LateCalPara> getLateCalPara(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT RULE_ID,START_CYCLE_ID,END_CYCLE_ID,LATE_FEE_RATIO1,LATE_FEE_RATIO2,");
        sql.append("INI_CAL_FEE,MAX_LATE_FEE,MAX_DAY_NUM,INI_DAYS FROM TD_B_LATECALPARA");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new LateCalParaRowMapper());
    }

    //销账规则结果集类
    private class RuleEparchyMapper implements RowMapper<RuleEparchy> {
        @Override
        public RuleEparchy mapRow(ResultSet resultSet, int i) throws SQLException {
            RuleEparchy ruleEparchy = new RuleEparchy();
            ruleEparchy.setRuleId(resultSet.getInt("RULE_ID"));
            ruleEparchy.setRuleType(StringUtil.firstOfString(resultSet.getString("RULE_TYPE")));
            ruleEparchy.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            ruleEparchy.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            ruleEparchy.setNetTypeCode(resultSet.getString("NET_TYPE_CODE"));
            ruleEparchy.setCondDesc(resultSet.getString("COND_DESC"));
            return ruleEparchy;
        }
    }

    //滞纳金计算规则结果集类
    private class LateCalParaRowMapper implements RowMapper<LateCalPara> {
        @Override
        public LateCalPara mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            LateCalPara rPLatecalpara = new LateCalPara();
            rPLatecalpara.setRuleId(resultSet.getInt("RULE_ID"));
            rPLatecalpara.setStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            rPLatecalpara.setEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            rPLatecalpara.setLateFeeRatio1(resultSet.getLong("LATE_FEE_RATIO1"));
            rPLatecalpara.setLateFeeRatio2(resultSet.getLong("LATE_FEE_RATIO2"));
            rPLatecalpara.setIniCalFee(resultSet.getLong("INI_CAL_FEE"));
            rPLatecalpara.setMaxLateFee(resultSet.getLong("MAX_LATE_FEE"));
            rPLatecalpara.setMaxDayNum(resultSet.getLong("MAX_DAY_NUM"));
            rPLatecalpara.setIniDays(resultSet.getInt("INI_DAYS"));
            return rPLatecalpara;
        }
    }
}

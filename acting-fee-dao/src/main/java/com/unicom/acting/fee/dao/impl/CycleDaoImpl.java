package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.CycleDao;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.skyark.component.util.TimeUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CycleDaoImpl extends JdbcBaseDao implements CycleDao {
    @Override
    public List<Cycle> getCycle(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CYCLE_ID,DATE_FORMAT(CYC_START_TIME,'%Y-%m-%d %T') CYC_START_TIME,");
        sql.append("DATE_FORMAT(CYC_END_TIME,'%Y-%m-%d %T') CYC_END_TIME,");
        sql.append("DATE_FORMAT(RECV_START_TIME,'%Y-%m-%d %T') RECV_START_TIME,");
        sql.append("DATE_FORMAT(RECV_END_TIME,'%Y-%m-%d %T') RECV_END_TIME,");
        sql.append("DATE_FORMAT(DISCNT_JUDGE_TIME,'%Y-%m-%d %T') DISCNT_JUDGE_TIME,");
        sql.append("DATE_FORMAT(RLATE_FEE_TIME,'%Y-%m-%d %T') RLATE_FEE_TIME,");
        sql.append("DATE_FORMAT(NLATE_FEE_TIME1,'%Y-%m-%d %T') NLATE_FEE_TIME1,");
        sql.append("DATE_FORMAT(NLATE_FEE_TIME2,'%Y-%m-%d %T') NLATE_FEE_TIME2,");
        sql.append("MONTH_ACCT_STATUS,LATE_FEE_RATIO1,");
        sql.append("LATE_FEE_RATIO2,USE_TAG,ADD_TAG,");
        sql.append("REMARK,UPDATE_PERSON,UPDATE_TIME,CHECK_PERSON,CHECK_TIME,");
        sql.append("DATE_FORMAT(CYC_HALF_TIME,'%Y-%m-%d %T') CYC_HALF_TIME,");
        sql.append("DATE_FORMAT(OPEN_ACCT_DATE,'%Y-%m-%d %T') OPEN_ACCT_DATE FROM TD_B_CYCLE");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new CycleRowMapper());
    }

    @Override
    public Cycle getCurCycle(String eparchyCode, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,CYCLE_ID,");
        sql.append("DATE_FORMAT(CYC_START_TIME,'%Y-%m-%d %T') CYC_START_TIME,");
        sql.append("DATE_FORMAT(CYC_END_TIME,'%Y-%m-%d %T') CYC_END_TIME,");
        sql.append("DATE_FORMAT(DISCNT_JUDGE_TIME,'%Y-%m-%d %T') DISCNT_JUDGE_TIME,");
        sql.append("MONTH_ACCT_STATUS,AUX_ACCT_STATUS,USE_TAG,ADD_TAG,REMARK,");
        sql.append("DATE_FORMAT(CYC_HALF_TIME,'%Y-%m-%d %T') CYC_HALF_TIME,");
        sql.append("DATE_FORMAT(OPEN_ACCT_DATE,'%Y-%m-%d %T') OPEN_ACCT_DATE ");
        sql.append("FROM TD_B_CYCLE_EPARCHY WHERE EPARCHY_CODE=:VEPARCHY_CODE ");
        sql.append("AND CYCLE_ID = :VCYCLE_ID");

        Map<String, String> param = new HashMap<>();
        param.put("VEPARCHY_CODE", eparchyCode);
        param.put("VCYCLE_ID", TimeUtil.getSysdate(TimeUtil.DATETIME_FORMAT_6));
        List<Cycle> results = this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new CycleEparchyRowMapper());
        if (!CollectionUtils.isEmpty(results)) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Cycle getMaxAcctCycle(String eparchyCode, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,CYCLE_ID,");
        sql.append("DATE_FORMAT(CYC_START_TIME,'%Y-%m-%d %T') CYC_START_TIME,");
        sql.append("DATE_FORMAT(CYC_END_TIME,'%Y-%m-%d %T') CYC_END_TIME,");
        sql.append("DATE_FORMAT(DISCNT_JUDGE_TIME,'%Y-%m-%d %T') DISCNT_JUDGE_TIME,");
        sql.append("MONTH_ACCT_STATUS,AUX_ACCT_STATUS,USE_TAG,ADD_TAG,REMARK,");
        sql.append("DATE_FORMAT(CYC_HALF_TIME,'%Y-%m-%d %T') CYC_HALF_TIME,");
        sql.append("DATE_FORMAT(OPEN_ACCT_DATE,'%Y-%m-%d %T') OPEN_ACCT_DATE ");
        sql.append("FROM TD_B_CYCLE_EPARCHY WHERE EPARCHY_CODE = :VEPARCHY_CODE ");
        sql.append("AND CYCLE_ID IN (SELECT MAX(CYCLE_ID) FROM TD_B_CYCLE_EPARCHY ");
        sql.append("WHERE USE_TAG ='1' AND EPARCHY_CODE = :VEPARCHY_CODE)");
        Map<String, String> param = new HashMap<>();
        param.put("VEPARCHY_CODE", eparchyCode);
        List<Cycle> results = this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new CycleEparchyRowMapper());
        if (!CollectionUtils.isEmpty(results)) {
            return results.get(0);
        }
        return null;
    }


    private class CycleEparchyRowMapper implements RowMapper {
        @Override
        public Cycle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Cycle cycle = new Cycle();
            cycle.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            cycle.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            cycle.setCycleId(resultSet.getInt("CYCLE_ID"));
            cycle.setCycStartTime(resultSet.getString("CYC_START_TIME"));
            cycle.setCycEndTime(resultSet.getString("CYC_END_TIME"));
            cycle.setAuxAcctStatus(resultSet.getString("AUX_ACCT_STATUS"));
            cycle.setMonthAcctStatus(resultSet.getString("MONTH_ACCT_STATUS"));
            cycle.setUseTag(StringUtil.firstOfString(resultSet.getString("USE_TAG")));
            cycle.setAddTag(StringUtil.firstOfString(resultSet.getString("ADD_TAG")));
            cycle.setOpenAcctDate(resultSet.getString("OPEN_ACCT_DATE"));
            return cycle;
        }
    }

    private class CycleRowMapper implements RowMapper {
        @Override
        public Cycle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Cycle cycle = new Cycle();
            cycle.setCycleId(resultSet.getInt("CYCLE_ID"));
            cycle.setCycStartTime(resultSet.getString("CYC_START_TIME"));
            cycle.setCycEndTime(resultSet.getString("CYC_END_TIME"));
            cycle.setUseTag(StringUtil.firstOfString(resultSet.getString("USE_TAG")));
            cycle.setAddTag(StringUtil.firstOfString(resultSet.getString("ADD_TAG")));
            return cycle;
        }
    }







}

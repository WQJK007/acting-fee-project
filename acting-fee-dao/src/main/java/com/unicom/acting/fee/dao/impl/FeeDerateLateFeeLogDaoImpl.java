package com.unicom.acting.fee.dao.impl;

import com.unicom.acting.fee.domain.FeeDerateLateFeeLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.FeeDerateLateFeeLogDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeeDerateLateFeeLogDaoImpl extends JdbcBaseDao implements FeeDerateLateFeeLogDao {
    @Override
    public List<FeeDerateLateFeeLog> getDerateLateFeeLog(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EPARCHY_CODE,DERATE_ID,ACCT_ID,USER_ID,CYCLE_ID,");
        sql.append("DERATE_RULE_ID,DERATE_FEE,USED_DERATE_FEE,USE_TAG,OPERATE_ID,");
        sql.append("DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,");
        sql.append("DATE_FORMAT(DERATE_TIME,'%Y-%m-%d %T') DERATE_TIME,");
        sql.append("DERATE_EPARCHY_CODE,DERATE_CITY_CODE,DERATE_DEPART_ID,");
        sql.append("DERATE_STAFF_ID,DERATE_REASON_CODE,REMARK,RSRV_FEE1,");
        sql.append("RSRV_FEE2,RSRV_INFO1,RSRV_INFO2 FROM TF_B_DERATELATEFEELOG ");
        sql.append("WHERE ACCT_ID = :VACCT_ID AND CYCLE_ID >=:VSTART_CYCLE_ID ");
        sql.append(" AND CYCLE_ID <=:VEND_CYCLE_ID AND USE_TAG IN ('0','2') ");
        sql.append("AND START_DATE <=SYSDATE() AND SYSDATE() <=END_DATE");
        Map<String, String> param = new HashMap<>();
        param.put("VACCT_ID", acctId);
        param.put("VSTART_CYCLE_ID", String.valueOf(startCycleId));
        param.put("VEND_CYCLE_ID", String.valueOf(endCycleId));
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new DerateLateFeeLogRowMapper());
    }

    private class DerateLateFeeLogRowMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            FeeDerateLateFeeLog feeDerateLateFeeLog = new FeeDerateLateFeeLog();
            feeDerateLateFeeLog.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            feeDerateLateFeeLog.setDerateId(resultSet.getString("DERATE_ID"));
            feeDerateLateFeeLog.setAcctId(resultSet.getString("ACCT_ID"));
            feeDerateLateFeeLog.setUserId(resultSet.getString("USER_ID"));
            feeDerateLateFeeLog.setCycleId(resultSet.getInt("CYCLE_ID"));
            feeDerateLateFeeLog.setDerateRuleId(resultSet.getInt("DERATE_RULE_ID"));
            feeDerateLateFeeLog.setDerateFee(resultSet.getInt("DERATE_FEE"));
            feeDerateLateFeeLog.setUsedDerateFee(resultSet.getInt("USED_DERATE_FEE"));
            feeDerateLateFeeLog.setOldUsedDerateFee(feeDerateLateFeeLog.getUsedDerateFee());
            feeDerateLateFeeLog.setUseTag(StringUtil.firstOfString(resultSet.getString("USE_TAG")));
            feeDerateLateFeeLog.setOldUseTag(feeDerateLateFeeLog.getUseTag());
            feeDerateLateFeeLog.setOperateId(resultSet.getString("OPERATE_ID"));
            feeDerateLateFeeLog.setStartDate(resultSet.getString("START_DATE"));
            feeDerateLateFeeLog.setEndDate(resultSet.getString("END_DATE"));
            feeDerateLateFeeLog.setDerateTime(resultSet.getString("DERATE_TIME"));
            feeDerateLateFeeLog.setDerateEparchyCode(resultSet.getString("DERATE_EPARCHY_CODE"));
            feeDerateLateFeeLog.setDerateCityCode(resultSet.getString("DERATE_CITY_CODE"));
            feeDerateLateFeeLog.setDerateDepartId(resultSet.getString("DERATE_DEPART_ID"));
            feeDerateLateFeeLog.setDerateStaffId(resultSet.getString("DERATE_STAFF_ID"));
            feeDerateLateFeeLog.setDerateReasonCode(resultSet.getInt("DERATE_REASON_CODE"));
            feeDerateLateFeeLog.setRemark(resultSet.getString("REMARK"));
            feeDerateLateFeeLog.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            feeDerateLateFeeLog.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            feeDerateLateFeeLog.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            feeDerateLateFeeLog.setRsrvInfo2(resultSet.getString("RSRV_INFO2"));
            return feeDerateLateFeeLog;
        }
    }
}

package com.unicom.acting.fee.dao.impl;

import com.unicom.acting.fee.dao.FeeAccountDao;
import com.unicom.acting.fee.domain.FeeAcctPaymentCycle;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeeAccountDaoImpl extends JdbcBaseDao implements FeeAccountDao {

    @Override
    public FeeAcctPaymentCycle getAcctPaymentCycle(String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.OFFSET_DAYS, A.OFFSET_MONTHS, A.BUNDLE_MONTHS,");
        sql.append("DATE_FORMAT(B.IN_DATE, '%Y%m') IN_DATE FROM TD_B_PAYMENT_CYCLE A,");
        sql.append("TF_F_ACCT_PAYMENT_CYCLE B WHERE A.PAYMENT_CYCLE_TYPE = B.PAYMENT_CYCLE_TYPE ");
        sql.append("AND B.ACCT_ID = :VACCT_ID AND B.ACT_TAG = '1'");
        Map param = Collections.singletonMap("VACCT_ID", acctId);
        List<FeeAcctPaymentCycle> results = this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(
                sql.toString(), param, new AcctPaymentCycleRowMapper());
        if (!CollectionUtils.isEmpty(results)) {
            return results.get(0);
        }
        return null;
    }

    //账户自定义缴费期
    private class AcctPaymentCycleRowMapper implements RowMapper<FeeAcctPaymentCycle> {
        @Override
        public FeeAcctPaymentCycle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            FeeAcctPaymentCycle acctPaymentCycle = new FeeAcctPaymentCycle();
            acctPaymentCycle.setOffDays(resultSet.getInt("OFFSET_DAYS"));
            acctPaymentCycle.setOffMonths(resultSet.getInt("OFFSET_MONTHS"));
            acctPaymentCycle.setBundleMonths(resultSet.getInt("BUNDLE_MONTHS"));
            acctPaymentCycle.setInDate(resultSet.getString("IN_DATE"));
            return acctPaymentCycle;
        }
    }
}

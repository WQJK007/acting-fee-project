package com.unicom.acting.fee.dao.impl;

import com.unicom.acting.fee.dao.FeeAccountDepositDao;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.domain.FeeAcctBalanceRel;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeeAccountDepositDaoImpl extends JdbcBaseDao implements FeeAccountDepositDao {
    @Override
    public List<FeeAccountDeposit> getAcctDepositByAcctId(String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCT_BALANCE_ID,ACCT_ID,USER_ID,");
        sql.append("DEPOSIT_CODE,DEPOSIT_MONEY,INIT_MONEY,MONEY,");
        sql.append("LIMIT_MONEY,LIMIT_MODE,INVOICE_FEE,PRINT_FEE,START_CYCLE_ID,");
        sql.append("END_CYCLE_ID,DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,OWE_FEE,");
        sql.append("VALID_TAG,FREEZE_FEE,PRIVATE_TAG,PROVINCE_CODE,PROVINCE_CODE,");
        sql.append("EPARCHY_CODE,VERSION_NO,IFNULL(ACTION_CODE,-1) ACTION_CODE,");
        sql.append("OPEN_CYCLE_ID,DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,");
        sql.append("RSRV_INFO2,RSRV_INFO1,RSRV_FEE2,RSRV_FEE1,LIMIT_LEFT ");
        sql.append("FROM TF_F_ACCOUNTDEPOSIT WHERE ACCT_ID=:VACCT_ID");
        Map param = Collections.singletonMap("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new PAcctDepositMapper());
    }

    @Override
    public List<FeeAcctBalanceRel> getAcctBalanceRelByAcctId(String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCT_BALANCE_ID,ACCT_BALANCE_ID2,ACCT_ID,RATE ");
        sql.append("FROM TF_F_ACCTBALANCE_REL WHERE ACCT_ID = :VACCT_ID ");
        Map param = Collections.singletonMap("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new PAcctBalanceRelMapper());
    }


    //账本结果集
    class PAcctDepositMapper implements RowMapper<FeeAccountDeposit> {
        @Override
        public FeeAccountDeposit mapRow(ResultSet resultSet, int i) throws SQLException {
            FeeAccountDeposit pAcctDeposit = new FeeAccountDeposit();
            pAcctDeposit.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            pAcctDeposit.setAcctId(resultSet.getString("ACCT_ID"));
            pAcctDeposit.setUserId(resultSet.getString("USER_ID"));
            pAcctDeposit.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            pAcctDeposit.setDepositMoney(resultSet.getLong("DEPOSIT_MONEY"));
            pAcctDeposit.setInitMoney(resultSet.getLong("INIT_MONEY"));
            pAcctDeposit.setMoney(resultSet.getLong("MONEY"));
            pAcctDeposit.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            pAcctDeposit.setLimitMoney(resultSet.getLong("LIMIT_MONEY"));
            pAcctDeposit.setLimitLeft(resultSet.getLong("LIMIT_LEFT"));
            pAcctDeposit.setInvoiceFee(resultSet.getLong("INVOICE_FEE"));
            pAcctDeposit.setPrintFee(resultSet.getLong("PRINT_FEE"));
            pAcctDeposit.setStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            pAcctDeposit.setEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            pAcctDeposit.setStartDate(resultSet.getString("START_DATE"));
            pAcctDeposit.setEndDate(resultSet.getString("END_DATE"));
            pAcctDeposit.setOweFee(resultSet.getLong("OWE_FEE"));
            pAcctDeposit.setFreezeFee(resultSet.getLong("FREEZE_FEE"));
            pAcctDeposit.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            pAcctDeposit.setActionCode(resultSet.getInt("ACTION_CODE"));
            pAcctDeposit.setVersionNo(resultSet.getInt("VERSION_NO"));
            pAcctDeposit.setUpdateTime(resultSet.getString("UPDATE_TIME"));
            pAcctDeposit.setValidTag(StringUtil.firstOfString(resultSet.getString("VALID_TAG")));
            pAcctDeposit.setOpenCycleId(resultSet.getInt("OPEN_CYCLE_ID"));
            pAcctDeposit.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            pAcctDeposit.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            pAcctDeposit.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            pAcctDeposit.setRsrvInfo2(resultSet.getString("RSRV_INFO2"));
            pAcctDeposit.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            pAcctDeposit.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            return pAcctDeposit;
        }
    }

    //帐本关系表结果集
    class PAcctBalanceRelMapper implements RowMapper<FeeAcctBalanceRel> {
        @Override
        public FeeAcctBalanceRel mapRow(ResultSet resultSet, int i) throws SQLException {
            FeeAcctBalanceRel pAcctBalanceRel = new FeeAcctBalanceRel();
            pAcctBalanceRel.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            pAcctBalanceRel.setAcctId(resultSet.getString("ACCT_ID"));
            pAcctBalanceRel.setAcctBalanceId2(resultSet.getString("ACCT_BALANCE_ID2"));
            pAcctBalanceRel.setRate(resultSet.getInt("RATE"));
            return pAcctBalanceRel;
        }
    }
}

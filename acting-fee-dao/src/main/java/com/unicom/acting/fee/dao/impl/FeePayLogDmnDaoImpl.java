package com.unicom.acting.fee.dao.impl;

import com.unicom.acting.fee.domain.FeeDiscntDeposit;
import com.unicom.acting.fee.domain.FeePayLogDmn;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.FeePayLogDmnDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeePayLogDmnDaoImpl extends JdbcBaseDao implements FeePayLogDmnDao {
    @Override
    public List<FeePayLogDmn> getPaylogDmnByAcctId(String acctId, String getMode, String dbType, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EPARCHY_CODE,PROVINCE_CODE,TRADE_ID,TRADE_TYPE_CODE,BATCH_ID,");
        sql.append("PRIORITY,CHARGE_ID,ACCT_ID,USER_ID,SERIAL_NUMBER,WRITEOFF_MODE,");
        sql.append("CHANNEL_ID,PAYMENT_ID,PAYMENT_OP,PAY_FEE_MODE_CODE,RECV_FEE,");
        sql.append("RECV_FEE,OUTER_TRADE_ID,BILL_START_CYCLE_ID START_CYCLE_ID,");
        sql.append("BILL_END_CYCLE_ID END_CYCLE_ID,MONTHS,");
        sql.append("DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,");
        sql.append("LIMIT_MONEY,PAYMENT_REASON_CODE,EXTEND_TAG,ACTION_CODE,");
        sql.append("ACTION_EVENT_ID,ACCT_BALANCE_ID,DEPOSIT_CODE,PRIVATE_TAG,REMARK,INPUT_NO,");
        sql.append("INPUT_MODE,ACCT_ID2,USER_ID2,DEPOSIT_CODE2,REL_CHARGE_ID,");
        sql.append("DATE_FORMAT(TRADE_TIME,'%Y-%m-%d %T') RECV_TIME,");
        sql.append("TRADE_EPARCHY_CODE,TRADE_CITY_CODE,TRADE_DEPART_ID,TRADE_STAFF_ID,");
        sql.append("CANCEL_TAG,DEAL_TAG,RESULT_CODE,RESULT_INFO,RSRV_FEE1,RSRV_FEE2,");
        sql.append("DATE_FORMAT(DEAL_TIME,'%Y-%m-%d %T') DEAL_TIME,");
        sql.append("RSRV_INFO1,LIMIT_MODE,PRINT_TAG FROM TF_B_PAYLOG_DMN ");
        sql.append("WHERE ACCT_ID=:VACCT_ID AND DEAL_TAG=:VDEAL_TAG");
        Map<String, String> param = new HashMap(2);
        param.put("VACCT_ID", acctId);
        param.put("VDEAL_TAG", getMode);
        return this.getJdbcTemplate(dbType, provinceCode).query(sql.toString(), param, new PaylogDmnRowMapper());
    }

    @Override
    public boolean ifExistOuterTradeId(String tradeId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_B_PAYLOG WHERE OUTER_TRADE_ID=:VOUTER_TRADE_ID ");
        Map<String, String> param = new HashMap<>();
        param.put("VOUTER_TRADE_ID", tradeId);
        List<String> result = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }

    @Override
    public List<FeeDiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCT_BALANCE_ID,LEFT_MONEY,LIMIT_MODE,SPLIT_METHOD,MONEY,");
        sql.append("LIMIT_MONEY,MONTHS FROM TF_B_DISCNT_DEPOSIT WHERE USER_ID = :VUSER_ID ");
        sql.append("AND ACCT_ID = :VACCT_ID AND CANCEL_TAG = '0' ");
        sql.append("AND START_CYCLE_ID <= 197001 AND END_CYCLE_ID <= 197001 ");
        sql.append("UNION ALL ");
        sql.append("SELECT ACCT_BALANCE_ID,LEFT_MONEY,LIMIT_MODE,SPLIT_METHOD,MONEY,");
        sql.append("LIMIT_MONEY,MONTHS FROM TF_B_DISCNT_DEPOSIT WHERE USER_ID = :VUSER_ID ");
        sql.append("AND ACCT_ID = :VACCT_ID AND CANCEL_TAG = '0' ");
        sql.append("AND END_CYCLE_ID >= :VCYCLE_ID ");
        Map<String, String> param = new HashMap<>();
        param.put("VUSER_ID", userId);
        param.put("VACCT_ID", acctId);
        param.put("VCYCLE_ID", TimeUtil.getSysdate(TimeUtil.DATETIME_FORMAT_6));
        return this.getJdbcTemplate(provinceId).query(sql.toString(), param, new RowMapper<FeeDiscntDeposit>() {
            @Override
            public FeeDiscntDeposit mapRow(ResultSet rs, int rowNum) throws SQLException {
                FeeDiscntDeposit feeDiscntDeposit = new FeeDiscntDeposit();
                feeDiscntDeposit.setAcctBalanceId(rs.getString("ACCT_BALANCE_ID"));
                feeDiscntDeposit.setLeftMoney(rs.getLong("LEFT_MONEY"));
                feeDiscntDeposit.setLimitMode(StringUtil.firstOfString(rs.getString("LIMIT_MODE")));
                feeDiscntDeposit.setMoney(rs.getLong("MONEY"));
                feeDiscntDeposit.setLimitMoney(rs.getLong("LIMIT_MONEY"));
                feeDiscntDeposit.setMonths(rs.getInt("MONTHS"));
                feeDiscntDeposit.setSplitMethod(rs.getString("SPLIT_METHOD"));
                return feeDiscntDeposit;
            }

        });
    }

    class PaylogDmnRowMapper implements RowMapper<FeePayLogDmn> {
        @Override
        public FeePayLogDmn mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            FeePayLogDmn feePayLogDmn = new FeePayLogDmn();
            feePayLogDmn.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            feePayLogDmn.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            feePayLogDmn.setTradeId(resultSet.getString("TRADE_ID"));
            feePayLogDmn.setTradeTypeCode(resultSet.getInt("TRADE_TYPE_CODE"));
            feePayLogDmn.setBatchId(resultSet.getString("BATCH_ID"));
            feePayLogDmn.setPriority(resultSet.getInt("PRIORITY"));
            feePayLogDmn.setChargeId(resultSet.getString("CHARGE_ID"));
            feePayLogDmn.setAcctId(resultSet.getString("ACCT_ID"));
            feePayLogDmn.setUserId(resultSet.getString("USER_ID"));
            feePayLogDmn.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            feePayLogDmn.setWriteoffMode(StringUtil.firstOfString(resultSet.getString("WRITEOFF_MODE")));
            feePayLogDmn.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            feePayLogDmn.setChannelId(resultSet.getString("CHANNEL_ID"));
            feePayLogDmn.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            feePayLogDmn.setPaymentOp(resultSet.getInt("PAYMENT_OP"));
            feePayLogDmn.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            feePayLogDmn.setRecvFee(resultSet.getInt("RECV_FEE"));
            feePayLogDmn.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            feePayLogDmn.setBillStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            feePayLogDmn.setBillEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            feePayLogDmn.setStartDate(resultSet.getString("START_DATE"));
            feePayLogDmn.setEndDate(resultSet.getString("END_DATE"));
            feePayLogDmn.setMonths(resultSet.getInt("MONTHS"));
            feePayLogDmn.setLimitMoney(resultSet.getInt("LIMIT_MONEY"));
            feePayLogDmn.setPaymentReasonCode(resultSet.getInt("PAYMENT_REASON_CODE"));
            feePayLogDmn.setExtendTag(StringUtil.firstOfString(resultSet.getString("EXTEND_TAG")));
            feePayLogDmn.setActionCode(resultSet.getInt("ACTION_CODE"));
            feePayLogDmn.setActionEventId(resultSet.getString("ACTION_EVENT_ID"));
            feePayLogDmn.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            feePayLogDmn.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            feePayLogDmn.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            feePayLogDmn.setRemark(resultSet.getString("REMARK"));
            feePayLogDmn.setInputNo(resultSet.getString("INPUT_NO"));
            feePayLogDmn.setInputMode(resultSet.getInt("INPUT_MODE"));
            feePayLogDmn.setAcctId2(resultSet.getString("ACCT_ID2"));
            feePayLogDmn.setUserId2(resultSet.getString("USER_ID2"));
            feePayLogDmn.setDepositCode2(resultSet.getInt("DEPOSIT_CODE2"));
            feePayLogDmn.setRelChargeId(resultSet.getString("REL_CHARGE_ID"));
            feePayLogDmn.setTradeTime(resultSet.getString("RECV_TIME"));
            feePayLogDmn.setTradeEparchyCode(resultSet.getString("TRADE_EPARCHY_CODE"));
            feePayLogDmn.setTradeCityCode(resultSet.getString("TRADE_CITY_CODE"));
            feePayLogDmn.setTradeDepartId(resultSet.getString("TRADE_DEPART_ID"));
            feePayLogDmn.setTradeStaffId(resultSet.getString("TRADE_STAFF_ID"));
            feePayLogDmn.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            feePayLogDmn.setDealTag(StringUtil.firstOfString(resultSet.getString("DEAL_TAG")));
            feePayLogDmn.setDealTime(resultSet.getString("DEAL_TIME"));
            feePayLogDmn.setResultCode(resultSet.getInt("RESULT_CODE"));
            feePayLogDmn.setResultInfo(resultSet.getString("RESULT_INFO"));
            feePayLogDmn.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            feePayLogDmn.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            feePayLogDmn.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            feePayLogDmn.setPrintTag(StringUtil.firstOfString(resultSet.getString("PRINT_TAG")));
            return feePayLogDmn;
        }
    }
}

package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.PayLogFeeDao;
import com.unicom.acting.fee.domain.DiscntDeposit;
import com.unicom.acting.fee.domain.PayLogDmn;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayLogFeeDaoImpl extends JdbcBaseDao implements PayLogFeeDao {
    @Override
    public List<PayLogDmn> getPaylogDmnByAcctId(String acctId, String getMode, String provinceCode) {
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
        Map<String, String> param = new HashMap<>();
        param.put("VACCT_ID", acctId);
        param.put("VDEAL_TAG", getMode);
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new PaylogDmnRowMapper());
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
    public List<DiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId, String provinceId) {
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
        return this.getJdbcTemplate(provinceId).query(sql.toString(), param, new RowMapper<DiscntDeposit>() {
            @Override
            public DiscntDeposit mapRow(ResultSet rs, int rowNum) throws SQLException {
                DiscntDeposit discntDeposit = new DiscntDeposit();
                discntDeposit.setAcctBalanceId(rs.getString("ACCT_BALANCE_ID"));
                discntDeposit.setLeftMoney(rs.getLong("LEFT_MONEY"));
                discntDeposit.setLimitMode(StringUtil.firstOfString(rs.getString("LIMIT_MODE")));
                discntDeposit.setMoney(rs.getLong("MONEY"));
                discntDeposit.setLimitMoney(rs.getLong("LIMIT_MONEY"));
                discntDeposit.setMonths(rs.getInt("MONTHS"));
                discntDeposit.setSplitMethod(rs.getString("SPLIT_METHOD"));
                return discntDeposit;
            }

        });
    }

    class PaylogDmnRowMapper implements RowMapper<PayLogDmn> {
        @Override
        public PayLogDmn mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PayLogDmn payLogDmn = new PayLogDmn();
            payLogDmn.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            payLogDmn.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            payLogDmn.setTradeId(resultSet.getString("TRADE_ID"));
            payLogDmn.setTradeTypeCode(resultSet.getInt("TRADE_TYPE_CODE"));
            payLogDmn.setBatchId(resultSet.getString("BATCH_ID"));
            payLogDmn.setPriority(resultSet.getInt("PRIORITY"));
            payLogDmn.setChargeId(resultSet.getString("CHARGE_ID"));
            payLogDmn.setAcctId(resultSet.getString("ACCT_ID"));
            payLogDmn.setUserId(resultSet.getString("USER_ID"));
            payLogDmn.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            payLogDmn.setWriteoffMode(StringUtil.firstOfString(resultSet.getString("WRITEOFF_MODE")));
            payLogDmn.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            payLogDmn.setChannelId(resultSet.getString("CHANNEL_ID"));
            payLogDmn.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            payLogDmn.setPaymentOp(resultSet.getInt("PAYMENT_OP"));
            payLogDmn.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            payLogDmn.setRecvFee(resultSet.getInt("RECV_FEE"));
            payLogDmn.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            payLogDmn.setBillStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            payLogDmn.setBillEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            payLogDmn.setStartDate(resultSet.getString("START_DATE"));
            payLogDmn.setEndDate(resultSet.getString("END_DATE"));
            payLogDmn.setMonths(resultSet.getInt("MONTHS"));
            payLogDmn.setLimitMoney(resultSet.getInt("LIMIT_MONEY"));
            payLogDmn.setPaymentReasonCode(resultSet.getInt("PAYMENT_REASON_CODE"));
            payLogDmn.setExtendTag(StringUtil.firstOfString(resultSet.getString("EXTEND_TAG")));
            payLogDmn.setActionCode(resultSet.getInt("ACTION_CODE"));
            payLogDmn.setActionEventId(resultSet.getString("ACTION_EVENT_ID"));
            payLogDmn.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            payLogDmn.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            payLogDmn.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            payLogDmn.setRemark(resultSet.getString("REMARK"));
            payLogDmn.setInputNo(resultSet.getString("INPUT_NO"));
            payLogDmn.setInputMode(resultSet.getInt("INPUT_MODE"));
            payLogDmn.setAcctId2(resultSet.getString("ACCT_ID2"));
            payLogDmn.setUserId2(resultSet.getString("USER_ID2"));
            payLogDmn.setDepositCode2(resultSet.getInt("DEPOSIT_CODE2"));
            payLogDmn.setRelChargeId(resultSet.getString("REL_CHARGE_ID"));
            payLogDmn.setTradeTime(resultSet.getString("RECV_TIME"));
            payLogDmn.setTradeEparchyCode(resultSet.getString("TRADE_EPARCHY_CODE"));
            payLogDmn.setTradeCityCode(resultSet.getString("TRADE_CITY_CODE"));
            payLogDmn.setTradeDepartId(resultSet.getString("TRADE_DEPART_ID"));
            payLogDmn.setTradeStaffId(resultSet.getString("TRADE_STAFF_ID"));
            payLogDmn.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            payLogDmn.setDealTag(StringUtil.firstOfString(resultSet.getString("DEAL_TAG")));
            payLogDmn.setDealTime(resultSet.getString("DEAL_TIME"));
            payLogDmn.setResultCode(resultSet.getInt("RESULT_CODE"));
            payLogDmn.setResultInfo(resultSet.getString("RESULT_INFO"));
            payLogDmn.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            payLogDmn.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            payLogDmn.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            payLogDmn.setPrintTag(StringUtil.firstOfString(resultSet.getString("PRINT_TAG")));
            return payLogDmn;
        }
    }
}

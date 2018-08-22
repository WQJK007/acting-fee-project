package com.unicom.acting.fee.dao.impl;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.FeeBillDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeeBillDaoImpl extends JdbcBaseDao implements FeeBillDao {
    @Override
    public List<FeeBill> getBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,NET_TYPE_CODE,SERIAL_NUMBER,");
        sql.append("BILL_ID,ACCT_ID,USER_ID,CYCLE_ID,INTEGRATE_ITEM_CODE,FEE,BALANCE,");
        sql.append("PRINT_FEE,B_DISCNT,A_DISCNT,ADJUST_BEFORE,ADJUST_AFTER,LATE_FEE,");
        sql.append("LATE_BALANCE,DATE_FORMAT(LATECAL_DATE,'%Y-%m-%d %T') LATECAL_DATE,");
        sql.append("CANPAY_TAG,PAY_TAG,BILL_PAY_TAG,VERSION_NO,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,UPDATE_DEPART_ID,");
        sql.append("UPDATE_STAFF_ID,CHARGE_ID,WRITEOFF_FEE1,WRITEOFF_FEE2,WRITEOFF_FEE3,");
        sql.append("RSRV_FEE1,RSRV_FEE2,RSRV_FEE3,RSRV_INFO1,RSRV_INFO2 FROM TS_B_BILL ");
        sql.append("WHERE ACCT_ID = :VACCT_ID AND CYCLE_ID >= :VSTART_CYCLE_ID ");
        sql.append("AND CYCLE_ID <= :VEND_CYCLE_ID AND BILL_PAY_TAG ='0' ");
        sql.append("AND CANPAY_TAG NOT IN ('8','9')");
        Map param = new HashMap();
        param.put("VACCT_ID", acctId);
        param.put("VSTART_CYCLE_ID", startCycleId);
        param.put("VEND_CYCLE_ID", endCycleId);
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new BillRowMapper());
    }

    @Override
    public List<FeeBill> getBillOweByUserId(String acctId, String userId, int startCycleId, int endCycleId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,NET_TYPE_CODE,SERIAL_NUMBER,");
        sql.append("BILL_ID,ACCT_ID,USER_ID,CYCLE_ID,INTEGRATE_ITEM_CODE,FEE,BALANCE,");
        sql.append("PRINT_FEE,B_DISCNT,A_DISCNT,ADJUST_BEFORE,ADJUST_AFTER,LATE_FEE,");
        sql.append("LATE_BALANCE,DATE_FORMAT(LATECAL_DATE,'%Y-%m-%d %T') LATECAL_DATE,");
        sql.append("CANPAY_TAG,PAY_TAG,BILL_PAY_TAG,VERSION_NO,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,UPDATE_DEPART_ID,");
        sql.append("UPDATE_STAFF_ID,CHARGE_ID,WRITEOFF_FEE1,WRITEOFF_FEE2,WRITEOFF_FEE3,");
        sql.append("RSRV_FEE1,RSRV_FEE2,RSRV_FEE3,RSRV_INFO1,RSRV_INFO2 FROM TS_B_BILL ");
        sql.append("WHERE ACCT_ID=:VACCT_ID AND USER_ID = :VUSER_ID ");
        sql.append("AND CYCLE_ID>=:VSTART_CYCLE_ID AND CYCLE_ID<=:VEND_CYCLE_ID ");
        sql.append("AND BILL_PAY_TAG='0' AND CANPAY_TAG NOT IN ('8','9')");
        Map param = new HashMap();
        param.put("VACCT_ID", acctId);
        param.put("VUSER_ID", userId);
        param.put("VSTART_CYCLE_ID", startCycleId);
        param.put("VEND_CYCLE_ID", endCycleId);
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new BillRowMapper());
    }

    @Override
    public List<FeeBill> getBadBillOweByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,NET_TYPE_CODE,SERIAL_NUMBER,");
        sql.append("BILL_ID,ACCT_ID,USER_ID,CYCLE_ID,INTEGRATE_ITEM_CODE,FEE,");
        sql.append("BALANCE,PRINT_FEE,B_DISCNT,A_DISCNT,ADJUST_BEFORE,ADJUST_AFTER,");
        sql.append("LATE_FEE,LATE_BALANCE,CANPAY_TAG,PAY_TAG,BILL_PAY_TAG,");
        sql.append("DATE_FORMAT(LATECAL_DATE,'%Y-%m-%d %T') LATECAL_DATE,");
        sql.append("VERSION_NO,UPDATE_DEPART_ID,UPDATE_STAFF_ID,CHARGE_ID,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,");
        sql.append("WRITEOFF_FEE1,WRITEOFF_FEE2,WRITEOFF_FEE3,");
        sql.append("RSRV_FEE1,RSRV_FEE2,RSRV_FEE3,RSRV_INFO1,RSRV_INFO2 ");
        sql.append("FROM TS_B_BADBILL WHERE ACCT_ID=:VACCT_ID AND CYCLE_ID>=:VSTART_CYCLE_ID ");
        sql.append("AND CYCLE_ID<=:VEND_CYCLE_ID AND BILL_PAY_TAG='0'");
        Map param = new HashMap();
        param.put("VACCT_ID", acctId);
        param.put("VSTART_CYCLE_ID", startCycleId);
        param.put("VEND_CYCLE_ID", endCycleId);
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), param, new BillRowMapper());
    }

    @Override
    public boolean getBillByAcctId(String acctId, int startCycleId, int endCycleId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TS_B_BILL WHERE ACCT_ID=:VACCT_ID ");
        sql.append("AND CYCLE_ID >=:VSTART_CYCLE_ID AND CYCLE_ID <=:VEND_CYCLE_ID");
        Map param = new HashMap();
        param.put("VACCT_ID", acctId);
        param.put("VSTART_CYCLE_ID", startCycleId);
        param.put("VEND_CYCLE_ID", endCycleId);
        List<String> result = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }

    //账单结果集
    class BillRowMapper implements RowMapper<FeeBill> {
        @Override
        public FeeBill mapRow(ResultSet resultSet, int i) throws SQLException {
            FeeBill feeBill = new FeeBill();
            feeBill.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            feeBill.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            feeBill.setNetTypeCode(resultSet.getString("NET_TYPE_CODE"));
            feeBill.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            feeBill.setBillId(resultSet.getString("BILL_ID"));
            feeBill.setAcctId(resultSet.getString("ACCT_ID"));
            feeBill.setUserId(resultSet.getString("USER_ID"));
            feeBill.setCycleId(resultSet.getInt("CYCLE_ID"));
            feeBill.setIntegrateItemCode(resultSet.getInt("INTEGRATE_ITEM_CODE"));
            feeBill.setFee(resultSet.getLong("FEE"));
            feeBill.setBalance(resultSet.getLong("BALANCE"));
            feeBill.setPrintFee(resultSet.getLong("PRINT_FEE"));
            feeBill.setbDiscnt(resultSet.getLong("B_DISCNT"));
            feeBill.setaDiscnt(resultSet.getLong("A_DISCNT"));
            feeBill.setAdjustBefore(resultSet.getLong("ADJUST_BEFORE"));
            feeBill.setAdjustAfter(resultSet.getLong("ADJUST_AFTER"));
            feeBill.setLateFee(resultSet.getLong("LATE_FEE"));
            feeBill.setLateBalance(resultSet.getLong("LATE_BALANCE"));
            feeBill.setLateCalDate(resultSet.getString("LATECAL_DATE"));
            feeBill.setCanpayTag(StringUtil.firstOfString(resultSet.getString("CANPAY_TAG")));
            feeBill.setPayTag(StringUtil.firstOfString(resultSet.getString("PAY_TAG")));
            feeBill.setOldPayTag(StringUtil.firstOfString(resultSet.getString("PAY_TAG")));
            feeBill.setBillPayTag(StringUtil.firstOfString(resultSet.getString("BILL_PAY_TAG")));
            feeBill.setVersionNo(resultSet.getInt("VERSION_NO"));
            feeBill.setUpdateTime(resultSet.getString("UPDATE_TIME"));
            feeBill.setUpdateDepartId(resultSet.getString("UPDATE_DEPART_ID"));
            feeBill.setUpdateStaffId(resultSet.getString("UPDATE_STAFF_ID"));
            feeBill.setChargeId(resultSet.getString("CHARGE_ID"));
            feeBill.setWriteoffFee1(resultSet.getLong("WRITEOFF_FEE1"));
            feeBill.setWriteoffFee2(resultSet.getLong("WRITEOFF_FEE2"));
            feeBill.setWriteoffFee3(resultSet.getLong("WRITEOFF_FEE3"));
            feeBill.setRsrvFee1(resultSet.getLong("RSRV_FEE1"));
            feeBill.setRsrvFee2(resultSet.getLong("RSRV_FEE2"));
            feeBill.setRsrvFee3(resultSet.getLong("RSRV_FEE3"));
            feeBill.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            return feeBill;
        }
    }

}

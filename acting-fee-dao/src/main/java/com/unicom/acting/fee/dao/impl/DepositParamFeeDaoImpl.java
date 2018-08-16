package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.DepositParamFeeDao;
import com.unicom.acting.fee.domain.DepositLimitRule;
import com.unicom.acting.fee.domain.DepositPriorRule;
import com.unicom.acting.fee.domain.PaymentDeposit;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DepositParamFeeDaoImpl extends JdbcBaseDao implements DepositParamFeeDao {
    @Override
    public List<DepositPriorRule> getDepositPriorRule(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.DEPOSIT_PRIOR_RULE_ID,A.DEPOSIT_CODE,A.DEPOSIT_PRIORITY,");
        sql.append("B.DEPOSIT_NAME,B.RETURN_TAG,B.DEPOSIT_TYPE_CODE,B.INVOICE_TAG,");
        sql.append("B.CAN_DIS_TAG,B.CAN_TRAN_TAG,B.CAN_CONSIGN_TAG,B.CASH_TAG,B.DEAL_TYPE,");
        sql.append("B.IF_BALANCE,B.CREDIT_MODE,B.IF_CALC_OWE,B.IF_UNITE,B.IF_ADJUST,");
        sql.append("B.ITEM_PRIOR_RULE_ID,B.PRIORITY,B.CASH_TYPE ");
        sql.append("FROM TD_B_DEPOSITPRIORRULE A, TD_B_DEPOSIT B ");
        sql.append("WHERE A.DEPOSIT_CODE = B.DEPOSIT_CODE ");
        sql.append("AND B.IF_BALANCE IN ('0','1') ");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new DepositPriorRuleMapper());
    }

    @Override
    public List<DepositLimitRule> getDepositLimitRule(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DEPOSIT_LIMIT_RULE_ID,DEPOSIT_CODE,ITEM_CODE,");
        sql.append("LIMIT_MODE,LIMIT_TYPE,REMARK FROM TD_B_DEPOSITLIMITRULE");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new DepositLimitRuleMapper());
    }

    @Override
    public List<PaymentDeposit> getPaymentDeposit(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PAYMENT_ID,PAY_FEE_MODE_CODE,RULE_ID,DEPOSIT_CODE,");
        sql.append("PRIVATE_TAG,INVOICE_TAG,REMARK FROM TD_B_PAYMENT_DEPOSIT");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new PaymentDepositMapper());
    }

    //账本科目优先级结果集类
    class DepositPriorRuleMapper implements RowMapper<DepositPriorRule> {
        @Override
        public DepositPriorRule mapRow(ResultSet resultSet, int i) throws SQLException {
            DepositPriorRule depositPriorRule = new DepositPriorRule();
            depositPriorRule.setDepositPriorRuleId(resultSet.getInt("DEPOSIT_PRIOR_RULE_ID"));
            depositPriorRule.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            depositPriorRule.setDepositName(resultSet.getString("DEPOSIT_NAME"));
            depositPriorRule.setDepositPriority(resultSet.getInt("DEPOSIT_PRIORITY"));
            depositPriorRule.setReturnTag(StringUtil.firstOfString(resultSet.getString("RETURN_TAG")));
            depositPriorRule.setDepositTypeCode(StringUtil.firstOfString(resultSet.getString("DEPOSIT_TYPE_CODE")));
            depositPriorRule.setInvoiceTag(StringUtil.firstOfString(resultSet.getString("INVOICE_TAG")));
            depositPriorRule.setCanDisTag(StringUtil.firstOfString(resultSet.getString("CAN_DIS_TAG")));
            depositPriorRule.setCanTranTag(StringUtil.firstOfString(resultSet.getString("CAN_TRAN_TAG")));
            depositPriorRule.setCanConsignTag(StringUtil.firstOfString(resultSet.getString("CAN_CONSIGN_TAG")));
            depositPriorRule.setCashTag(StringUtil.firstOfString(resultSet.getString("CASH_TAG")));
            depositPriorRule.setCashType(StringUtil.firstOfString(resultSet.getString("CASH_TYPE")));
            depositPriorRule.setDealType(resultSet.getString("DEAL_TYPE"));
            depositPriorRule.setIfBalance(StringUtil.firstOfString(resultSet.getString("IF_BALANCE")));
            depositPriorRule.setCreditMode(StringUtil.firstOfString(resultSet.getString("CREDIT_MODE")));
            depositPriorRule.setIfCalcOwe(StringUtil.firstOfString(resultSet.getString("IF_CALC_OWE")));
            depositPriorRule.setIfUnite(StringUtil.firstOfString(resultSet.getString("IF_UNITE")));
            depositPriorRule.setIfAdjust(StringUtil.firstOfString(resultSet.getString("IF_ADJUST")));
            depositPriorRule.setItemPriorRuleId(resultSet.getInt("ITEM_PRIOR_RULE_ID"));
            depositPriorRule.setPriority(resultSet.getInt("PRIORITY"));
            return depositPriorRule;
        }
    }

    //账本科目限定规则结果集
    class DepositLimitRuleMapper implements RowMapper<DepositLimitRule> {
        @Override
        public DepositLimitRule mapRow(ResultSet resultSet, int i) throws SQLException {
            DepositLimitRule depositLimitRule = new DepositLimitRule();
            depositLimitRule.setDepositLimitRuleId(resultSet.getInt("DEPOSIT_LIMIT_RULE_ID"));
            depositLimitRule.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            depositLimitRule.setItemCode(resultSet.getInt("ITEM_CODE"));
            depositLimitRule.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            depositLimitRule.setLimitType(StringUtil.firstOfString(resultSet.getString("LIMIT_TYPE")));
            return depositLimitRule;
        }
    }


    //储值方式和账本科目对应关系结果集
    class PaymentDepositMapper implements RowMapper<PaymentDeposit> {
        @Override
        public PaymentDeposit mapRow(ResultSet resultSet, int i) throws SQLException {
            PaymentDeposit paymentDeposit = new PaymentDeposit();
            paymentDeposit.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            paymentDeposit.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            paymentDeposit.setRuleId(resultSet.getInt("RULE_ID"));
            paymentDeposit.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            paymentDeposit.setInvoiceTag(StringUtil.firstOfString(resultSet.getString("INVOICE_TAG")));
            paymentDeposit.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            return paymentDeposit;
        }
    }
}

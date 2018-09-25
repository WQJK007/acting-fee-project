package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.acting.fee.dao.FeeUserOtherInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资料先关数据表操作
 *
 * @author Wangkh
 */
@Repository
public class FeeUserOtherInfoDaoImpl extends JdbcBaseDao implements FeeUserOtherInfoDao {
    @Override
    public boolean isBadBillUser(String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_F_BADBILL_USERINFO WHERE ACCT_ID=:VACCT_ID ");
        sql.append("AND ACT_TAG='1'");
        Map<String, String> param = Collections.singletonMap("VACCT_ID", acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForList(sql.toString(), param, String.class);
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isNoCalcLateFeeUser(String userId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_F_NOCALCLATEFEE_USER WHERE USER_ID = :VUSER_ID ");
        sql.append("AND ACCT_ID =:VACCT_ID AND SYSDATE()>=START_TIME AND SYSDATE()<=END_TIME ");
        Map<String, String> param = new HashMap(2);
        param.put("VUSER_ID", userId);
        param.put("VACCT_ID", acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForList(sql.toString(), param, String.class);
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }

}

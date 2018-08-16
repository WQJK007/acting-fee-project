package com.unicom.acting.fee.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.dao.CommparaFeeDao;
import com.unicom.acting.fee.domain.CommPara;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommparaFeeDaoImpl extends JdbcBaseDao implements CommparaFeeDao {
    @Override
    public List<CommPara> getCommpara(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,PARA_ATTR,PARA_CODE,PARA_NAME,");
        sql.append("PARA_CODE1,PARA_CODE2,PARA_CODE3,PARA_CODE4,PARA_CODE5,PARA_CODE6,");
        sql.append("DATE_FORMAT(PARA_DATE7,'%Y-%m-%d %T') PARA_DATE7,");
        sql.append("DATE_FORMAT(PARA_DATE8,'%Y-%m-%d %T') PARA_DATE8,");
        sql.append("DATE_FORMAT(PARA_DATE9,'%Y-%m-%d %T') PARA_DATE9,");
        sql.append("DATE_FORMAT(PARA_DATE10,'%Y-%m-%d %T') PARA_DATE10,");
        sql.append("DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,");
        sql.append("USE_TAG,REMARK,UPDATE_EPARCHY_CODE,UPDATE_CITY_CODE,");
        sql.append("UPDATE_DEPART_ID,UPDATE_STAFF_ID FROM TD_B_COMMPARA");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new CommparaMapper());
    }

    @Override
    public String getParamTimeStamp(String tagCode, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DATE_FORMAT(PARA_DATE7,'%Y-%m-%d %T') PARA_DATE7 ");
        sql.append("FROM TD_B_COMMPARA WHERE PARA_CODE= :VPARA_CODE");
        Map<String, String> param = new HashMap<>();
        param.put("VPARA_CODE", tagCode);
        List<String> result = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if(!result.isEmpty()) {
            return result.get(0);
        }
        return "";
    }

    @Override
    public CommPara getCommpara(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,PARA_ATTR,PARA_CODE,PARA_NAME,");
        sql.append("PARA_CODE1,PARA_CODE2,PARA_CODE3,PARA_CODE4,PARA_CODE5,PARA_CODE6,");
        sql.append("DATE_FORMAT(PARA_DATE7,'%Y-%m-%d %T') PARA_DATE7,");
        sql.append("DATE_FORMAT(PARA_DATE8,'%Y-%m-%d %T') PARA_DATE8,");
        sql.append("DATE_FORMAT(PARA_DATE9,'%Y-%m-%d %T') PARA_DATE9,");
        sql.append("DATE_FORMAT(PARA_DATE10,'%Y-%m-%d %T') PARA_DATE10,");
        sql.append("DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,");
        sql.append("UPDATE_EPARCHY_CODE,UPDATE_CITY_CODE,UPDATE_DEPART_ID,USE_TAG,REMARK,");
        sql.append("UPDATE_STAFF_ID FROM TD_B_COMMPARA WHERE PARA_CODE = :VPARA_CODE ");
        sql.append("AND EPARCHY_CODE = :VEPARCHY_CODE ");
        sql.append("AND PROVINCE_CODE = :VPROVINCE_CODE ");
        Map param = new HashMap(3);
        param.put("VPARA_CODE", paraCode);
        param.put("VEPARCHY_CODE", eparchyCode);
        param.put("VPROVINCE_CODE", provinceCode);
        List<CommPara> resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }

        //如果没有查询到结果，EPARCHY_CODE取默认ZZZZ
        param.put("VEPARCHY_CODE", "ZZZZ");
        resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }

        //如果没有查询到结果，PROVINCE_CODE取默认ZZZZ
        param.put("VPROVINCE_CODE", "ZZZZ");
        resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public CommPara getCommparaByLike(String paraCode, String provinceCode, String eparchyCode, String provinceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE,EPARCHY_CODE,PARA_ATTR,PARA_CODE,PARA_NAME,");
        sql.append("PARA_CODE1,PARA_CODE2,PARA_CODE3,PARA_CODE4,PARA_CODE5,PARA_CODE6,");
        sql.append("DATE_FORMAT(PARA_DATE7,'%Y-%m-%d %T') PARA_DATE7,");
        sql.append("DATE_FORMAT(PARA_DATE8,'%Y-%m-%d %T') PARA_DATE8,");
        sql.append("DATE_FORMAT(PARA_DATE9,'%Y-%m-%d %T') PARA_DATE9,");
        sql.append("DATE_FORMAT(PARA_DATE10,'%Y-%m-%d %T') PARA_DATE10,");
        sql.append("DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE,");
        sql.append("DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE,");
        sql.append("DATE_FORMAT(UPDATE_TIME,'%Y-%m-%d %T') UPDATE_TIME,");
        sql.append("UPDATE_EPARCHY_CODE,UPDATE_CITY_CODE,UPDATE_DEPART_ID,USE_TAG,REMARK,");
        sql.append("UPDATE_STAFF_ID FROM TD_B_COMMPARA ");
        sql.append("WHERE PARA_CODE LIKE :VPARA_CODE"+"%");
        sql.append("AND EPARCHY_CODE = :VEPARCHY_CODE ");
        sql.append("AND PROVINCE_CODE = :VPROVINCE_CODE ");
        Map param = new HashMap(3);
        param.put("VPARA_CODE", paraCode);
        param.put("VEPARCHY_CODE", eparchyCode);
        param.put("VPROVINCE_CODE", provinceCode);
        List<CommPara> resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }

        //如果没有查询到结果，EPARCHY_CODE取默认ZZZZ
        param.put("VEPARCHY_CODE", "ZZZZ");
        resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }

        //如果没有查询到结果，PROVINCE_CODE取默认ZZZZ
        param.put("VPROVINCE_CODE", "ZZZZ");
        resultList = this.getJdbcTemplate(provinceId).query(sql.toString(), param, new CommparaMapper());
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public String getProvCodeByEparchyCode(String eparchyCode, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PROVINCE_CODE FROM TD_B_EPARCHY WHERE EPARCHY_CODE = :VEPARCHY_CODE");
        Map param = new HashMap(1);
        param.put("VEPARCHY_CODE", eparchyCode);
        List<String> result = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return "";
    }

    @Override
    public String getParentTypeCode(String netTypeCode, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PARENT_TYPE_CODE FROM TD_S_NETCODE ");
        sql.append("WHERE NET_TYPE_CODE = :VNET_TYPE_CODE");
        Map<String, String> param = new HashMap<>();
        param.put("VNET_TYPE_CODE", netTypeCode);
        List<String> results = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(results)) {
            return results.get(0);
        }
        return "";
    }

    //公共参数结果集类
    class CommparaMapper implements RowMapper<CommPara> {
        @Override
        public CommPara mapRow(ResultSet resultSet, int i) throws SQLException {
            CommPara pCommPara = new CommPara();
            pCommPara.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            pCommPara.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            pCommPara.setParaAttr(resultSet.getInt("PARA_ATTR"));
            pCommPara.setParaCode(resultSet.getString("PARA_CODE"));
            pCommPara.setParaName(resultSet.getString("PARA_NAME"));
            pCommPara.setParaCode1(resultSet.getString("PARA_CODE1"));
            pCommPara.setParaCode2(resultSet.getString("PARA_CODE2"));
            pCommPara.setParaCode3(resultSet.getString("PARA_CODE3"));
            pCommPara.setParaCode4(resultSet.getString("PARA_CODE4"));
            pCommPara.setParaCode5(resultSet.getString("PARA_CODE5"));
            pCommPara.setParaCode6(resultSet.getString("PARA_CODE6"));
            pCommPara.setParaDate7(resultSet.getString("PARA_DATE7"));
            pCommPara.setParaDate8(resultSet.getString("PARA_DATE8"));
            pCommPara.setParaDate9(resultSet.getString("PARA_DATE9"));
            pCommPara.setParaDate10(resultSet.getString("PARA_DATE10"));
            pCommPara.setStartDate(resultSet.getString("START_DATE"));
            pCommPara.setEndDate(resultSet.getString("END_DATE"));
            pCommPara.setUseTag(StringUtil.firstOfString(resultSet.getString("USE_TAG")));
            return pCommPara;
        }
    }
}

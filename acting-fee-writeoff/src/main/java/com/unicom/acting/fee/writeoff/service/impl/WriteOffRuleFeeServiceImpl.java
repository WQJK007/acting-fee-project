package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.acting.fee.dao.*;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.WriteOffRuleFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 销账规则参数加载服务
 *
 * @author Administrators
 */
@Service
public class WriteOffRuleFeeServiceImpl implements WriteOffRuleFeeService {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffRuleFeeServiceImpl.class);
    @Autowired
    private RuleEparchyFeeDao ruleEparchyFeeDao;
    @Autowired
    private ItemFeeDao itemFeeDao;
    @Autowired
    private DepositParamFeeDao depositParamFeeDao;
    @Autowired
    private CycleFeeDao cycleFeeDao;
    @Autowired
    private CommparaFeeDao commparaFeeDao;


    @Override
    public void loadWriteOffParam(String provinceCode) {
        //地市销账规则
        List<RuleEparchy> vRuleEparchy = ruleEparchyFeeDao.getRuleEparchy(provinceCode);
        logger.debug("地市销账规则参数 vRuleEparchy.size = " + vRuleEparchy.size());

        //账目项优先级
        List<ItemPriorRule> vItemPriorRule = itemFeeDao.getItemPriorRule(provinceCode);
        logger.debug("账目项优先级规则参数 vItemPriorRule.size = " + vItemPriorRule.size());

        //明细账目项
        List<DetailItem> vDetailItem = itemFeeDao.getDetailItem(provinceCode);
        logger.debug("明细账目项参数 vDetailItem.size = " + vDetailItem.size());

        //组合账目项
        List<CompItem> vCompItem = itemFeeDao.getCompItem(provinceCode);
        logger.debug("组合账目项参数 vCompItem.size = " + vCompItem.size());

        //账本科目优先级
        List<DepositPriorRule> vDepositPriorRule = depositParamFeeDao.getDepositPriorRule(provinceCode);
        logger.debug("账本科目优先规则参数 vDepositPriorRule.size = " + vDepositPriorRule.size());

        //账本科目限定关系
        List<DepositLimitRule> vDepositLimitRule = depositParamFeeDao.getDepositLimitRule(provinceCode);
        logger.debug("账本科目限定规则参数 vDepositLimitRule.size = " + vDepositLimitRule.size());

        //储值方式和账本科目关系
        List<PaymentDeposit> vPaymentDeposit = depositParamFeeDao.getPaymentDeposit(provinceCode);
        logger.debug("储值方式和账本科目映射关系参数 vPaymentDeposit.size = " + vPaymentDeposit.size());

        //帐务通用参数
        List<CommPara> vCommPara = commparaFeeDao.getCommpara(provinceCode);
        logger.debug("帐务通用参数 vCommPara.size = " + vCommPara.size());

        //滞纳金计算参数
        List<LateCalPara> vLateCalPara = ruleEparchyFeeDao.getLateCalPara(provinceCode);
        logger.debug("滞纳金计算参数 vLateCalPara.size = " + vLateCalPara.size());

        //账期
        List<Cycle> vCycle = cycleFeeDao.getCycle(provinceCode);
        logger.debug("账期参数 vCycle.size = " + vCycle.size());

        WriteOffRuleStaticInfo.setAllVRuleeparchy(vRuleEparchy);
        WriteOffRuleStaticInfo.setAllMMItemPriorRule(vItemPriorRule);
        WriteOffRuleStaticInfo.setAllMPDetailItem(vDetailItem);
        WriteOffRuleStaticInfo.setAllMVCompItem(vCompItem);
        WriteOffRuleStaticInfo.setAllMMDepositPriorRule(vDepositPriorRule);
        WriteOffRuleStaticInfo.setAllMVDepositLimitRule(vDepositLimitRule);
        WriteOffRuleStaticInfo.setAllMVPaymentDeposit(vPaymentDeposit);
        WriteOffRuleStaticInfo.setAllMCycle(vCycle);
        WriteOffRuleStaticInfo.setAllMMCommpara(vCommPara);
        WriteOffRuleStaticInfo.setAllMVLateCalPara(vLateCalPara);
    }

    @Override
    public void getWriteOffRule(WriteOffRuleInfo writeOffRuleInfo, String eparchyCode, String provinceCode, String netTypeCode) {
        //帐目优先地市规则
        int ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
               ActPayPubDef.ITEM_PRIOR_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMMItemPriorRule().containsKey(ruleId)) {
                writeOffRuleInfo.setItemPriorRuleId(ruleId);
                writeOffRuleInfo.setItemPriorRuleMap(WriteOffRuleStaticInfo.getAllMMItemPriorRule().get(ruleId));
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        }else{
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //帐本科目优先规则
        ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
               ActPayPubDef.DEPOSIT_PRIOR_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMMDepositPriorRule().containsKey(ruleId)) {
                writeOffRuleInfo.setDepositPriorRuleId(ruleId);
                writeOffRuleInfo.setDepositPriorRuleMap(WriteOffRuleStaticInfo.getAllMMDepositPriorRule().get(ruleId));
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //账本科目限定
        ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
               ActPayPubDef.DEPOSIT_LIMIT_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMVDepositLimitRule().containsKey(ruleId)) {
                logger.debug("genDepositLimitRuleEparchy begin");
                writeOffRuleInfo.setDepositLimitRuleMap(genDepositLimitRuleEparchy(ruleId, WriteOffRuleStaticInfo.getAllMVDepositLimitRule().get(ruleId)));
                writeOffRuleInfo.setDepositLimitRuleId(ruleId);
                logger.debug("genDepositLimitRuleEparchy, allMPDetailItem.size = " + WriteOffRuleStaticInfo.getAllMPDetailItem().size() + ", DepositLimitRule.size = " + WriteOffRuleStaticInfo.getAllMVDepositLimitRule().get(ruleId).size());
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目限定地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目限定地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //储值方式和帐本类型
        ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
               ActPayPubDef.PAYMENT_DEPOSIT_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMVPaymentDeposit().containsKey(ruleId)) {
                writeOffRuleInfo.setPaymentDepositRuleId(ruleId);
                writeOffRuleInfo.setPaymentDepositList(WriteOffRuleStaticInfo.getAllMVPaymentDeposit().get(ruleId));
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置储值方式和账本科目关系地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置储值方式和帐本类型规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //滞纳金
        ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
               ActPayPubDef.LATEFEE_CALCPARA_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMVLateCalPara().containsKey(ruleId)) {
                writeOffRuleInfo.setLateCalParas(WriteOffRuleStaticInfo.getAllMVLateCalPara().get(ruleId));
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置违约金计算规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置违约金计算规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //全量参数
        if (WriteOffRuleStaticInfo.getAllMMCommpara().containsKey(provinceCode)) {
            if (WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).containsKey(eparchyCode)) {
                writeOffRuleInfo.setEparchyCommParaMap(WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).get(eparchyCode));
            }

            if (WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).containsKey(ActPayPubDef.DEFAULT_EPARCHY_CODE)) {
                writeOffRuleInfo.setProvCommParaMap(WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).get(ActPayPubDef.DEFAULT_EPARCHY_CODE));
            }
        }

        //没有配参数或者配置为1:不剔除实时账单，如果配置为0:剔除时账单
        CommPara tmpCommPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_CAN_PREREALBILL_CALC);
        if (tmpCommPara == null || "1".equals(tmpCommPara.getParaCode1())) {
            writeOffRuleInfo.setCanPrerealbillCalc(true);
        } else {
            writeOffRuleInfo.setCanPrerealbillCalc(false);
        }

        //设置销负账单的账本
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_NEGATIVEBILL_DEPOSIT);
        if (commPara == null) {
            writeOffRuleInfo.setNegativeBillDeposit(-1);
        } else {
            //如果配置的是空或者包含非数据，抛异常
            writeOffRuleInfo.setNegativeBillDeposit(Integer.parseInt(commPara.getParaCode1()));
        }

        //坏账计算滞纳金
        CommPara commPara1 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_BADBILL_CALC_LATEFEE);
        if (commPara1 != null && "1".equals(commPara1.getParaCode1())) {
            writeOffRuleInfo.setBadBillCalcLateFee(true);
        } else {
            writeOffRuleInfo.setBadBillCalcLateFee(false);
        }
    }


    /**
     * 根据地市，省份和网别获取销账规则类型对应的规则对象
     *
     * @param ruleList     地市销账规则列表
     * @param ruleType     销账规则类型
     * @param eparchyCode  账户归属地市
     * @param provinceCode 账户归属省份
     * @param netTypeCode  账户网别
     * @return 销账规则对象
     */
    private RuleEparchy getEparchyRuleInfo(List<RuleEparchy> ruleList, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        for (RuleEparchy ruleEparchy : ruleList) {
            if (ruleType == ruleEparchy.getRuleType()
                    && eparchyCode.equals(ruleEparchy.getEparchyCode())
                    && provinceCode.equals(ruleEparchy.getProvinceCode())
                    && netTypeCode.equals(ruleEparchy.getNetTypeCode())) {
                return ruleEparchy;
            }
        }
        return null;
    }


    /**
     * 根据地市，省份和网别获取销账规则类型对应的规则实例标识
     *
     * @param ruleList     地市销账规则列表
     * @param ruleType     销账规则类型
     * @param eparchyCode  账户归属地市
     * @param provinceCode 账户归属省份
     * @param netTypeCode  账户网别
     * @return 销账规则实例标识
     */
    private int getRuleEparchy(List<RuleEparchy> ruleList, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        int ruleId = -1;
        RuleEparchy rulePtr = getEparchyRuleInfo(ruleList, ruleType, eparchyCode, provinceCode, netTypeCode);
        if (rulePtr == null) {
            //取默认网别规则
            rulePtr = getEparchyRuleInfo(ruleList, ruleType, eparchyCode, provinceCode,ActPayPubDef.DEFAULT_NET_TYPE_CODE);
            //取默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,ActPayPubDef.DEFAULT_EPARCHY_CODE, provinceCode, netTypeCode);
            }
            //取默认地市默认网别规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,ActPayPubDef.DEFAULT_EPARCHY_CODE, provinceCode,ActPayPubDef.DEFAULT_NET_TYPE_CODE);
            }
            //取默认省别默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,ActPayPubDef.DEFAULT_EPARCHY_CODE,ActPayPubDef.DEFAULT_PROVINCE_CODE, netTypeCode);
            }
            //全默认优先级最低
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,ActPayPubDef.DEFAULT_EPARCHY_CODE,ActPayPubDef.DEFAULT_PROVINCE_CODE,ActPayPubDef.DEFAULT_NET_TYPE_CODE);
            }
        }

        if (rulePtr != null) {
            ruleId = rulePtr.getRuleId();
        }
        return ruleId;
    }

    /**
     * 获取地市帐本科目限定规则对象
     * @param ruleId 帐本科目限定地市规则实例标识
     * @param depositLimitRuleList 帐本科目限定地市规则列表
     * @return 该地市销账规则中配置的账本可以销的明细账目集
     */
    private Map<Integer, Set<Integer>> genDepositLimitRuleEparchy(int ruleId, List<DepositLimitRule> depositLimitRuleList) {
        if(!CollectionUtils.isEmpty(WriteOffRuleStaticInfo.getAllMMDepositItemLimit())) {
            if (WriteOffRuleStaticInfo.getAllMMDepositItemLimit().containsKey(ruleId)) {
                logger.info("ruleId exists, rule_id = " + ruleId);
                return WriteOffRuleStaticInfo.getAllMMDepositItemLimit().get(ruleId);
            }
        } else {
            WriteOffRuleStaticInfo.setAllMMDepositItemLimit(new HashMap<>());
        }

        //构造账本科目限定列表 Map<存折类型,可销帐目列表>
        Map<Integer, Set<Integer>> depositLimitMap = new HashMap<>();
        //明细账目项
        Set<Integer> detailItemSet = WriteOffRuleStaticInfo.getAllMPDetailItem().keySet();
        //全量组合账目项
        Map<Integer, List<CompItem>> compItemMap = WriteOffRuleStaticInfo.getAllMVCompItem();
        if (!CollectionUtils.isEmpty(depositLimitRuleList)) {
            for (DepositLimitRule depositLimitRule : depositLimitRuleList) {
                //账目项或账目组
                int itemCode = depositLimitRule.getItemCode();
                //账本科目
                int depositCode = depositLimitRule.getDepositCode();
                //第一次循环或者该类型账本科目第一次循环
                if (CollectionUtils.isEmpty(depositLimitMap)
                        || !depositLimitMap.containsKey(depositCode)) {
                    depositLimitMap.put(depositCode, new HashSet<>());
                }

                //'0' 该存折不能销对应帐目
                if ('0' == depositLimitRule.getLimitMode()) {
                    if (depositLimitMap.containsKey(depositCode)
                            && CollectionUtils.isEmpty(depositLimitMap.get(depositCode))) {
                        Set<Integer> tmpDetailItem = new HashSet<>();
                        tmpDetailItem.addAll(detailItemSet);
                        depositLimitMap.put(depositCode, tmpDetailItem);
                    }

                    //限定帐目组  如果账本对组合账目项不能销账，组合账目中所有明细账目项都需要从限定对象中移除
                    if ('1' == depositLimitRule.getLimitType()) {
                        if (compItemMap.containsKey(itemCode)) {
                            List<CompItem> compItemList = compItemMap.get(itemCode);
                            for (CompItem pCompItem : compItemList) {
                                if (depositLimitMap.get(depositCode).contains(pCompItem.getSubItemId())) {
                                    depositLimitMap.get(depositCode).remove(pCompItem.getSubItemId());
                                }
                            }
                        }
                    } else {
                        //限定帐目  如果账本对明细账目不能销账，从限定对象中移除配置的明细帐目
                        if (depositLimitMap.get(depositCode).contains(itemCode)) {
                            depositLimitMap.get(depositCode).remove(itemCode);
                        }
                    }
                } else {
                    //'1'-该存折只能销对应帐目
                    if ('1' == depositLimitRule.getLimitType()) {
                        //限定帐目组
                        if (compItemMap.containsKey(itemCode)) {
                            List<CompItem> compItemList = compItemMap.get(itemCode);
                            for (CompItem pCompItem : compItemList) {
                                if (detailItemSet.contains(pCompItem.getSubItemId())) {
                                    depositLimitMap.get(depositCode).add(pCompItem.getSubItemId());
                                }
                            }
                        }
                    } else {
                        //限定明细帐目
                        if (detailItemSet.contains(itemCode)) {
                            depositLimitMap.get(depositCode).add(itemCode);
                        }
                    }
                }
            }
        }
        WriteOffRuleStaticInfo.getAllMMDepositItemLimit().put(ruleId, depositLimitMap);
        return WriteOffRuleStaticInfo.getAllMMDepositItemLimit().get(ruleId);
    }

}

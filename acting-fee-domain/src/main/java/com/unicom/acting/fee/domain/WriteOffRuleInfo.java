package com.unicom.acting.fee.domain;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 省份销账规则，非静态部分
 *
 * @author Wangkh
 */
public class WriteOffRuleInfo {
    private Logger logger = LoggerFactory.getLogger(WriteOffRuleInfo.class);
    /**
     * @see #DEPOSIT_PRIOR_RULE_TYPE
     * 帐本科目优先地市规则
     */
    public static final char DEPOSIT_PRIOR_RULE_TYPE = '0';
    /**
     * @see #DEPOSIT_LIMIT_RULE_TYPE
     * 帐本科目限定地市规则
     */
    public static final char DEPOSIT_LIMIT_RULE_TYPE = '1';
    /**
     * @see #ITEM_PRIOR_RULE_TYPE
     * 帐目优先地市规则
     */
    public static final char ITEM_PRIOR_RULE_TYPE = '2';
    /**
     * @see #PAYMENT_DEPOSIT_RULE_TYPE
     * 储值方式和帐本类型
     */
    public static final char PAYMENT_DEPOSIT_RULE_TYPE = '3';
    /**
     * @see #LATEFEE_CALCPARA_RULE_TYPE
     * 滞纳金计算规则
     */
    public static final char LATEFEE_CALCPARA_RULE_TYPE = 'B';

    /**
     * @see #DEFAULT_PROVINCE_CODE
     * 默认省份编码
     */
    public static final String DEFAULT_PROVINCE_CODE = "ZZZZ";
    /**
     * @see #DEFAULT_EPARCHY_CODE
     * 默认地市编码
     */
    public static final String DEFAULT_EPARCHY_CODE = "ZZZZ";
    /**
     * @see #DEFAULT_NET_TYPE_CODE
     * 默认网别编码
     */
    public static final String DEFAULT_NET_TYPE_CODE = "ZZ";

    /**
     * @see #maxAcctCycle
     * 最大开账账期
     */
    private Cycle maxAcctCycle;
    /**
     * @see #curCycle
     * 当前账期
     */
    private Cycle curCycle;
    /**
     * @see #sysDate
     * 系统当前时间
     */
    private String sysDate;
    /**
     * @see #parity
     * 奇偶月
     */
    private int parity;
    /**
     * @see #isCycleSpecialState
     * 地市账期在特殊状态
     */
    private boolean isCycleSpecialState;
    /**
     * @see #itemPriorRuleId
     * 当前帐目优先级ID
     */
    private int itemPriorRuleId;
    /**
     * @see #depositLimitRuleId
     * 当前帐目限定规则ID
     */
    private int depositLimitRuleId;
    /**
     * @see #depositPriorRuleId
     * 当前帐本优先规则ID
     */
    private int depositPriorRuleId;
    /**
     * @see #paymentDepositRuleId
     * 储值方式和帐本类型规则ID
     */
    private int paymentDepositRuleId;
    /**
     * @see #canPrerealbillCalc
     * 是否剔除实时账单
     */
    private boolean canPrerealbillCalc;
    /**
     * @see #negativeBillDeposit
     * 负账单转换目的账本
     */
    private int negativeBillDeposit;
    /**
     * @see #badBillCalcLateFee
     * 坏账计算滞纳金
     */
    private boolean badBillCalcLateFee;
    /**
     * @see #eparchyCommParaMap
     * 匹配省份编码和地州编码的公共参数，地市编码不是ZZZZ
     * Map<PARA_CODE, CommPara>
     */
    private Map<String, CommPara> eparchyCommParaMap;
    /**
     * @see #provCommParaMap
     * 匹配省份编码和默认地州编码ZZZZ
     * Map<PARA_CODE, CommPara>
     */
    private Map<String, CommPara> provCommParaMap;
    /**
     * @see #itemPriorRuleMap
     * 账目优先级
     * Map<账目项编码,账目项优先级属性>
     */
    private Map<Integer, ItemPriorRule> itemPriorRuleMap;
    /**
     * @see #paymentDepositList
     * 储值方式和帐本类型关系
     */
    private List<PaymentDeposit> paymentDepositList;
    /**
     * @see #depositPriorRuleMap
     * 账本优先级
     * Map<账本项编码,账本优先级属性>
     */
    private Map<Integer, DepositPriorRule> depositPriorRuleMap;
    /**
     * @see #depositLimitRuleMap
     * 当前地市帐本类型账目限定关系
     * Map<账本类型, Set<账目项>>
     */
    private Map<Integer, Set<Integer>> depositLimitRuleMap;
    /**
     * @see #allMMDepositItemLimitPtr
     * 全量帐本类型账目限定
     * Map<账本类型, Map<账本类型, Set<账目项>>>
     */
    private Map<Integer, Map<Integer, Set<Integer>>> allMMDepositItemLimitPtr;
    /**
     * @see #lateCalParas
     * 滞纳金
     */
    private List<LateCalPara> lateCalParas;

    public Cycle getMaxAcctCycle() {
        return maxAcctCycle;
    }

    public void setMaxAcctCycle(Cycle maxAcctCycle) {
        this.maxAcctCycle = maxAcctCycle;
    }

    public Cycle getCurCycle() {
        return curCycle;
    }

    public void setCurCycle(Cycle curCycle) {
        this.curCycle = curCycle;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public boolean isCycleSpecialState() {
        return isCycleSpecialState;
    }

    public void setCycleSpecialState(boolean cycleSpecialState) {
        isCycleSpecialState = cycleSpecialState;
    }

    public void setSysdate(String sysdate) {
        if (sysdate.length() != 19 || (!"-".equals(sysdate.substring(4, 5)) && !"-".equals(sysdate.substring(7, 8)))) {
            throw new SkyArkException("需要使用正确的日期格式!(yyyy-MM-dd HH:mm:ss) sysdate = " + sysdate);
        }
        this.sysDate = sysdate;
    }

    public String getSysdate() {
        return sysDate;
    }

    public Map<Integer, DepositPriorRule> getDepositPriorRuleMap() {
        return this.depositPriorRuleMap;
    }

    public void setDepositPriorRuleMap(Map<Integer, DepositPriorRule> depositPriorRuleMap) {
        this.depositPriorRuleMap = depositPriorRuleMap;
    }

    public Map<Integer, Set<Integer>> getDepositLimitRuleMap() {
        return depositLimitRuleMap;
    }

    public void setDepositLimitRuleMap(Map<Integer, Set<Integer>> depositLimitRuleMap) {
        this.depositLimitRuleMap = depositLimitRuleMap;
    }

    public int getPaymentDepositRuleId() {
        return paymentDepositRuleId;
    }

    public int getItemPriorRuleId() {
        return itemPriorRuleId;
    }

    public int getDepositLimitRuleId() {
        return depositLimitRuleId;
    }

    public int getDepositPriorRuleId() {
        return depositPriorRuleId;
    }

    public void setItemPriorRuleId(int itemPriorRuleId) {
        this.itemPriorRuleId = itemPriorRuleId;
    }

    public void setEparchyCommParaMap(Map<String, CommPara> eparchyCommParaMap) {
        this.eparchyCommParaMap = eparchyCommParaMap;
    }

    public void setProvCommParaMap(Map<String, CommPara> provCommParaMap) {
        this.provCommParaMap = provCommParaMap;
    }

    public Map<Integer, ItemPriorRule> getItemPriorRuleMap() {
        return itemPriorRuleMap;
    }

    public void setItemPriorRuleMap(Map<Integer, ItemPriorRule> itemPriorRuleMap) {
        this.itemPriorRuleMap = itemPriorRuleMap;
    }

    public void setDepositLimitRuleId(int depositLimitRuleId) {
        this.depositLimitRuleId = depositLimitRuleId;
    }

    public void setDepositPriorRuleId(int depositPriorRuleId) {
        this.depositPriorRuleId = depositPriorRuleId;
    }

    public void setPaymentDepositRuleId(int paymentDepositRuleId) {
        this.paymentDepositRuleId = paymentDepositRuleId;
    }

    public boolean isCanPrerealbillCalc() {
        return canPrerealbillCalc;
    }

    public void setCanPrerealbillCalc(boolean canPrerealbillCalc) {
        this.canPrerealbillCalc = canPrerealbillCalc;
    }

    public int getNegativeBillDeposit() {
        return negativeBillDeposit;
    }

    public void setNegativeBillDeposit(int negativeBillDeposit) {
        this.negativeBillDeposit = negativeBillDeposit;
    }

    public boolean isBadBillCalcLateFee() {
        return badBillCalcLateFee;
    }

    public void setBadBillCalcLateFee(boolean badBillCalcLateFee) {
        this.badBillCalcLateFee = badBillCalcLateFee;
    }

    /**
     * 根据参数编码获取账管公共参数
     *
     * @param paraCode
     * @return
     */
    public CommPara getCommpara(String paraCode) {
        if (StringUtil.isEmptyCheckNullStr(paraCode)) {
            return null;
        }

        if (this.eparchyCommParaMap != null && this.eparchyCommParaMap.containsKey(paraCode)) {
            return this.eparchyCommParaMap.get(paraCode);
        }

        if (this.provCommParaMap != null && this.provCommParaMap.containsKey(paraCode)) {
            return this.provCommParaMap.get(paraCode);
        }

        if (WriteOffRuleStaticInfo.getAllMMCommpara().containsKey(DEFAULT_PROVINCE_CODE)
                && WriteOffRuleStaticInfo.getAllMMCommpara().get(
                DEFAULT_PROVINCE_CODE).containsKey(DEFAULT_EPARCHY_CODE)) {
            return WriteOffRuleStaticInfo.getAllMMCommpara().get(
                    DEFAULT_PROVINCE_CODE).get(DEFAULT_EPARCHY_CODE).get(paraCode);
        }
        return null;
    }

    private RuleEparchy getEparchyRuleInfo(List<RuleEparchy> rules, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        for (RuleEparchy pRuleeparchy : rules) {
            if (ruleType == pRuleeparchy.getRuleType() && eparchyCode.equals(pRuleeparchy.getEparchyCode())
                    && provinceCode.equals(pRuleeparchy.getProvinceCode()) && netTypeCode.equals(pRuleeparchy.getNetTypeCode())) {
                return pRuleeparchy;
            }
        }
        return null;
    }

    private int getRuleEparchy(List<RuleEparchy> rules, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        int ruleId = -1;
        RuleEparchy rulePtr = getEparchyRuleInfo(rules, ruleType, eparchyCode, provinceCode, netTypeCode);
        if (rulePtr == null) {
            //取默认网别规则
            rulePtr = getEparchyRuleInfo(rules, ruleType, eparchyCode, provinceCode, DEFAULT_NET_TYPE_CODE);
            //取默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(rules, ruleType, DEFAULT_EPARCHY_CODE, provinceCode, netTypeCode);
            }
            //取默认地市默认网别规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(rules, ruleType, DEFAULT_EPARCHY_CODE, provinceCode, DEFAULT_NET_TYPE_CODE);
            }
            //取默认省别默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(rules, ruleType, DEFAULT_EPARCHY_CODE, DEFAULT_PROVINCE_CODE, netTypeCode);
            }
            //全默认优先级最低
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(rules, ruleType, DEFAULT_EPARCHY_CODE, DEFAULT_PROVINCE_CODE, DEFAULT_NET_TYPE_CODE);
            }
        }

        if (rulePtr != null) {
            ruleId = rulePtr.getRuleId();
        }
        return ruleId;
    }

    /**
     * 格式化账本销账账目项
     *
     * @param ruleId
     * @param vDepositLimitRule
     * @param eparchyCode
     */
    private void formatDepositlimitrule(int ruleId, List<DepositLimitRule> vDepositLimitRule, String eparchyCode) {
        //mMpAllDeposititemLimit 只存储账本可以销的明细账目集
        if (!CollectionUtils.isEmpty(allMMDepositItemLimitPtr)) {
            if (allMMDepositItemLimitPtr.containsKey(ruleId)) {
                depositLimitRuleMap = allMMDepositItemLimitPtr.get(ruleId);
                return;
            }
        } else {
            allMMDepositItemLimitPtr = new HashMap<>();
        }

        //构造限定帐目列表 Map<存折类型,可销帐目列表>
        Map<Integer, Set<Integer>> tmpEmpty = new HashMap<>();
        //构造本条销账规则对应的限定帐目列表  Map<销账规则,Map<存折类型,可销帐目列表>>
        allMMDepositItemLimitPtr.put(ruleId, tmpEmpty);

        for (DepositLimitRule pDepositlimitrule : vDepositLimitRule) {
            if (!tmpEmpty.containsKey(pDepositlimitrule.getDepositCode())) {
                tmpEmpty.put(pDepositlimitrule.getDepositCode(), new HashSet<>());
            }
        }
        //加载账本信息
        Set<Integer> setItem = new HashSet<>();
        //加载明细账目集
        setItem.clear();
        for (Map.Entry<Integer, DetailItem> entry : WriteOffRuleStaticInfo.getAllMPDetailItem().entrySet()) {
            setItem.add(entry.getKey());
        }

        //全量组合账目项
        Map<Integer, List<CompItem>> compItems = WriteOffRuleStaticInfo.getAllMVCompItem();
        for (DepositLimitRule pDepositlimitrule : vDepositLimitRule) {
            int itemCode = pDepositlimitrule.getItemCode();
            int depositCode = pDepositlimitrule.getDepositCode();
            //Set setItem  = keySet;
            //'0'-该存折不能销对应帐目
            if (pDepositlimitrule.getLimitMode() == '0') {
                //明细账目集
                if (tmpEmpty.containsKey(depositCode) && CollectionUtils.isEmpty(tmpEmpty.get(depositCode))) {
                    tmpEmpty.put(depositCode, setItem);
                }

                //限定帐目组  如果账本对组合账目项不能销账，组合账目中所有明细账目项都需要从限定对象中移除
                if ('1' == pDepositlimitrule.getLimitType()) {
                    if (compItems.containsKey(itemCode)) {
                        List<CompItem> tcomp = compItems.get(itemCode);
                        for (CompItem pCompItem : tcomp) {
                            if (tmpEmpty.get(depositCode).contains(pCompItem.getSubItemId())) {
                                tmpEmpty.get(depositCode).remove(pCompItem.getSubItemId());
                            }
                        }
                    }
                } else {
                    //限定帐目  如果账本对明细账目不能销账，从限定对象中移除配置的明细帐目
                    if (tmpEmpty.get(depositCode).contains(itemCode)) {
                        tmpEmpty.get(depositCode).remove(itemCode);
                    }
                }
            } else {
                //'1'-该存折只能销对应帐目
                if ('1' == pDepositlimitrule.getLimitType()) {
                    //限定帐目组
                    if (compItems.containsKey(itemCode)) {
                        List<CompItem> tcomp = compItems.get(itemCode);
                        for (CompItem pCompItem : tcomp) {
                            if (setItem.contains(pCompItem.getSubItemId())) {
                                tmpEmpty.get(depositCode).add(pCompItem.getSubItemId());
                            }
                        }
                    }
                } else {
                    //限定明细帐目
                    if (setItem.contains(itemCode)) {
                        tmpEmpty.get(depositCode).add(itemCode);
                    }
                }
            }
        }

        if (allMMDepositItemLimitPtr.containsKey(ruleId)) {
            depositLimitRuleMap = allMMDepositItemLimitPtr.get(ruleId);
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有对应地市的账本科目限定关系eparchyCode=" + eparchyCode);
        }
    }

    /**
     * 据地市编码,省份编码,网别类型获取对应的销账规则  放到其他对象中getWriteOffRule
     *
     * @param eparchyCode
     * @param provinceCode
     * @param netTypeCode
     */
    public void getWriteOffRule(String eparchyCode, String provinceCode, String netTypeCode) {
        int ruleId = -1;
        //账目优先
        if ((ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(), ITEM_PRIOR_RULE_TYPE, eparchyCode, provinceCode, netTypeCode)) > 0) {
            if (WriteOffRuleStaticInfo.getAllMMItemPriorRule().containsKey(ruleId)) {
                this.itemPriorRuleMap = WriteOffRuleStaticInfo.getAllMMItemPriorRule().get(ruleId);
                this.itemPriorRuleId = ruleId;
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //帐本科目优先
        if ((ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(), DEPOSIT_PRIOR_RULE_TYPE, eparchyCode, provinceCode, netTypeCode)) > 0) {
            if (WriteOffRuleStaticInfo.getAllMMDepositPriorRule().containsKey(ruleId)) {
                this.depositPriorRuleMap = WriteOffRuleStaticInfo.getAllMMDepositPriorRule().get(ruleId);
                this.depositPriorRuleId = ruleId;
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //账本科目限定 单独赋值ruleId
        if ((ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(), DEPOSIT_LIMIT_RULE_TYPE, eparchyCode, provinceCode, netTypeCode)) > 0) {
            if (WriteOffRuleStaticInfo.getAllMVDepositLimitRule().containsKey(ruleId)) {
                logger.debug("formatDepositlimitrule...");
                formatDepositlimitrule(ruleId, WriteOffRuleStaticInfo.getAllMVDepositLimitRule().get(ruleId), eparchyCode);
                this.depositLimitRuleId = ruleId;
                logger.debug("formatDepositlimitrule, allMPDetailItem.size = " + WriteOffRuleStaticInfo.getAllMPDetailItem().size() + ", DepositLimitRule.size = " + WriteOffRuleStaticInfo.getAllMVDepositLimitRule().get(ruleId).size());
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目限定地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目限定地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //储值方式和帐本类型
        if ((ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(), PAYMENT_DEPOSIT_RULE_TYPE, eparchyCode, provinceCode, netTypeCode)) > 0) {
            if (WriteOffRuleStaticInfo.getAllMVPaymentDeposit().containsKey(ruleId)) {
                this.paymentDepositList = WriteOffRuleStaticInfo.getAllMVPaymentDeposit().get(ruleId);
                this.paymentDepositRuleId = ruleId;
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置储值方式和账本科目关系地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置储值方式和帐本类型规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //滞纳金
        if ((ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(), LATEFEE_CALCPARA_RULE_TYPE, eparchyCode, provinceCode, netTypeCode)) > 0) {
            if (WriteOffRuleStaticInfo.getAllMVLateCalPara().containsKey(ruleId)) {
                this.lateCalParas = WriteOffRuleStaticInfo.getAllMVLateCalPara().get(ruleId);
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置违约金计算规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置违约金计算规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }

        //全量参数
        if (WriteOffRuleStaticInfo.getAllMMCommpara().containsKey(provinceCode)) {
            if (WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).containsKey(eparchyCode)) {
                this.eparchyCommParaMap = WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).get(eparchyCode);
            }

            if (WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).containsKey(DEFAULT_EPARCHY_CODE)) {
                this.provCommParaMap = WriteOffRuleStaticInfo.getAllMMCommpara().get(provinceCode).get(DEFAULT_EPARCHY_CODE);
            }
        }
    }

    public void setLateCalParas(List<LateCalPara> lateCalParas) {
        this.lateCalParas = lateCalParas;
    }

    /**
     * 获取滞纳金计算规则参数
     *
     * @param cycleId
     * @return
     */
    public LateCalPara getLateCalPara(int cycleId) {
        if (CollectionUtils.isEmpty(WriteOffRuleStaticInfo.getAllMVLateCalPara())) {
            throw new SkyArkException("没有配置滞纳金计算参数，请联系管理员!");
        }

        int i = 0;
        for (; i < lateCalParas.size(); i++) {
            if (lateCalParas.get(i).getStartCycleId() <= cycleId
                    && cycleId <= lateCalParas.get(i).getEndCycleId()) {
                return lateCalParas.get(i);
            }
        }

        if (i == lateCalParas.size()) {
            throw new SkyArkException("cycleId=" + cycleId + "没有配置账期对应的滞纳金计算参数，请联系管理员!");
        }
        return null;
    }

    /**
     * 根据账目项编码获取账目项优先级
     *
     * @param integrateItemCode
     * @return
     */
    public int findItemprior(int integrateItemCode) {
        if (this.itemPriorRuleMap == null) {
            return 9999;
        }
        if (this.itemPriorRuleMap.containsKey(integrateItemCode)) {
            return this.itemPriorRuleMap.get(integrateItemCode).getItemPriority();
        } else {
            return 9999;
        }
    }

    /**
     * 根据账本类型编码校验账本是否可合并,如果可以合并返回true,如果可用返回false
     *
     * @param depositCode
     * @return
     */
    public boolean depositIfUnite(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return true;
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getIfUnite() == '1';
        }
        return true;
    }

    /**
     * 根据账本类型编码获取账本名称
     *
     * @param depositCode
     * @return
     */
    public String getDepositName(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return "";
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getDepositName();
        }
        return "";
    }

    /**
     * 根据账本类型编码校验账本类型优先级是否配置,如果配置了优先级规则返回true,否则返回false
     *
     * @param depositCode
     * @return
     */
    public boolean findDeposit(int depositCode) {
        return this.depositPriorRuleMap.containsKey(depositCode);
    }

    /**
     * 根据账本编码校验账本是普通赠款账本,如果是返回true,否则返回false
     *
     * @param depositCode
     * @return
     */
    public boolean isPresentDeposit(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return false;
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getDepositTypeCode() == '1';
        } else {
            throw new SkyArkException("参数问题，请联系管理员!无效账本科目类型depositCode= " + depositCode);
        }
    }

    /**
     * 根据账本编码获取账本类型,账本类型目前分为
     * 普通现金账本(0),
     * 冻结现金账本(1),
     * 普通赠款账本(2),
     * 冻结赠款账本(3)
     *
     * @param depositCode
     * @return
     */
    public char depositTypeCode(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return '*';
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getDepositTypeCode();
        }
        return '*';
    }

    //根据账本编码校验账本是可清退账本,如果可清退返回true,否则返回false
    public boolean isBackDeposit(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return false;
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getReturnTag() == '1';
        } else {
            throw new SkyArkException("参数问题，请联系管理员!无效账本科目类型depositCode= " + depositCode);
        }
    }

    //根据账本编码校验账本是托收类型账本,如果不是返回true,否则返回false
    public boolean isConsignTag(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return false;
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getCanConsignTag() == '0';
        } else {
            throw new SkyArkException("参数问题，请联系管理员!无效账本科目类型depositCode= " + depositCode);
        }
    }

    //根据账本编码校验账本是可转账账本,如果是返回true,否则返回false
    public boolean isTransDeposit(int depositCode) {
        if (this.depositPriorRuleMap == null) {
            return false;
        }
        if (this.depositPriorRuleMap.containsKey(depositCode)) {
            return this.depositPriorRuleMap.get(depositCode).getCanTranTag() == '1';
        } else {
            throw new SkyArkException("参数问题，请联系管理员!无效账本科目类型depositCode= " + depositCode);
        }
    }

    //根据账本编码校验账本是否销账可用,如果不可用返回true,否则返回false
    public boolean isCannotUseDeposit(int depositCode) {
        if (this.depositLimitRuleMap == null) {
            return false;
        }
        if (this.depositLimitRuleMap.containsKey(depositCode)) {
            return this.depositLimitRuleMap.get(depositCode).isEmpty();
        } else {
            return false;
        }
    }

    //根据账本编码校验账本是否存在销账范围限制,如果存在返回true,否则返回false
    public boolean isLimitDeposit(int depositCode) {
        if (this.depositLimitRuleMap == null) {
            return false;
        }
        if (this.depositLimitRuleMap.containsKey(depositCode)) {
            return this.depositLimitRuleMap.get(depositCode).size() != WriteOffRuleStaticInfo.getAllMPDetailItem().size();
        } else {
            return false;
        }
    }

    //根据储值方式和账本科目校验是否存在关系映射,如果存在返回true,如果不存在返回false
    public PaymentDeposit getPaymentDeposit(int paymentId, int payFeeModeCode) {
        if (this.paymentDepositList == null) {
            return null;
        }
        for (PaymentDeposit paymentDeposit : this.paymentDepositList) {
            if (paymentDeposit.getPaymentId() == paymentId && paymentDeposit.getPayFeeModeCode() == payFeeModeCode) {
                return paymentDeposit;
            }
        }
        return null;
    }

    public void setPaymentDepositList(List<PaymentDeposit> paymentDepositList) {
        this.paymentDepositList = paymentDepositList;
    }

    /**
     * 根据账目项编码获取账目项优先级对象信息
     *
     * @param itemPriorRuleId
     */
    public void getItempriorrule(int itemPriorRuleId) {
        if (WriteOffRuleStaticInfo.getAllMMItemPriorRule().containsKey(itemPriorRuleId)) {
            this.itemPriorRuleMap = WriteOffRuleStaticInfo.getAllMMItemPriorRule().get(itemPriorRuleId);
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐目优先规则参数ruleId= " + itemPriorRuleId);
        }
    }

    public List<LateCalPara> getLateCalParas() {
        return lateCalParas;
    }

    //是否抵扣或补收期间
    public boolean isSpecialRecvState(Cycle cycle) {
        return (isDrecvPeriod(cycle) || isPatchDrecvPeriod(cycle));
    }

    /**
     * 是否抵扣期
     *
     * @param curCycle
     * @return
     */
    public boolean isDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 5 || getMonthAcctStatus(curCycle) < 0
                || getMonthAcctStatus(curCycle) >= 20 && getMonthAcctStatus(curCycle) < 90);
    }

    /**
     * 是否补收期间
     *
     * @param curCycle
     * @return
     */
    public boolean isPatchDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 8 || getMonthAcctStatus(curCycle) == 90);
    }

    /**
     * 当前账期状态
     *
     * @param curCycle
     * @return
     */
    private long getMonthAcctStatus(Cycle curCycle) {
        return Long.valueOf(curCycle.getMonthAcctStatus());
    }

}

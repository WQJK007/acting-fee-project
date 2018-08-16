package com.unicom.acting.fee.domain;

import com.unicom.skyark.component.exception.SkyArkException;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 销账规则静态参数对象
 *
 * @author Wangkh
 */
public class WriteOffRuleStaticInfo {
    private WriteOffRuleStaticInfo() {
    }

    /**
     * @see #allVRuleeparchy
     * 全量销账规则
     */
    private static List<RuleEparchy> allVRuleeparchy;
    /**
     * @see #allMCycle
     * 全量账期
     */
    private static Map<Integer, Cycle> allMCycle;
    /**
     * @see #allMMCommpara
     * 匹配省份编码地市编码和公共参数
     */
    private static Map<String, Map<String, Map<String, CommPara>>> allMMCommpara;
    /**
     * @see #allMPDetailItem
     * 明细账目项  Map<账目项编码,账目项属性>
     */
    private static Map<Integer, DetailItem> allMPDetailItem;
    /**
     * @see #allMVCompItem
     * 组合帐目全集	 Map<组合账目项编码,List<组合账目项属性>>
     */
    private static Map<Integer, List<CompItem>> allMVCompItem;
    /**
     * @see #allMMItemPriorRule
     * 全量账目优先级  Map<销账规则,Map<账目项编码,账目项优先级属性>>
     */
    private static Map<Integer, Map<Integer, ItemPriorRule>> allMMItemPriorRule;
    /**
     * @see #allMVPaymentDeposit
     * 全量储值方式和帐本类型关系
     */
    private static Map<Integer, List<PaymentDeposit>> allMVPaymentDeposit;
    /**
     * @see #allMMDepositPriorRule
     * 全量账本优先级  Map<销账规则,Map<账本项编码,账本优先级属性>>
     */
    private static Map<Integer, Map<Integer, DepositPriorRule>> allMMDepositPriorRule;
    /**
     * @see #allMVDepositLimitRule
     * 全量帐本类型限定关系    Map<账本类型, List<帐本类型账目限定关系>>
     */
    private static Map<Integer, List<DepositLimitRule>> allMVDepositLimitRule;
    /**
     * @see #allMVLateCalPara
     * 全量滞纳金计算规则 Map<销账规则, List<滞纳金计算规则>>
     */
    private static Map<Integer, List<LateCalPara>> allMVLateCalPara;
    /**
     * @see #allMMDepositItemLimit
     * 全量帐本类型账目限定  Map<账本类型, Map<账本类型, Set<账目项>>>
     */
    private static Map<Integer, Map<Integer, Set<Integer>>> allMMDepositItemLimit;

    //销账规则参数
    public static void setAllVRuleeparchy(List<RuleEparchy> vRuleEparchy) {
        if (!CollectionUtils.isEmpty(vRuleEparchy)) {
            allVRuleeparchy = vRuleEparchy;
        } else {
            allVRuleeparchy = new ArrayList<>();
        }
    }

    //明细帐目参数
    public static void setAllMPDetailItem(List<DetailItem> vDetailItem) {
        if (!CollectionUtils.isEmpty(allMPDetailItem)) {
            allMPDetailItem.clear();
        } else {
            allMPDetailItem = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vDetailItem)) {
            for (DetailItem pDetailItem : vDetailItem) {
                allMPDetailItem.put(pDetailItem.getItemId(), pDetailItem);
            }
        }
    }

    //组合帐目
    public static void setAllMVCompItem(List<CompItem> vCompItem) {
        if (!CollectionUtils.isEmpty(allMVCompItem)) {
            allMVCompItem.clear();
        } else {
            allMVCompItem = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vCompItem)) {
            for (CompItem pCompItem : vCompItem) {
                if (!allMVCompItem.containsKey(pCompItem.getItemId())) {
                    List<CompItem> tmpCompItems = new ArrayList<>();
                    tmpCompItems.add(pCompItem);
                    allMVCompItem.put(pCompItem.getItemId(), tmpCompItems);
                } else {
                    allMVCompItem.get(pCompItem.getItemId()).add(pCompItem);
                }
            }
        }
    }

    //帐目优先规则
    public static void setAllMMItemPriorRule(List<ItemPriorRule> vItemPriorRule) {
        if (!CollectionUtils.isEmpty(allMMItemPriorRule)) {
            allMMItemPriorRule.clear();
        } else {
            allMMItemPriorRule = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vItemPriorRule)) {
            for (ItemPriorRule pItempriorrule : vItemPriorRule) {
                if (allMMItemPriorRule.containsKey(pItempriorrule.getItemPriorRuleId())) {
                    allMMItemPriorRule.get(pItempriorrule.getItemPriorRuleId()).put(pItempriorrule.getItemCode(), pItempriorrule);
                } else {
                    Map<Integer, ItemPriorRule> tmpMap = new HashMap<>();
                    tmpMap.put(pItempriorrule.getItemCode(), pItempriorrule);
                    allMMItemPriorRule.put(pItempriorrule.getItemPriorRuleId(), tmpMap);
                }
            }
        }
    }

    //存折优先规则
    public static void setAllMMDepositPriorRule(List<DepositPriorRule> vDepositPriorRule) {
        if (!CollectionUtils.isEmpty(allMMDepositPriorRule)) {
            allMMDepositPriorRule.clear();
        } else {
            allMMDepositPriorRule = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vDepositPriorRule)) {
            for (DepositPriorRule pDepositpriorrule : vDepositPriorRule) {
                if (allMMDepositPriorRule.containsKey(pDepositpriorrule.getDepositPriorRuleId())) {
                    allMMDepositPriorRule.get(pDepositpriorrule.getDepositPriorRuleId()).put(pDepositpriorrule.getDepositCode(), pDepositpriorrule);
                } else {
                    Map<Integer, DepositPriorRule> tmpMap = new HashMap<>();
                    tmpMap.put(pDepositpriorrule.getDepositCode(), pDepositpriorrule);
                    allMMDepositPriorRule.put(pDepositpriorrule.getDepositPriorRuleId(), tmpMap);
                }
            }
        }
    }

    //账本限定规则
    public static void setAllMVDepositLimitRule(List<DepositLimitRule> vDepositLimitRule) {
        if (!CollectionUtils.isEmpty(allMVDepositLimitRule)) {
            allMVDepositLimitRule.clear();
        } else {
            allMVDepositLimitRule = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vDepositLimitRule)) {
            for (DepositLimitRule pDepositlimitrule : vDepositLimitRule) {
                if (allMVDepositLimitRule.containsKey(pDepositlimitrule.getDepositLimitRuleId())) {
                    allMVDepositLimitRule.get(pDepositlimitrule.getDepositLimitRuleId()).add(pDepositlimitrule);
                } else {
                    List<DepositLimitRule> tmpList = new ArrayList<>();
                    tmpList.add(pDepositlimitrule);
                    allMVDepositLimitRule.put(pDepositlimitrule.getDepositLimitRuleId(), tmpList);
                }
            }
        }
    }

    //储值方式和帐本类型关系
    public static void setAllMVPaymentDeposit(List<PaymentDeposit> vPaymentDeposit) {
        if (!CollectionUtils.isEmpty(allMVPaymentDeposit)) {
            allMVPaymentDeposit.clear();
        } else {
            allMVPaymentDeposit = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vPaymentDeposit)) {
            for (PaymentDeposit pPaymentDeposit : vPaymentDeposit) {
                if (allMVPaymentDeposit.containsKey(pPaymentDeposit.getRuleId())) {
                    allMVPaymentDeposit.get(pPaymentDeposit.getRuleId()).add(pPaymentDeposit);
                } else {
                    List<PaymentDeposit> tmpList = new ArrayList<>();
                    tmpList.add(pPaymentDeposit);
                    allMVPaymentDeposit.put(pPaymentDeposit.getRuleId(), tmpList);
                }
            }
        }
    }

    //全量参数
    public static void setAllMMCommpara(List<CommPara> vCommPara) {
        if (!CollectionUtils.isEmpty(allMMCommpara)) {
            allMMCommpara.clear();
        } else {
            allMMCommpara = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vCommPara)) {
            for (CommPara pCommPara : vCommPara) {
                String provinceCode = pCommPara.getProvinceCode();
                if (allMMCommpara.containsKey(provinceCode)) {
                    String eparchyCode = pCommPara.getEparchyCode();
                    if (allMMCommpara.get(provinceCode).containsKey(eparchyCode)) {
                        allMMCommpara.get(provinceCode).get(eparchyCode).put(pCommPara.getParaCode(), pCommPara);
                    } else {
                        Map<String, CommPara> mTmp = new HashMap<>();
                        mTmp.put(pCommPara.getParaCode(), pCommPara);
                        allMMCommpara.get(pCommPara.getProvinceCode()).put(eparchyCode, mTmp);
                    }
                } else {
                    Map<String, CommPara> mTmp = new HashMap<>();
                    mTmp.put(pCommPara.getParaCode(), pCommPara);
                    Map<String, Map<String, CommPara>> mmTmp = new HashMap<>();
                    mmTmp.put(pCommPara.getEparchyCode(), mTmp);
                    allMMCommpara.put(provinceCode, mmTmp);
                }
            }
        }
    }

    //账期参数
    public static void setAllMCycle(List<Cycle> vCycle) {
        if (!CollectionUtils.isEmpty(allMCycle)) {
            allMCycle.clear();
        } else {
            allMCycle = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vCycle)) {
            for (Cycle pCycle : vCycle) {
                allMCycle.put(pCycle.getCycleId(), pCycle);
            }
        }
    }

    //滞纳金参数
    public static void setAllMVLateCalPara(List<LateCalPara> vLateCalPara) {
        if (!CollectionUtils.isEmpty(allMVLateCalPara)) {
            allMVLateCalPara.clear();
        } else {
            allMVLateCalPara = new HashMap<>();
        }

        if (!CollectionUtils.isEmpty(vLateCalPara)) {
            for (LateCalPara lateCalPara : vLateCalPara) {
                if (allMVLateCalPara.containsKey(lateCalPara.getRuleId())) {
                    allMVLateCalPara.get(lateCalPara.getRuleId()).add(lateCalPara);
                } else {
                    List<LateCalPara> tmpList = new ArrayList<>();
                    tmpList.add(lateCalPara);
                    allMVLateCalPara.put(lateCalPara.getRuleId(), tmpList);
                }
            }
        }
    }

    public static Map<Integer, Map<Integer, Set<Integer>>> getAllMMDepositItemLimit() {
        return allMMDepositItemLimit;
    }

    public static void setAllMMDepositItemLimit(Map<Integer, Map<Integer, Set<Integer>>> allMMDepositItemLimit) {
        WriteOffRuleStaticInfo.allMMDepositItemLimit = allMMDepositItemLimit;
    }

    //根据账目项编码获取账目项名称
    public static String getItemName(int itemCode) {
        if (allMPDetailItem.containsKey(itemCode)) {
            return allMPDetailItem.get(itemCode).getItemName();
        }
        return "";
    }

    //根据账目项编码检验账目项是否计算欠费,如果计算返回true,如果可用返回false
    public static boolean isOweItem(int itemCode) {
        if (allMPDetailItem.containsKey(itemCode)) {
            return (allMPDetailItem.get(itemCode).getOweTag() == '1'
                    || allMPDetailItem.get(itemCode).getOweTag() == '2');
        } else {
            return true;
        }
    }

    //校验账目项是否计算滞纳金,如果计算滞纳金返回true,否则返回false
    public static boolean isCalcLatefee(int itemCode) {
        if (!allMPDetailItem.containsKey(itemCode)) {
            throw new SkyArkException("没有配置帐目参数!itemCode = " + itemCode);
        }
        return (allMPDetailItem.get(itemCode).getLatefeeCalcTag() == '1');
    }

    //根据账期标识获取账期对象
    public static Cycle getCycle(int cycleId) {
        if (CollectionUtils.isEmpty(allMCycle)) {
            throw new SkyArkException("参数问题，请联系管理员!获取帐期失败!allMCycle is null ");
        }

        if (WriteOffRuleStaticInfo.allMCycle.containsKey(cycleId)) {
            return WriteOffRuleStaticInfo.allMCycle.get(cycleId);
        } else {
            throw new SkyArkException("参数问题，请联系管理员!获取帐期失败!cycleId= " + cycleId);
        }
    }

    public static List<RuleEparchy> getAllVRuleeparchy() {
        if (allVRuleeparchy == null) {
            return new ArrayList<>();
        } else {
            return allVRuleeparchy;
        }
    }

    public static Map<Integer, DetailItem> getAllMPDetailItem() {
        if (allMPDetailItem == null) {
            return new HashMap<>();
        } else {
            return allMPDetailItem;
        }
    }

    public static Map<Integer, Cycle> getAllMCycle() {
        if (allMCycle == null) {
            return new HashMap<>();
        } else {
            return allMCycle;
        }
    }

    public static Map<String, Map<String, Map<String, CommPara>>> getAllMMCommpara() {
        if (allMMCommpara == null) {
            return new HashMap<>();
        } else {
            return allMMCommpara;
        }
    }

    public static Map<Integer, List<CompItem>> getAllMVCompItem() {
        if (allMVCompItem == null) {
            return new HashMap<>();
        } else {
            return allMVCompItem;
        }
    }

    public static Map<Integer, Map<Integer, ItemPriorRule>> getAllMMItemPriorRule() {
        if (allMMItemPriorRule == null) {
            return new HashMap<>();
        } else {
            return allMMItemPriorRule;
        }
    }

    public static Map<Integer, List<PaymentDeposit>> getAllMVPaymentDeposit() {
        if (allMVPaymentDeposit == null) {
            return new HashMap<>();
        } else {
            return allMVPaymentDeposit;
        }
    }

    public static Map<Integer, Map<Integer, DepositPriorRule>> getAllMMDepositPriorRule() {
        if (allMMDepositPriorRule == null) {
            return new HashMap<>();
        } else {
            return allMMDepositPriorRule;
        }
    }

    public static Map<Integer, List<DepositLimitRule>> getAllMVDepositLimitRule() {
        if (allMVDepositLimitRule == null) {
            return new HashMap<>();
        } else {
            return allMVDepositLimitRule;
        }
    }

    public static Map<Integer, List<LateCalPara>> getAllMVLateCalPara() {
        if (allMVLateCalPara == null) {
            return new HashMap<>();
        } else {
            return allMVLateCalPara;
        }
    }

}

package com.unicom.acting.fee.owefee.service.impl;

import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.owefee.service.OweFeeService;
import com.unicom.acting.fee.writeoff.service.PayLogFeeService;
import com.unicom.acting.fee.writeoff.service.WriteOffFeeService;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.acts.pay.domain.AcctPaymentCycle;
import com.unicom.skyark.component.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 欠费查询公共方法
 *
 * @author Administrators
 */
@Service
public class OweFeeServiceImpl implements OweFeeService {
    private static final Logger logger = LoggerFactory.getLogger(OweFeeServiceImpl.class);
    @Autowired
    private WriteOffFeeService writeOffService;
    @Autowired
    private CalculateService calculateComponent;
    @Autowired
    private PayLogFeeService payLogFeeService;

    @Override
    public TradeCommInfoOut queryOweFee(TradeCommInfoIn tradeCommInfoIn) throws SkyArkException {
        TradeCommInfo tradeCommInfo = new TradeCommInfo();
        //查询用户资料
        writeOffService.genUserDatumInfo(tradeCommInfoIn, tradeCommInfo);
        //限制大合账用户查询
        if ("1".equals(tradeCommInfoIn.getTradeEnter()) && tradeCommInfoIn.isBigAcctRecvFee()) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "$$$30010$$$该账户为大合帐账户，请到大合帐页面进行处理!");
        }
        //查询账期信息
        writeOffService.genEparchyCycleInfo(tradeCommInfo,
                tradeCommInfoIn.getEparchyCode(), ActPayPubDef.ACT_RDS_DBCONN);
        //查询账本
        writeOffService.getAcctBalance(tradeCommInfoIn, tradeCommInfo);
        //查询账单
        writeOffService.getOweBill(tradeCommInfoIn, tradeCommInfo);
        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!writeOffService.ifCalcLateFee(tradeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            writeOffService.getDerateFeeLog(tradeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            writeOffService.getAcctPaymentCycle(tradeCommInfo, tradeCommInfo.getAccount().getAcctId(), ActPayPubDef.ACTS_DRDS_DBCONN);
        }
        //销账和结余计算
        calculateComponent.calc(tradeCommInfo);
        return genTradeCommInfoOut(tradeCommInfoIn, tradeCommInfo);
    }

    /**
     * 欠费查询信息规整
     *
     * @param tradeCommInfo
     * @return
     */
    private TradeCommInfoOut genTradeCommInfoOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        TradeCommInfoOut tradeCommInfoOut = new TradeCommInfoOut();
        //销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        WriteSnapLog snaplog = tradeCommInfo.getWriteSnapLog();

        //设置账期信息
        tradeCommInfoOut.setCurCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommInfoOut.setMaxAcctCycleId(writeOffRuleInfo.getMaxAcctCycle().getCycleId());

        //设置用户信息
        User mainUser = tradeCommInfo.getMainUser();
        tradeCommInfoOut.setEparchyCode(mainUser.getEparchyCode());
        tradeCommInfoOut.setSerialNumber(mainUser.getSerialNumber());
        tradeCommInfoOut.setNetTypeCode(mainUser.getNetTypeCode());
        tradeCommInfoOut.setUserId(mainUser.getUserId());
        tradeCommInfoOut.setBrandCode(mainUser.getBrandCode());
        tradeCommInfoOut.setAcctId(tradeCommInfo.getAccount().getAcctId());
        tradeCommInfoOut.setPayName(tradeCommInfo.getAccount().getPayName());
        tradeCommInfoOut.setPayModeCode(tradeCommInfo.getAccount().getPayModeCode());
        tradeCommInfoOut.setCreditValue(String.valueOf(mainUser.getCreditValue()));
        tradeCommInfoOut.setProvinceCode(mainUser.getProvinceCode());

        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_AM_HY_ATTR_CODE_TYPE);
        //手机合约余额播
        String hysjType;
        if (commPara != null) {
            hysjType = commPara.getParaCode1();
        } else {
            throw new SkyArkException("没有ASM_AM_HY_ATTR_CODE_TYPE的配置信息！");
        }
        boolean isHYSJFlag = false;
        tradeCommInfoOut.setProductType(String.valueOf(isHYSJFlag ? 1 : 0));
        //合约月生效金额
        long sumTransFee = 0;
        tradeCommInfoOut.setTransMoney(String.valueOf(sumTransFee));
        List<User> userList = tradeCommInfo.getPayUsers();
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getWriteoffMode())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            userList = tradeCommInfo.getAllPayUsers();
        }
        if (userList.size() > 1) {
            tradeCommInfoOut.setCompTag("1");
        } else {
            tradeCommInfoOut.setCompTag("0");
        }

        if (snaplog.getAllNewBalance() < 0) {
            tradeCommInfoOut.setBoweTag("1");
        } else {
            tradeCommInfoOut.setBoweTag("0");
        }
        //设置是否预付费用户
        long prepayUser = 0;
        if (!mainUser.getPrepayTag().equals("0"))
            prepayUser = 1;
        //2,3G转4G用户特殊临时信用度 暂时没有23直接转2I用户设置为0，
        long specialCreditValue = 0;
        //TotalSpayFee应交费用不包含实时话费、SpayFee包含实时话费
        if (specialCreditValue < 0) {
            tradeCommInfoOut.setSpayFee(String.valueOf(snaplog.getSpayFee() - specialCreditValue));
            tradeCommInfoOut.setTotalSpayFee(String.valueOf(snaplog.getAllNewBOweFee() - specialCreditValue));
        } else {
            tradeCommInfoOut.setSpayFee(String.valueOf(snaplog.getSpayFee()));
            tradeCommInfoOut.setTotalSpayFee(String.valueOf(snaplog.getAllNewBOweFee()));
        }
        //SpayFee应交费用是否包含实时话费  0：包含，1：不包含
        CommPara commParaSpayMode = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SPAY_MODE);
        if (commParaSpayMode != null) {
            if ("1".equals(commParaSpayMode.getParaCode1())) {
                if (specialCreditValue < 0) {
                    tradeCommInfoOut.setSpayFee(String.valueOf(snaplog.getAllNewBOweFee() - specialCreditValue));
                } else {
                    tradeCommInfoOut.setSpayFee(String.valueOf(snaplog.getAllNewBOweFee()));
                }
            }
        }
        tradeCommInfoOut.setAllBalance(String.valueOf(snaplog.getAllBalance()));
        tradeCommInfoOut.setAllNewBalance(String.valueOf(snaplog.getAllNewBalance()));
        tradeCommInfoOut.setNewBalance(String.valueOf(snaplog.getAllNewBalance() + specialCreditValue));
        tradeCommInfoOut.setAllBOweFee(String.valueOf(snaplog.getAllBOweFee()));
        tradeCommInfoOut.setAimpFee(String.valueOf(snaplog.getaImpFee()));
        tradeCommInfoOut.setAllNewBOweFee(String.valueOf(snaplog.getAllNewBOweFee()));
        tradeCommInfoOut.setPreRealFee(String.valueOf(snaplog.getPreRealFee()));
        tradeCommInfoOut.setCurRealFee(String.valueOf(snaplog.getCurRealFee()));
        tradeCommInfoOut.setAllROweFee(String.valueOf(snaplog.getCurRealFee() + snaplog.getPreRealFee()));
        tradeCommInfoOut.setRsrvDate(TimeUtil.getSysdate(TimeUtil.DATETIME_FORMAT));
        //统一余额播报参数
        CommPara commPara2 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SHOW_TYPE);
        if (commPara2 == null) {
            throw new SkyArkException("没有配置统一余额播报方案参数:ASM_SHOW_TYPE");
        }
        String contactType = "";
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
            contactType = commPara2.getParaCode1();
        }
        tradeCommInfoOut.setConTactType(contactType);
        //统一余额播报参数
        CommPara commPara3 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SHOW_SPECIALFEE);
        if (commPara3 != null) {
            tradeCommInfoOut.setSpecialfeeConTactType(commPara3.getParaCode1());
        } else
            tradeCommInfoOut.setSpecialfeeConTactType("0");
        //统一余额播报
        UniBalanceInfo balanceInfo = tradeCommInfo.getUniBalanceInfo();
        //账户缴费前可用余额
        tradeCommInfoOut.setAllMoney(String.valueOf(balanceInfo.getAllMoney()));
        //账户缴费后可用余额
        tradeCommInfoOut.setAllNewMoney(String.valueOf(balanceInfo.getAllNewMoney()));
        //可用预存款 = 专项可用预存款 + 普通可用预存款 RSRV_STR2 = RSRV_STR3 + RSRV_STR4
        balanceInfo.setAvailPreFee(balanceInfo.getAvailSpePreFee() + balanceInfo.getAvailOrdiPreFee());
        //可用赠款 = 专项可用赠款 + 普通可用赠款  RSRV_STR5 = RSRV_STR6 + RSRV_STR7
        balanceInfo.setAvailGrants(balanceInfo.getAvailSpeGrants() + balanceInfo.getAvailOrdGrants());
        //冻结预存款 = 专项冻结预存款 + 普通冻结预存款  RSRV_STR8 = RSRV_STR9 + RSRV_STR10
        balanceInfo.setFrozenPreFee(balanceInfo.getFrozenSpePreFee() + balanceInfo.getFrozenOrdPreFee());
        //冻结赠款 =  专项冻结赠款 +  普通冻结赠款    RSRV_STR11 = RSRV_STR12 + RSRV_STR13
        balanceInfo.setFrozenGrants(balanceInfo.getFrozenSpeGrants() + balanceInfo.getFrozenOrdGrants());
        //专项赠款 = 专项可用赠款 + 专项冻结赠款     RSRV_STR15 = RSRV_STR6 + RSRV_STR12
        balanceInfo.setSpecialGrants(balanceInfo.getAvailSpeGrants() + balanceInfo.getFrozenSpeGrants());
        //普通赠款 = 普通可用赠款 + 普通冻结赠款    RSRV_STR16 = RSRV_STR7 + RSRV_STR13
        balanceInfo.setOrdinaryGrants(balanceInfo.getAvailOrdGrants() + balanceInfo.getFrozenOrdGrants());
        /**账户余额 = 账户可用余额 + 账户冻结余额 =账户可用余额 + 专项冻结预存款 + 普通冻结预存款 + 专项冻结赠款 + 普通冻结赠款
         * 账户可用余额 = 可用预存款 + 可用赠款
         * 账户冻结余额 = 冻结预存款 + 冻结赠款 = 专项冻结预存款 + 普通冻结预存款 + 专项冻结赠款 + 普通冻结赠款
         * RSRV_STR1 = ALL_NEW_MONEY + RSRV_STR8 + RSRV_STR11 = ALL_NEW_MONEY + RSRV_STR9 + RSRV_STR10 + RSRV_STR12 + RSRV_STR13
         */
        balanceInfo.setAcctBalance(balanceInfo.getAllNewMoney() + balanceInfo.getFrozenPreFee() + balanceInfo.getFrozenGrants());
        //可用额度 RSRV_STR14 = 账户可用余额 + 信用度 - 实时话费 - 往月欠费
        balanceInfo.setAvialCredit(balanceInfo.getAllNewMoney() + mainUser.getCreditValue() - snaplog.getCurRealFee() - snaplog.getPreRealFee() - snaplog.getAllNewBOweFee());
        //账户可用专项款 = 专项可用预存款 + 专项可用赠款 RSRV_STR17 = RSRV_STR3 + RSRV_STR6
        balanceInfo.setAvailSpecialFee(balanceInfo.getAvailSpePreFee() + balanceInfo.getAvailSpeGrants());

        //获取当前余额信息
        List<AccountDeposit> tempdeposits = tradeCommInfo.getAccountDeposits();
        List<AccountDeposit> deposits = new ArrayList<>();
        //ECS要求把可用限额账本拆成两个账本显示
        for (AccountDeposit tempdeposit : tempdeposits) {
            if (tempdeposit.getLimitMode() == '1' && (tempdeposit.getDepositTypeCode() == '0' || tempdeposit.getDepositTypeCode() == '2')) {
                AccountDeposit tempdeposit2 = tempdeposit.clone();
                tempdeposit2.setMoney(tempdeposit.getMoney() - tempdeposit.getLeftCanUse()
                        - tempdeposit.getImpFee() - tempdeposit.getUseRecvFee());
//                tempdeposit2.setOddMoney(tempdeposit.getOddMoney() + tempdeposit.getEvenMoney() - tempdeposit.getLeftCanUse() - tempdeposit.getImpFee() - tempdeposit.getUseRecvFee());
//                tempdeposit2.setEvenMoney(0);
                tempdeposit2.setImpFee(0);
                tempdeposit2.setUseRecvFee(0);
                tempdeposit2.setInvoiceFee(0);
                tempdeposit2.setPrintFee(0);
                if (tempdeposit.getDepositTypeCode() == '0') {
                    tempdeposit2.setDepositTypeCode('1');
                } else {
                    tempdeposit2.setDepositTypeCode('3');
                }
                tempdeposit2.setLeftCanUse(0);
                tempdeposit2.setImpFee(0);

                tempdeposit.setMoney(tempdeposit.getLeftCanUse() + tempdeposit.getImpFee() + tempdeposit.getUseRecvFee());
//                tempdeposit.setOddMoney(tempdeposit.getLeftCanUse() + tempdeposit.getImpFee() + tempdeposit.getUseRecvFee());
//                tempdeposit.setEvenMoney(0);
                tempdeposit.setRecvFee(0);
                tempdeposit.setInitMoney(tempdeposit.getLimitMoney());

                deposits.add(tempdeposit);
                deposits.add(tempdeposit2);

            } else {
                deposits.add(tempdeposit);
            }
        }
        //查询用户的活动实例
        Account account = tradeCommInfo.getAccount();
        //需要放在账户域Dao层中
        List<DiscntDeposit> discntDeposits = payLogFeeService.getUserDiscntDepositByUserId(account.getAcctId(), mainUser.getUserId(), ActPayPubDef.ACTS_DRDS_DBCONN);
        //修改预付费用户校验逻辑，暂时根据主用户进行校验
        boolean hasPrePayUser = "1".equals(mainUser.getPrepayTag()) ? true : false;
        if (hasPrePayUser) {
            prepayUser = 1;
        }
        long xBackFee = 0;
        long xTransFee = 0;
        int curCycleId = writeOffRuleInfo.getCurCycle().getCycleId(); //当前帐期
        long leftBackFee = 0;//账本扣除实时话费倍数不够的金额
        //预付费用户清退是否扣除实时话费:para_code1=1时不需要扣除实时话费。其它情况下默认扣除实时话费。后付费用户(prepay_tag = 0)永不扣除实时话费
        CommPara backFeeType = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_IFBACKFEE_NOREALFEE);
        String backTag = "0";
        if (backFeeType != null) {
            if (StringUtil.isEmptyCheckNullStr(backFeeType.getParaCode1()) && "1".equals(backFeeType.getParaCode1())) {
                backTag = "1";
            }
        }
        List<DetailDepositInfo> detailDepositInfos = new ArrayList<>();
        for (AccountDeposit deposit : deposits) {
            DetailDepositInfo tempdepositInfo = new DetailDepositInfo();
            tempdepositInfo.setAcctId(deposit.getAcctId());
            tempdepositInfo.setAcctBalanceId(deposit.getAcctBalanceId());
            tempdepositInfo.setDepositCode(String.valueOf(deposit.getDepositCode()));
            tempdepositInfo.setDepositTypeCode(deposit.getDepositTypeCode());
            tempdepositInfo.setDepositName(writeOffRuleInfo.getDepositName(deposit.getDepositCode()));
            tempdepositInfo.setDepositMoney(deposit.getMoney() + deposit.getRecvFee());
            tempdepositInfo.setUsedMoney(deposit.getImpFee() + deposit.getUseRecvFee());
            tempdepositInfo.setCanUsedMoney(deposit.getLeftCanUse());
            if (tradeCommInfo.getWriteOffMode() == 2) {
                if (deposit.getPrivateTag() == '0' || deposit.getUserId().equals(mainUser.getUserId())) {
                    if (deposit.getDepositTypeCode() == '0') {
                        if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode())) {
                            balanceInfo.setCurAvailSpePreFee(balanceInfo.getCurAvailSpePreFee() + deposit.getLeftCanUse());
                        } else {
                            balanceInfo.setCurAvailOrdiPreFee(balanceInfo.getCurAvailOrdiPreFee() + deposit.getLeftCanUse());
                        }
                    } else if (deposit.getDepositTypeCode() == '2') {
                        if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode())) {
                            balanceInfo.setCurAvailSpeGrants(balanceInfo.getCurAvailSpeGrants() + deposit.getLeftCanUse());
                        } else {
                            balanceInfo.setCurAvailOrdGrants(balanceInfo.getCurAvailOrdGrants() + deposit.getLeftCanUse());
                        }
                    }
                }
            } else {
                if (deposit.getDepositTypeCode() == '0') {
                    if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode())) {
                        balanceInfo.setCurAvailSpePreFee(balanceInfo.getCurAvailSpePreFee() + deposit.getLeftCanUse());
                    } else {
                        balanceInfo.setCurAvailOrdiPreFee(balanceInfo.getCurAvailOrdiPreFee() + deposit.getLeftCanUse());
                    }
                } else if (deposit.getDepositTypeCode() == '2') {
                    if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode())) {
                        balanceInfo.setCurAvailSpeGrants(balanceInfo.getCurAvailSpeGrants() + deposit.getLeftCanUse());
                    } else {
                        balanceInfo.setCurAvailOrdGrants(balanceInfo.getCurAvailOrdGrants() + deposit.getLeftCanUse());
                    }
                }
            }
            if (!StringUtil.isEmptyCheckNullStr(deposit.getUserId()) && !"-1".equals(deposit.getUserId())) {
                for (User user : userList) {
                    if (deposit.getUserId().equals(user.getUserId())) {
                        tempdepositInfo.setSerialNumber(user.getSerialNumber());
                        tempdepositInfo.setAreaCode(user.getEparchyCode());
                        tempdepositInfo.setServiceClassCode(user.getNetTypeCode());
                        tempdepositInfo.setBindUserId(deposit.getUserId());
                    }
                }
            } else {
                tempdepositInfo.setBindUserId("-1");
            }
            tempdepositInfo.setPrivateTag(deposit.getPrivateTag());
            if (writeOffRuleInfo.isLimitDeposit(deposit.getDepositCode())) {
                tempdepositInfo.setSpecialFlag("1");
            } else {
                tempdepositInfo.setSpecialFlag("0");
            }

            if (deposit.getValidTag() == '0' && writeOffRuleInfo.isBackDeposit(deposit.getDepositCode()) && curCycleId <= deposit.getEndCycleId()) {
                long tmpBackFee = deposit.getMoney() - deposit.getImpFee() - deposit.getUseRecvFee() + deposit.getImpRealFee();
                //后付费不扣除实时话费 ,配置了参数的预付费不扣除实时话费
                if (!(prepayUser == 0 || "1".equals(backTag))) {
                    tmpBackFee = tmpBackFee - (deposit.getImpRealFee()) + leftBackFee;
                }
                leftBackFee = (tmpBackFee > 0 ? 0 : tmpBackFee);
                tmpBackFee = (tmpBackFee > 0 ? tmpBackFee : 0);
                xBackFee += tmpBackFee;
                tempdepositInfo.setBankFee(tmpBackFee);
            }
            if (deposit.getValidTag() == '0' && writeOffRuleInfo.isTransDeposit(deposit.getDepositCode()) && curCycleId <= deposit.getEndCycleId()) {
                long tmpTransFee = deposit.getMoney() - deposit.getImpFee() - deposit.getUseRecvFee();
                tmpTransFee = (tmpTransFee > 0 ? tmpTransFee : 0);
                xTransFee += tmpTransFee;
                tempdepositInfo.setTransFee(tmpTransFee);
            }
            tempdepositInfo.setLimitMode(deposit.getLimitMode());
            tempdepositInfo.setLimitMoney(deposit.getLimitMoney());
            tempdepositInfo.setStartCycleId(deposit.getStartCycleId());
            tempdepositInfo.setEndCycleId(deposit.getEndCycleId());
            tempdepositInfo.setStartDate(deposit.getStartDate());
            tempdepositInfo.setEndDate(deposit.getEndDate());
            tempdepositInfo.setValidTag(deposit.getValidTag());
            //月转兑金额和冻结金额
            if (deposit.getDepositTypeCode() == '0' || deposit.getDepositTypeCode() == '2') {
                tempdepositInfo.setAmount(0);
                tempdepositInfo.setFreezeFee(0);
            } else {
                if (deposit.getLimitMode() == '1') {
                    tempdepositInfo.setAmount(deposit.getLimitMoney());
                    //账本没有奇偶月了，只有一个当前余额
                    tempdepositInfo.setFreezeFee(deposit.getMoney());
                } else {
                    tempdepositInfo.setAmount(0);
                    tempdepositInfo.setFreezeFee(0);
                    for (DiscntDeposit discntDeposit : discntDeposits) {
                        if (deposit.getAcctBalanceId().equals(discntDeposit.getAcctBalanceId())) {
                            tempdepositInfo.setFreezeFee(discntDeposit.getLeftMoney());
                            long amount = 0;
                            if (discntDeposit.getLimitMode() == '0') {
                                //无限额，一次性转
                                amount = discntDeposit.getMoney();
                            } else if (discntDeposit.getLimitMode() == '1' || discntDeposit.getLimitMode() == 'c' || discntDeposit.getLimitMode() == 'd') {
                                amount = discntDeposit.getLimitMoney();
                                if (discntDeposit.getLimitMoney() <= 0) {
                                    amount = discntDeposit.getMoney() / discntDeposit.getMonths();
                                }
                            } else if (discntDeposit.getLimitMode() == '4') {
                                String splitMethod = discntDeposit.getSplitMethod();
                                String[] splitMethods = splitMethod.split(";");
                                int len = splitMethods[0].length();
                                String indexString = splitMethods[0];
                                if (len > 0) {
                                    if (indexString.substring(indexString.length() - 1, indexString.length()).charAt(0) == '%') //按比例拆分
                                        amount = discntDeposit.getMoney() / 100 * Long.parseLong(indexString);
                                    else //按固定金额
                                        amount = Long.parseLong(indexString);
                                } else
                                    throw new SkyArkException("总优惠分解方式错误!" + splitMethod);

                            } else if (discntDeposit.getLimitMode() == 'm' || discntDeposit.getLimitMode() == '6' || discntDeposit.getLimitMode() == '8') {
                                amount = discntDeposit.getLimitMoney();
                            }
                            amount = discntDeposit.getLeftMoney() > amount ? amount : discntDeposit.getLeftMoney();
                            tempdepositInfo.setAmount(amount);
                        }
                    }
                }
            }
            detailDepositInfos.add(tempdepositInfo);
        }
        tradeCommInfoOut.setDetailDepositInfos(detailDepositInfos);
        tradeCommInfoOut.setBankFee(String.valueOf(xBackFee));
        tradeCommInfoOut.setTransFee(String.valueOf(xTransFee));
        //账户冻结余额 = 冻结预存款 +  冻结赠款 = 专项冻结预存款+普通冻结预存款 +  专项冻结赠款   +  普通冻结赠款
        tradeCommInfoOut.setResFee(String.valueOf(balanceInfo.getFrozenPreFee() + balanceInfo.getFrozenGrants()));
        //fee18 = leftFee3 + leftFee4 + leftFee6 + leftFee7;
        //账户当前可用余额 = 账户当前可用专项预存款 + 账户当前可用普通预存款 + 账户当前可用专项赠款 + 账户当前可用普通赠款
        balanceInfo.setAcctCurAvailBalance(balanceInfo.getCurAvailSpePreFee() + balanceInfo.getCurAvailOrdiPreFee() + balanceInfo.getCurAvailSpeGrants() + balanceInfo.getCurAvailOrdGrants());
        //账户当前可用专项款 = 账户当前可用专项预存款 + 账户当前可用专项赠款
        balanceInfo.setCurAvailSpecialFee(balanceInfo.getCurAvailSpePreFee() + balanceInfo.getCurAvailSpeGrants());
        //账户当前可用预存款 =  账户当前可用专项预存款 + 账户当前可用普通预存款
        balanceInfo.setCurAvailPreFee(balanceInfo.getCurAvailSpePreFee() + balanceInfo.getCurAvailOrdiPreFee());
        //账户当前可用赠款 = 账户当前可用专项赠款 + 账户当前可用普通赠款
        balanceInfo.setCurAvailGrants(balanceInfo.getCurAvailSpeGrants() + balanceInfo.getCurAvailOrdGrants());
        //ASM_SHOW_CURRENT 账户当前可用余额赋值实时结余
        CommPara commPara4 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SHOW_CURRENT);
        if (commPara4 != null) {
            if ("1".equals(commPara4.getParaCode1())) {
                balanceInfo.setAcctCurAvailBalance(snaplog.getCurrentAvlFee());
            }
        }
        if (snaplog.getAllNewBOweFee() > 0) {
            List<Bill> billList = tradeCommInfo.getBills();
            AcctPaymentCycle paymentCycle = tradeCommInfo.getAcctPaymentCycle();
            int maxAcctCycleId = writeOffRuleInfo.getMaxAcctCycle().getCycleId();
            int firstCalCycleId = maxAcctCycleId;//计算本月应交的起始月
            if (paymentCycle != null) {
                if (paymentCycle.getBundleMonths() > 1 && paymentCycle.getOffMonths() > 1) {
                    int buldeEndCycleId = 0;
                    buldeEndCycleId = curCycleId / 100 * 100;
                    while (TimeUtil.genCycle(buldeEndCycleId, paymentCycle.getBundleMonths()) < curCycleId)
                        buldeEndCycleId = TimeUtil.genCycle(buldeEndCycleId, paymentCycle.getBundleMonths());//当前账期所在的捆绑缴费期最后一个账期
                    while (TimeUtil.genCycle(buldeEndCycleId, -paymentCycle.getOffMonths()) > curCycleId)
                        buldeEndCycleId = TimeUtil.genCycle(buldeEndCycleId, -paymentCycle.getBundleMonths());//最近一个开始算滞纳金的捆绑缴费期的最后一个账期
                    firstCalCycleId = TimeUtil.genCycle(buldeEndCycleId, 1);//下一账期还未算滞纳金，往后开始累成本月应交
                }
            }
            long oweFee = 0;//往月欠费
            long oldFee = 0;//上上个月的欠费
            for (Bill bill : billList) {
                if (bill.getCycleId() <= maxAcctCycleId && bill.getCycleId() >= firstCalCycleId) {
                    oweFee += bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee()
                            - bill.getDerateFee() - bill.getCurrWriteOffLate() - bill.getCurrWriteOffBalance();
                }
                if (bill.getCycleId() < maxAcctCycleId) {
                    oldFee += bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee()
                            - bill.getDerateFee() - bill.getCurrWriteOffLate() - bill.getCurrWriteOffBalance();
                }
            }
            tradeCommInfoOut.setIntfFee01(String.valueOf(oweFee));
            tradeCommInfoOut.setIntfFee02(String.valueOf(snaplog.getAllBOweFee() - oweFee));
            tradeCommInfoOut.setOldFee(String.valueOf(oldFee));
        }

        tradeCommInfoOut.setUnifiedBalanceInfo(balanceInfo);
        long consignFee = 0; //托收在途金额
        long prePrintFee = 0; //预打金额
        int recvCycleId = 0; //缴费月份接口需要
        // 0:只取得欠费信息,1 加上帐本信息，2加上帐单信息 3加上销帐日志和存取款日志
        int targetData = Integer.parseInt(tradeCommInfoIn.getTargetData());
        if (targetData > 0) {
            if (targetData > 1) {
                //追加账单信息
                List<Bill> billList = tradeCommInfo.getBills();
                Map<Integer, Bill> mMainBill = new HashMap<>();
                for (Bill bill : billList) {
                    if (mMainBill.containsKey(bill.getCycleId())) {
                        Bill temp = mMainBill.get(bill.getCycleId());
                        temp.setFee(temp.getFee() + bill.getFee());
                        temp.setWriteoffFee1(temp.getWriteoffFee1() + bill.getWriteoffFee1());
                        temp.setBalance(temp.getBalance() + bill.getBalance());
                        temp.setLateFee(temp.getLateFee() + bill.getLateFee());
                        temp.setLateBalance(temp.getLateBalance() + bill.getLateBalance());
                        temp.setNewLateFee(temp.getNewLateFee() + bill.getNewLateFee());
                        temp.setDerateFee(temp.getDerateFee() + bill.getDerateFee());
                        temp.setCurrWriteOffBalance(temp.getCurrWriteOffBalance() + bill.getCurrWriteOffBalance());
                        temp.setCurrWriteOffLate(temp.getCurrWriteOffLate() + bill.getCurrWriteOffLate());
                        temp.setaDiscnt(temp.getaDiscnt() + bill.getaDiscnt());
                        temp.setbDiscnt(temp.getbDiscnt() + bill.getbDiscnt());
                        temp.setAdjustBefore(temp.getAdjustBefore() + bill.getAdjustBefore());
                        temp.setAdjustAfter(temp.getAdjustAfter() + bill.getAdjustAfter());
                        temp.setCanpayTag(bill.getCanpayTag());
                        temp.setPayTag(bill.getPayTag());
                        if (temp.getOldPayTag() != '7') {
                            temp.setOldPayTag(bill.getOldPayTag());
                        }
                    } else {
                        Bill billTemp = bill.clone();
                        mMainBill.put(bill.getCycleId(), billTemp);
                    }
                    if (bill.getOldPayTag() == '7') {
                        consignFee += bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee()
                                - bill.getDerateFee() - bill.getCurrWriteOffBalance() - bill.getCurrWriteOffLate();
                    }
                    if (bill.getOldPayTag() == '8') {
                        prePrintFee += bill.getBalance() + bill.getLateBalance() + bill.getNewLateFee()
                                - bill.getDerateFee() - bill.getCurrWriteOffBalance() - bill.getCurrWriteOffLate();
                    }
                    if (recvCycleId < bill.getCycleId())
                        recvCycleId = bill.getCycleId();
                }
                logger.debug("账单条数mMainBill = " + mMainBill.size());
                if (consignFee != 0) {
                    tradeCommInfoOut.setCanConsignTag("1");
                } else {
                    tradeCommInfoOut.setCanConsignTag("0");
                }
                if (prePrintFee != 0)
                    tradeCommInfoOut.setCanConsignTag("2");
                tradeCommInfoOut.setConsignFee(String.valueOf(consignFee));
                //账单明细
                List<DetailBillInfo> detailBillInfoList = new ArrayList<>();
                long lateFee = 0;
                for (Map.Entry<Integer, Bill> it : mMainBill.entrySet()) {
                    DetailBillInfo detailBillInfo = new DetailBillInfo();
                    detailBillInfo.setAcctId(it.getValue().getAcctId());
                    detailBillInfo.setCycleId(it.getValue().getCycleId());
                    detailBillInfo.setCanpayTag(it.getValue().getCanpayTag());
                    if (it.getValue().getOldPayTag() == '7') {
                        detailBillInfo.setPayTag(it.getValue().getOldPayTag());
                    } else {
                        detailBillInfo.setPayTag(it.getValue().getPayTag());
                    }
                    detailBillInfo.setMonthfee(it.getValue().getFee() + it.getValue().getAdjustAfter());
                    detailBillInfo.setPayFee(it.getValue().getFee() + it.getValue().getAdjustAfter() - it.getValue().getBalance());
                    detailBillInfo.setPayLateFee(it.getValue().getLateFee() - it.getValue().getLateBalance());
                    long tmpCurrLateFee = it.getValue().getLateBalance() + it.getValue().getNewLateFee() - it.getValue().getDerateFee();
                    detailBillInfo.setMonthLateFee(tmpCurrLateFee);
                    lateFee += tmpCurrLateFee;
                    detailBillInfo.setDerateFee(it.getValue().getDerateFee());
                    detailBillInfo.setSumDebFee(it.getValue().getBalance() + tmpCurrLateFee);
                    detailBillInfo.setSpayFee(it.getValue().getFee() + it.getValue().getAdjustAfter() + it.getValue().getLateFee()
                            + it.getValue().getNewLateFee() - it.getValue().getDerateFee());
                    detailBillInfoList.add(detailBillInfo);
                }
                tradeCommInfoOut.setLateFee(String.valueOf(lateFee));
                tradeCommInfoOut.setDetailBillInfos(detailBillInfoList);

                //<cycleId, <integrateItemCode,PBill>>
                Map<Integer, Map<Integer, Bill>> subBill = new HashMap<>();
                for (Bill bill : billList) {
                    if (subBill.containsKey(bill.getCycleId())) {
                        Map<Integer, Bill> tmpMapBill = subBill.get(bill.getCycleId());
                        if (tmpMapBill.containsKey(bill.getIntegrateItemCode())) {
                            Bill tempBill = tmpMapBill.get(bill.getIntegrateItemCode());
                            tempBill.setFee(tempBill.getFee() + bill.getFee());
                            tempBill.setBalance(tempBill.getBalance() + bill.getBalance());
                            tempBill.setLateBalance(tempBill.getLateBalance() + bill.getLateBalance());
                            tempBill.setNewLateFee(tempBill.getNewLateFee() + bill.getNewLateFee());
                            tempBill.setDerateFee(tempBill.getDerateFee() + bill.getDerateFee());
                            tempBill.setCurrWriteOffBalance(tempBill.getCurrWriteOffBalance() + bill.getCurrWriteOffBalance());
                            tempBill.setCurrWriteOffLate(tempBill.getCurrWriteOffLate() + bill.getCurrWriteOffLate());
                            tempBill.setaDiscnt(tempBill.getaDiscnt() + bill.getaDiscnt());
                            tempBill.setbDiscnt(tempBill.getbDiscnt() + bill.getbDiscnt());
                            tempBill.setAdjustAfter(tempBill.getAdjustAfter() + bill.getAdjustAfter());
                            tempBill.setAdjustBefore(tempBill.getAdjustBefore() + bill.getAdjustBefore());
                        } else {
                            Bill subbillTemp = new Bill();
                            subbillTemp = bill;
                            tmpMapBill.put(bill.getIntegrateItemCode(), subbillTemp);
                        }

                    } else {
                        Map<Integer, Bill> tmpMap = new HashMap<>();
                        tmpMap.put(bill.getIntegrateItemCode(), bill);
                        subBill.put(bill.getCycleId(), tmpMap);
                    }
                }
                logger.debug("子账单明细条数subBill = " + subBill.size());
                //子账单明细
                List<SubDetailBillInfo> subDetailBillInfoList = new ArrayList<>();
                for (Map.Entry<Integer, Map<Integer, Bill>> it : subBill.entrySet()) {
                    for (Map.Entry<Integer, Bill> it2 : it.getValue().entrySet()) {
                        SubDetailBillInfo subBillInfoTemp = new SubDetailBillInfo();
                        subBillInfoTemp.setAcctId(it2.getValue().getAcctId());
                        subBillInfoTemp.setCycleIdb(it2.getValue().getCycleId());
                        subBillInfoTemp.setIntegrateItemCode(it2.getValue().getIntegrateItemCode());
                        subBillInfoTemp.setIntegrateItem(WriteOffRuleStaticInfo.getItemName(it2.getValue().getIntegrateItemCode()));
                        subBillInfoTemp.setFee(it2.getValue().getFee());
                        long lateBalance = it2.getValue().getLateBalance() + it2.getValue().getNewLateFee() - it2.getValue().getDerateFee();
                        subBillInfoTemp.setBalance(it2.getValue().getBalance() + lateBalance);
                        subBillInfoTemp.setLateBalance(lateBalance);
                        subBillInfoTemp.setCanpayTag(it2.getValue().getCanpayTag());
                        subDetailBillInfoList.add(subBillInfoTemp);
                    }
                }
                tradeCommInfoOut.setSubDetailBillInfos(subDetailBillInfoList);
            }
        }
        logger.debug("欠费查询输出的字段值：" + tradeCommInfoOut.toString());
        return tradeCommInfoOut;
    }
}

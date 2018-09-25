package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acting.fee.writeoff.service.*;
import com.unicom.skyark.component.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 费用计算公共方法
 *
 * @author wangkh
 */
@Service
public class FeeCommServiceImpl implements FeeCommService {
    private static final Logger logger = LoggerFactory.getLogger(FeeCommServiceImpl.class);
    @Autowired
    private WriteOffRuleService writeOffRuleService;
    @Autowired
    private DatumService datumService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private CycleService cycleService;
    @Autowired
    private AcctDepositService acctDepositService;
    @Autowired
    private FeePayLogService feePayLogService;
    @Autowired
    private FeeBillService feeBillService;
    @Autowired
    private FeeDerateLateFeeLogService feeDerateLateFeeLogService;
    @Autowired
    private DatumParamRelService datumParamRelService;
    @Autowired
    private RealBillService realBillService;

    @Override
    public void getUserDatumInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //调用微服务查询三户资料
        //UserDatumInfo userDatumInfo = datumService.getUserDatumByMS(tradeCommInfoIn);
        UserDatumInfo userDatumInfo = datumParamRelService.getUserDatum(tradeCommInfoIn);
        //充值用户
        tradeCommInfo.setMainUser(userDatumInfo.getMainUser());
        //付费账户
        tradeCommInfo.setAccount(userDatumInfo.getAccount());
        //按用户交易
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getWriteoffMode())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            if (userDatumInfo.getMainUser() == null) {
                throw new SkyArkException("按用户交易，未获取交易用户信息!");
            }
            tradeCommInfo.setPayUsers(Collections.singletonList(userDatumInfo.getMainUser()));
            tradeCommInfo.setAllPayUsers(userDatumInfo.getDefaultPayUsers());
            //按用户缴费设置缴费用户标识
            tradeCommInfo.setChooseUserId(Collections.singleton(userDatumInfo.getMainUser().getUserId()));
            tradeCommInfo.setWriteOffMode(Integer.parseInt(tradeCommInfoIn.getWriteoffMode()));
        } else {
            //账户付费用户
            tradeCommInfo.setPayUsers(userDatumInfo.getDefaultPayUsers());
        }
        //请求入参中的号码归属地市和省份编码替换为三户资料查询结果中的地市和省份编码
        tradeCommInfoIn.setProvinceCode(tradeCommInfo.getAccount().getProvinceCode());
        tradeCommInfoIn.setEparchyCode(tradeCommInfo.getAccount().getEparchyCode());
        //大合帐用户
        tradeCommInfoIn.setBigAcctRecvFee(userDatumInfo.isBigAcct());
    }

    @Override
    public void getEparchyCycleInfo(TradeCommInfo tradeCommInfo, String eparchyCode, String provinceCode) {
        //系统时间
        String sysdate = sysCommOperFeeService.getSysdate(TimeUtil.DATETIME_FORMAT);
        //当前当期
        Cycle curCycle = null;
        //当前最大开账账期
        Cycle maxCycle = null;
        CommPara commPara = commParaFeeService.getCommpara(ActingFeeCommparaDef.ASM_AUXACCTSTATUS_FROMCACHE,
                provinceCode, eparchyCode);

        if (commPara != null && "1".equals(commPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode3())
                && sysdate.substring(8, 10).compareTo(commPara.getParaCode2()) >= 0
                && sysdate.substring(8, 10).compareTo(commPara.getParaCode3()) <= 0) {
            curCycle = cycleService.getCacheCurCycle(eparchyCode);
            maxCycle = cycleService.getCacheMaxAcctCycle(eparchyCode);
        } else {
            curCycle = cycleService.getCurCycle(eparchyCode);
            maxCycle = cycleService.getMaxAcctCycle(eparchyCode);
        }

        if (curCycle == null) {
            throw new SkyArkException("没有取到当前帐期!");
        }

        if (maxCycle == null) {
            throw new SkyArkException("没有取到当前最大开帐帐期!");
        }

        //创建销账规则对象
        WriteOffRuleInfo writeOffRuleInfo = new WriteOffRuleInfo();
        writeOffRuleInfo.setCurCycle(curCycle);
        writeOffRuleInfo.setMaxAcctCycle(maxCycle);
        writeOffRuleInfo.setSysdate(sysdate);
        writeOffRuleInfo.setParity(curCycle.getCycleId() % 2);
        tradeCommInfo.setWriteOffRuleInfo(writeOffRuleInfo);
    }

    @Override
    public void getWriteOffRule(WriteOffRuleInfo writeOffRuleInfo, String provinceCode, String eparchyCode, String netTypeCode) {
        //获取销账规则
        writeOffRuleService.getWriteOffRule(writeOffRuleInfo, eparchyCode, provinceCode, netTypeCode);
    }

    @Override
    public void getAcctBalance(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有获取账户信息，请先查询三户资料");
        }
        Account feeAccount = tradeCommInfo.getAccount();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //获取账本
        tradeCommInfo.setFeeAccountDeposits(acctDepositService.getAcctDepositByAcctId(feeAccount.getAcctId()));
        //获取账本比例关系
        tradeCommInfo.setFeeAcctBalanceRels(acctDepositService.getAcctBalanceRelByAcctId(feeAccount.getAcctId()));
        //获取销账规则
        //writeOffRuleService.getWriteOffRule(writeOffRuleInfo, feeAccount.getEparchyCode(), feeAccount.getProvinceCode(), feeAccount.getNetTypeCode());

        //抵扣，补收和增量出账期间需要查询帐务业务后台处理表做处理
        boolean specialState = writeOffRuleInfo.isSpecialRecvState(writeOffRuleInfo.getCurCycle());
        if (specialState) {
            //获取帐务业务后台处理表记录
            List<FeePayLogDmn> feePayLogDmns = feePayLogService.getPayLogDmnByAcctId(feeAccount.getAcctId(), "0", DbTypes.ACT_RDS, feeAccount.getProvinceCode());
            if (!CollectionUtils.isEmpty(feePayLogDmns)) {
                for (FeePayLogDmn feePayLogDmn : feePayLogDmns) {
                    acctDepositService.genAcctDepositByPayLogDmn(tradeCommInfoIn, feePayLogDmn, tradeCommInfo);
                }
            }
        }

        tradeCommInfo.setSpecialCycleStatus(specialState);
    }

    @Override
    public void getOweBill(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有获取账户信息，请先查询三户资料");
        }

        Account feeAccount = tradeCommInfo.getAccount();
        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        //销账规则对象
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //已开帐帐期
        int maxAcctCycleId = writeOffRuleInfo.getMaxAcctCycle().getCycleId();
        //当前帐期
        int currCycleId = writeOffRuleInfo.getCurCycle().getCycleId();
        int tmpAcctCycleId = (currCycleId > ActingFeePubDef.MAX_CYCLE_ID ? ActingFeePubDef.MAX_CYCLE_ID : currCycleId);
        //待开帐帐期
        int preCurCycleId = TimeUtil.genCycle(maxAcctCycleId, 1);

        String acctId = feeAccount.getAcctId();
        String userId = "";
        if (tradeCommInfo.getMainUser() != null) {
            userId = tradeCommInfo.getMainUser().getUserId();
        }
        //获取实时账单
        List<FeeBill> realFeeBills = new ArrayList();
        if (!"1".equals(tradeCommInfoIn.getBatchDealTag())) {
            realFeeBills = realBillService.getRealBillFromMemDB(tradeCommInfoIn, feeAccount.getAcctId(), userId, preCurCycleId, currCycleId);
        } else {
            realFeeBills = tradeCommInfo.getFeeBills();
        }


        //获取往月账单
        List<FeeBill> feeBills = new ArrayList();
        if ("1".equals(tradeCommInfoIn.getQryBillType())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            feeBills = feeBillService.getBillOweByUserId(acctId, userId, ActingFeePubDef.MIN_CYCLE_ID, tmpAcctCycleId);
        } else {
            feeBills = feeBillService.getBillOweByAcctId(acctId, ActingFeePubDef.MIN_CYCLE_ID, tmpAcctCycleId);
        }


        /*
         *  内存数据库方式，开帐标志由销帐打，抵扣时候尚未开帐，判断抵扣入库期间或增量出账期间
         *  如果有开帐帐期的帐单已入库需要去除实时帐单中的对应月份的部分
         *  因为这个时候开帐标识use_tag='0'有两个月的实时话费，而实际上已经有帐单入库了
         *  多个抵扣入库进程没有办法控制一起完成
         */
        if (cycleService.isDrecvPeriod(writeOffRuleInfo.getCurCycle())
                && !CollectionUtils.isEmpty(realFeeBills)) {
            if (preCurCycleId < currCycleId) {
                realFeeBills = feeBillService.removeWriteOffRealBill(feeBills, realFeeBills,
                        acctId, preCurCycleId);
            }
        }

        //设置实时账单的BILL_ID
        if (!CollectionUtils.isEmpty(realFeeBills)
                && !CollectionUtils.isEmpty(tradeCommInfo.getPayUsers())) {
            //可能的账单实例条数
            int billIdCount = tradeCommInfo.getPayUsers().size();
            //如果实时账单不止一个账期，序列值加倍
            if (TimeUtil.diffMonths(currCycleId, maxAcctCycleId) > 1) {
                billIdCount *= 2;
            }

            feeBillService.updateRealBillId(realFeeBills, billIdCount,
                    tradeCommInfoIn.getEparchyCode(), tradeCommInfoIn.getProvinceCode());
        }

        //将实时账单添加到账单列表
        feeBills.addAll(realFeeBills);
        //坏账缴费时需要加载坏账账单
        if (!StringUtil.isEmpty(tradeCommInfoIn.getBadTypeCode())
                && datumService.isBadBillUser(acctId)) {
            String badTypeCode = tradeCommInfoIn.getBadTypeCode();
            //捞取坏账账单
            List<FeeBill> badFeeBills = feeBillService.getBadBillOweByAcctId(acctId, maxAcctCycleId,
                    tmpAcctCycleId);
            if (!CollectionUtils.isEmpty(badFeeBills)) {
                //坏账缴费处理方式
                CommPara commPara = writeOffRuleInfo.getCommpara(ActingFeeCommparaDef.ASM_BADBILL_PAYFEE);
                if (commPara == null) {
                    throw new SkyArkException("ASM_BADBILL_PAYFEE参数没有配置!");
                }

                //坏账缴费只销缴费号码的坏账，针对合账用户，可能存在同账户下其他用户的坏账不会销掉
                if ("2".equals(tradeCommInfoIn.getWriteoffMode()) && !"".equals(userId)) {
                    List<FeeBill> debFeeBill = new ArrayList<>();
                    for (FeeBill feeBill : badFeeBills) {
                        if (userId.equals(feeBill.getUserId())) {
                            debFeeBill.add(feeBill);
                        }
                    }
                    badFeeBills = debFeeBill;
                }

                if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1()) && "1".equals(commPara.getParaCode1())) {
                    //只有话费收取传"1"，其他欠费查询都不传,坏帐缴费传"2",其他欠费查询途径为"";
                    if ("1".equals(badTypeCode)) {
                        throw new SkyArkException("用户存在坏帐，请先到坏帐界面结清坏帐!acctId=" + acctId);
                    }
                    feeBills = badFeeBills;
                    //坏帐不触发信控
                    tradeCommInfo.setFireCreditCtrl(false);
                } else {
                    for (FeeBill feeBill : badFeeBills) {
                        feeBills.add(feeBill);
                    }
                }
                tradeCommInfo.setHasBadBill(true);
            }
        } else {
            tradeCommInfo.setHasBadBill(false);
        }
        tradeCommInfo.setFeeBills(feeBills);
    }

    @Override
    public void specialBusiCheck(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (ifBillConsigning(tradeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收在途不能前台缴费!");
        }

        if (ifConsignPayMode(tradeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收用户不能前台缴费!");
        }
        if (ifPrePrintInvoice(tradeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("代理商预打发票只能通过预打回款缴费!");
        }

        //电子券充值用户服务状态校验
        elecPresentLimit(tradeCommInfo.getMainUser(), tradeCommInfoIn.getPaymentId(),
                tradeCommInfo.getWriteOffRuleInfo());

    }

    @Override
    public boolean ifCalcLateFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有获取账户信息，请先查询三户资料");
        }
        Account feeAccount = tradeCommInfo.getAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有获取用户信息，请先查询三户资料");
        }

        //免滞纳金计算用户不计算滞纳金
        if (datumService.isNoCalcLateFeeUser("-1",
                feeAccount.getAcctId())) {
            return true;
        }

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //取得不算滞纳金的付费方式
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingFeeCommparaDef.ASM_CALCLATEFEE_PAYMODE_LIMIT);
        if (commPara != null && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
            String[] payFeeModes = commPara.getParaCode1().split("\\|");
            for (String payFeeMode : payFeeModes) {
                //如果限制了就不算滞纳金
                if (payFeeMode.equals(feeAccount.getPayModeCode())) {
                    return true;
                }
            }
        }
        //配置参数启用，只有离网用户计算滞纳金
        CommPara userCalcPara = writeOffRuleInfo.getCommpara(ActingFeeCommparaDef.ASM_CALCLATEFEE_DESTROY_USER);
        if (userCalcPara != null && "1".equals(userCalcPara.getParaCode1())
                && "0".equals(tradeCommInfo.getMainUser().getRemoveTag())) {
            return true;
        }
        return false;
    }

    @Override
    public void getFeeDerateLateFeeLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())) {
            return;
        }

        List<FeeBill> feeBills = tradeCommInfo.getFeeBills();
        int minCycleId = feeBills.get(0).getCycleId();
        int maxCycleId = feeBills.get(0).getCycleId();
        String acctId = feeBills.get(0).getAcctId();
        for (FeeBill feeBill : feeBills) {
            if (feeBill.getCycleId() > maxCycleId) {
                maxCycleId = feeBill.getCycleId();
            }

            if (feeBill.getCycleId() < minCycleId) {
                minCycleId = feeBill.getCycleId();
            }
        }
        List<FeeDerateLateFeeLog> feeDerateLateFeeLogs = feeDerateLateFeeLogService.getDerateLateFeeLog(acctId, minCycleId, maxCycleId);
        tradeCommInfo.setFeeDerateLateFeeLogs(feeDerateLateFeeLogs);
    }

    @Override
    public void getAcctPaymentCycle(TradeCommInfo tradeCommInfo, String acctId) {
        tradeCommInfo.setActPaymentCycle(datumService.getAcctPaymentCycle(acctId));
    }

    @Override
    public boolean ifBillConsigning(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(ActingFeeCommparaDef.ASM_CONSIGN_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_CONSIGN_CAN_RECV参数没有配置!");
        }

        //1000041银行代扣和托收一样的处理
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "0".compareTo(commPara.getParaCode1()) < 0
                && (tradeCommInfoIn.getPaymentId() != 100004
                && tradeCommInfoIn.getPaymentId() != 1000041
                && tradeCommInfoIn.getPaymentId() != 1000044)) {
            //托收在途判断
            List<FeeBill> feeBills = tradeCommInfo.getFeeBills();
            for (FeeBill feeBill : feeBills) {
                if ('7' == feeBill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean ifConsignPayMode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())
                || tradeCommInfo.getAccount() == null) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(ActingFeeCommparaDef.ASM_CONSIGN_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_CONSIGN_CAN_RECV参数没有配置!");
        }

        //1000041银行代扣和托收一样的处理
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "2".equals(commPara.getParaCode1())
                && (tradeCommInfoIn.getPaymentId() != 100004
                && tradeCommInfoIn.getPaymentId() != 1000041
                && tradeCommInfoIn.getPaymentId() != 1000044)) {
            CommPara rCommPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(ActingFeeCommparaDef.ASM_CONSIGN_PAY_MODE);
            if (rCommPara == null) {
                throw new SkyArkException("ASM_CONSIGN_PAY_MODE参数没有配置!");
            }
            String[] consignPayMode = rCommPara.getParaCode1().split("\\|");
            //托收在途判断
            Account feeAccount = tradeCommInfo.getAccount();
            for (String payFeeMode : consignPayMode) {
                if (payFeeMode.equals(feeAccount.getPayModeCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean ifPrePrintInvoice(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if ("15000".equals(tradeCommInfoIn.getChannelId())) {
            return false;
        }

        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())) {
            return false;
        }
        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(ActingFeeCommparaDef.ASM_PRE_PRINTINVOICE_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_PRE_PRINTINVOICE_CAN_RECV参数没有配置!");
        }

        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "1".equals(commPara.getParaCode1())
                && !tradeCommInfoIn.getChannelId().equals(commPara.getParaCode2())) {
            //发票预打在途,(只有后台进程才能缴费)
            List<FeeBill> feeBills = tradeCommInfo.getFeeBills();
            for (FeeBill feeBill : feeBills) {
                if ('8' == feeBill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void elecPresentLimit(User mainUser, int paymentId, WriteOffRuleInfo writeOffRuleInfo) {
        String userStateCode = mainUser.getServiceStateCode();
        if (!StringUtil.isEmptyCheckNullStr(mainUser.getServiceStateCode())
                && !"0".equals(userStateCode) && !"N".equals(userStateCode)) {
            CommPara commPara1 = writeOffRuleInfo.getCommpara(ActingFeeCommparaDef.ASM_PRESENT_LIMITPAYMENT);
            if (commPara1 == null) {
                throw new SkyArkException("ASM_PRESENT_LIMITPAYMENT参数没有配置!");
            }

            //没有配置限制的储值方式不再处理
            if (StringUtil.isEmptyCheckNullStr(commPara1.getParaCode1())) {
                return;
            }

            String regexStr = "|" + paymentId + "|";
            if (commPara1.getParaCode1().contains(regexStr)) {
                throw new SkyArkException("停机状态,限制此业务!userStateCode=" + userStateCode + ",paymentId=" + paymentId);
            }
        }
    }
}

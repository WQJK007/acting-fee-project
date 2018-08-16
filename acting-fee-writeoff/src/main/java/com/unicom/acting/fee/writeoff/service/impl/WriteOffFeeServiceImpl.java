package com.unicom.acting.fee.writeoff.service.impl;

import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acting.fee.writeoff.service.*;
import com.unicom.acts.pay.domain.Account;
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
 * 销账相关公共方法
 *
 * @author Administrators
 */
@Service
public class WriteOffFeeServiceImpl implements WriteOffFeeService {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffFeeServiceImpl.class);
    @Autowired
    private WriteOffRuleFeeService writeOffRuleFeeService;
    @Autowired
    private DatumFeeService datumFeeService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private CycleFeeService cycleFeeService;
    @Autowired
    private AcctDepositFeeService acctDepositFeeService;
    @Autowired
    private PayLogFeeService payLogFeeService;
    @Autowired
    private BillFeeService billFeeService;
    @Autowired
    private DerateLateFeeLogFeeService derateLateFeeLogFeeService;

    @Override
    public void genUserDatumInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //调用微服务查询三户资料
        UserDatumInfo userDatumInfo = datumFeeService.getUserDatumByMS(tradeCommInfoIn);
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
    public void genEparchyCycleInfo(TradeCommInfo tradeCommInfo, String eparchyCode, String provinceCode) {
        //系统时间
        String sysdate = sysCommOperFeeService.getSysdate(TimeUtil.DATETIME_FORMAT);
        String day = sysdate.substring(8, 10);
        //当前当期
        Cycle curCycle = null;
        //当前最大开账账期
        Cycle maxCycle = null;
        CommPara commPara = commParaFeeService.getCommpara(PubCommParaDef.ASM_AUXACCTSTATUS_FROMCACHE,
                provinceCode, eparchyCode, ActPayPubDef.ACT_RDS_DBCONN);

        if (commPara != null && "1".equals(commPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode3())
                && day.compareTo(commPara.getParaCode2()) >= 0
                && day.compareTo(commPara.getParaCode3()) <= 0) {
            curCycle = cycleFeeService.getCacheCurCycle(eparchyCode, ActPayPubDef.ACT_RDS_DBCONN);
            maxCycle = cycleFeeService.getCacheMaxAcctCycle(eparchyCode, ActPayPubDef.ACT_RDS_DBCONN);
        } else {
            curCycle = cycleFeeService.getCurCycle(eparchyCode, ActPayPubDef.ACT_RDS_DBCONN);
            maxCycle = cycleFeeService.getMaxAcctCycle(eparchyCode, ActPayPubDef.ACT_RDS_DBCONN);
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
    public void getAcctBalance(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有获取账户信息，请先查询三户资料");
        }
        Account account = tradeCommInfo.getAccount();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //获取账本
        tradeCommInfo.setAccountDeposits(acctDepositFeeService.getAcctDepositByAcctId(account.getAcctId(), ActPayPubDef.ACTS_DRDS_DBCONN));
        //获取账本比例关系
        tradeCommInfo.setAcctBalanceRels(acctDepositFeeService.getAcctBalanceRelByAcctId(account.getAcctId(), ActPayPubDef.ACTS_DRDS_DBCONN));
        //获取销账规则
        writeOffRuleFeeService.getWriteOffRule(writeOffRuleInfo, account.getEparchyCode(), account.getProvinceCode(), account.getNetTypeCode());
        //抵扣，补收和增量出账期间需要查询帐务业务后台处理表做处理
        boolean specialState = datumFeeService.isSpecialRecvState(writeOffRuleInfo.getCurCycle());
        if (specialState) {
            //获取帐务业务后台处理表记录
            List<PayLogDmn> payLogDmns = payLogFeeService.getPayLogDmnByAcctId(account.getAcctId(), "0", ActPayPubDef.ACT_RDS_DBCONN_ORDER1);
            if (!CollectionUtils.isEmpty(payLogDmns)) {
                tradeCommInfo.setExistsPayLogDmn(true);
                for (PayLogDmn payLogDmn : payLogDmns) {
                    acctDepositFeeService.genAcctDepositByPayLogDmn(tradeCommInfoIn, payLogDmn, tradeCommInfo);
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

        Account account = tradeCommInfo.getAccount();
        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        //销账规则对象
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //已开帐帐期
        int maxAcctCycleId = writeOffRuleInfo.getMaxAcctCycle().getCycleId();
        //当前帐期
        int currCycleId = writeOffRuleInfo.getCurCycle().getCycleId();
        int tmpAcctCycleId = (currCycleId > ActPayPubDef.MAX_CYCLE_ID ? ActPayPubDef.MAX_CYCLE_ID : currCycleId);
        //待开帐帐期
        int preCurCycleId = TimeUtil.genCycle(maxAcctCycleId, 1);

        String acctId = account.getAcctId();
        String userId = "";
        if (tradeCommInfo.getMainUser() != null) {
            userId = tradeCommInfo.getMainUser().getUserId();
        }
        //获取实时账单
        List<Bill> realBills = billFeeService.getRealBillFromMemDB(tradeCommInfoIn, account.getAcctId(), userId, preCurCycleId, currCycleId);

        //获取往月账单
        List<Bill> bills = new ArrayList();
        if ("1".equals(tradeCommInfoIn.getQryBillType())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            bills = billFeeService.getBillOweByUserId(acctId, userId, ActPayPubDef.MIN_CYCLE_ID, tmpAcctCycleId, ActPayPubDef.ACTING_DRDS_DBCONN);
        } else {
            bills = billFeeService.getBillOweByAcctId(acctId, ActPayPubDef.MIN_CYCLE_ID, tmpAcctCycleId, ActPayPubDef.ACTING_DRDS_DBCONN);
        }


        /*
         *  内存数据库方式，开帐标志由销帐打，抵扣时候尚未开帐，判断抵扣入库期间或增量出账期间
         *  如果有开帐帐期的帐单已入库需要去除实时帐单中的对应月份的部分
         *  因为这个时候开帐标识use_tag='0'有两个月的实时话费，而实际上已经有帐单入库了
         *  多个抵扣入库进程没有办法控制一起完成
         */
        if (cycleFeeService.isDrecvPeriod(writeOffRuleInfo.getCurCycle())) {
            if (preCurCycleId < currCycleId) {
                realBills = billFeeService.removeWriteOffRealBill(bills, realBills,
                        acctId, preCurCycleId, tradeCommInfoIn.getProvinceCode());
            }
        }

        //设置实时账单的BILL_ID
        if (!CollectionUtils.isEmpty(realBills)
                && !CollectionUtils.isEmpty(tradeCommInfo.getPayUsers())) {
            //可能的账单实例条数
            int billIdCount = tradeCommInfo.getPayUsers().size();
            //如果实时账单不止一个账期，序列值加倍
            if (TimeUtil.diffMonths(currCycleId, maxAcctCycleId) > 1) {
                billIdCount *= 2;
            }

            billFeeService.updateRealBillId(realBills, billIdCount,
                    tradeCommInfoIn.getEparchyCode(), tradeCommInfoIn.getProvinceCode());
        }

        //将实时账单添加到账单列表
        bills.addAll(realBills);
        //坏账缴费时需要加载坏账账单
        if (!StringUtil.isEmpty(tradeCommInfoIn.getBadTypeCode())
                && datumFeeService.isBadBillUser(acctId, ActPayPubDef.ACTING_DRDS_DBCONN)) {
            String badTypeCode = tradeCommInfoIn.getBadTypeCode();
            //捞取坏账账单
            List<Bill> badBills = billFeeService.getBadBillOweByAcctId(acctId, maxAcctCycleId,
                    tmpAcctCycleId, ActPayPubDef.ACTING_DRDS_DBCONN);
            if (!CollectionUtils.isEmpty(badBills)) {
                //坏账缴费处理方式
                CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_BADBILL_PAYFEE);
                if (commPara == null) {
                    throw new SkyArkException("ASM_BADBILL_PAYFEE参数没有配置!");
                }

                //坏账缴费只销缴费号码的坏账，针对合账用户，可能存在同账户下其他用户的坏账不会销掉
                if ("2".equals(tradeCommInfoIn.getWriteoffMode()) && !"".equals(userId)) {
                    List<Bill> debBill = new ArrayList<>();
                    for (Bill bill : badBills) {
                        if (userId.equals(bill.getUserId())) {
                            debBill.add(bill);
                        }
                    }
                    badBills = debBill;
                }

                if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1()) && "1".equals(commPara.getParaCode1())) {
                    //只有话费收取传"1"，其他欠费查询都不传,坏帐缴费传"2",其他欠费查询途径为"";
                    if ("1".equals(badTypeCode)) {
                        throw new SkyArkException("用户存在坏帐，请先到坏帐界面结清坏帐!acctId=" + acctId);
                    }
                    bills = badBills;
                    //坏帐不触发信控
                    tradeCommInfo.setFireCreditCtrl(false);
                } else {
                    for (Bill bill : badBills) {
                        bills.add(bill);
                    }
                }
                tradeCommInfo.setHasBadBill(true);
            }
        } else {
            tradeCommInfo.setHasBadBill(false);
        }
        tradeCommInfo.setBills(bills);
    }

    @Override
    public void specialTradeCheck(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
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
        Account account = tradeCommInfo.getAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有获取用户信息，请先查询三户资料");
        }

        //免滞纳金计算用户不计算滞纳金
        if (datumFeeService.isNoCalcLateFeeUser("-1",
                account.getAcctId(), ActPayPubDef.ACTING_DRDS_DBCONN)) {
            return true;
        }

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //取得不算滞纳金的付费方式
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_CALCLATEFEE_PAYMODE_LIMIT);
        if (commPara != null && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
            String[] payFeeModes = commPara.getParaCode1().split("\\|");
            for (String payFeeMode : payFeeModes) {
                //如果限制了就不算滞纳金
                if (payFeeMode.equals(account.getPayModeCode())) {
                    return true;
                }
            }
        }
        //配置参数启用，只有离网用户计算滞纳金
        CommPara userCalcPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_CALCLATEFEE_DESTROY_USER);
        if (userCalcPara != null && "1".equals(userCalcPara.getParaCode1())
                && "0".equals(tradeCommInfo.getMainUser().getRemoveTag())) {
            return true;
        }
        return false;
    }

    @Override
    public void getDerateFeeLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(tradeCommInfo.getBills())) {
            return;
        }

        List<Bill> bills = tradeCommInfo.getBills();
        int minCycleId = bills.get(0).getCycleId();
        int maxCycleId = bills.get(0).getCycleId();
        String acctId = bills.get(0).getAcctId();
        for (Bill bill : bills) {
            if (bill.getCycleId() > maxCycleId) {
                maxCycleId = bill.getCycleId();
            }

            if (bill.getCycleId() < minCycleId) {
                minCycleId = bill.getCycleId();
            }
        }
        List<DerateLateFeeLog> derateLateFeeLogs = derateLateFeeLogFeeService.getDerateLateFeeLog(acctId, minCycleId, maxCycleId, ActPayPubDef.ACTING_DRDS_DBCONN);
        tradeCommInfo.setDerateLateFeeLogs(derateLateFeeLogs);
    }

    @Override
    public void getAcctPaymentCycle(TradeCommInfo tradeCommInfo, String acctId, String provinceCode) {
        tradeCommInfo.setAcctPaymentCycle(datumFeeService.getAcctPaymentCycle(acctId, provinceCode));
    }

    /**
     * 托收在途
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifBillConsigning(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_CAN_RECV);
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
            List<Bill> bills = tradeCommInfo.getBills();
            for (Bill bill : bills) {
                if ('7' == bill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 托收账户
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifConsignPayMode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())
                || tradeCommInfo.getAccount() == null) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_CONSIGN_CAN_RECV参数没有配置!");
        }

        //1000041银行代扣和托收一样的处理
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "2".equals(commPara.getParaCode1())
                && (tradeCommInfoIn.getPaymentId() != 100004
                && tradeCommInfoIn.getPaymentId() != 1000041
                && tradeCommInfoIn.getPaymentId() != 1000044)) {
            CommPara rCommPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_PAY_MODE);
            if (rCommPara == null) {
                throw new SkyArkException("ASM_CONSIGN_PAY_MODE参数没有配置!");
            }
            String[] consignPayMode = rCommPara.getParaCode1().split("\\|");
            //托收在途判断
            Account account = tradeCommInfo.getAccount();
            for (String payFeeMode : consignPayMode) {
                if (payFeeMode.equals(account.getPayModeCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 代理商预打发票
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifPrePrintInvoice(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if ("15000".equals(tradeCommInfoIn.getChannelId())) {
            return false;
        }

        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())) {
            return false;
        }
        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_PRE_PRINTINVOICE_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_PRE_PRINTINVOICE_CAN_RECV参数没有配置!");
        }

        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "1".equals(commPara.getParaCode1())
                && !tradeCommInfoIn.getChannelId().equals(commPara.getParaCode2())) {
            //发票预打在途,(只有后台进程才能缴费)
            List<Bill> bills = tradeCommInfo.getBills();
            for (Bill bill : bills) {
                if ('8' == bill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 电子赠款停机状态不能赠送
     *
     * @param mainUser
     * @param paymentId
     * @param writeOffRuleInfo
     */
    private void elecPresentLimit(User mainUser, int paymentId, WriteOffRuleInfo writeOffRuleInfo) {
        String userStateCode = mainUser.getServiceStateCode();
        if (!StringUtil.isEmptyCheckNullStr(mainUser.getServiceStateCode())
                && !"0".equals(userStateCode) && !"N".equals(userStateCode)) {
            CommPara commPara1 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_PRESENT_LIMITPAYMENT);
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

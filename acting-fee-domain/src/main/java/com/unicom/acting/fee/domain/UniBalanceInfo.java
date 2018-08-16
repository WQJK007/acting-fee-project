package com.unicom.acting.fee.domain;

/**
 * 统一余额播报内容说明
 * <p>
 * 统一余额播报公式
 * 账户余额 = 账户可用余额 + 账户冻结余额
 * 账户可用余额 = 可用预存款 + 可用赠款
 * 账户冻结余额 = 冻结预存款 + 冻结赠款
 * 可用预存款 = 专项可用预存款 + 普通可用预存款
 * 可用赠款 = 专项可用赠款 + 普通可用赠款;
 * 冻结预存款 = 专项冻结预存款 + 普通冻结预存款
 * 冻结赠款 = 专项冻结赠款 + 专项冻结赠款
 * 可用额度 = 账户可用余额+信用额度－用户的实时话费－用户的欠费
 * 欠费 = 当期欠费 + 逾期欠费
 * 账户当前可用余额 = 账户余额 - 实时话费 = 账户当前可用预存款 + 账户当前可用赠款
 * 账户当前可用预存款 = 专项当前可用预存款 + 普通当前可用预存款
 * 账户当前可用赠款 = 专项当前可用赠款 + 普通当前可用赠款
 * <p>
 * 统一余额播报方案
 * 原统一余额播报方案分两级进行播报：
 * 一级播报主要信息包括账户可用余额/账户当前可用余额(欠费）、实时话费
 * 二级播报详细信息包括账户余额(可用预存款、可用赠款、冻结预存款、冻结赠款)、信用额度（可选）、可用额度（可选）
 * 账户可用余额/账户当前可用余额不能为负值
 * <p>
 * 现统一余额播报方案分三级进行播报：
 * 一级播报：账户当前可用余额(不含可用预存款中不可用专款)
 * 二级播报：实时话费、欠费、信用额度（可选）、可用额度（可选）
 * 三级播报：账户余额(可用预存款、可用赠款、冻结预存款、冻结赠款)
 * 其中，账户当前可用余额 = 可用预存款 + 可用赠款 - 欠费 - 实时话费 (最终结果可取负值)
 * 不可用专款：专款账本在对当前费用销账后，剩余不能继续用来销账的余额(目前生产环境没有启用，是否启用省份决定)
 * <p>
 * 由于ECS、客服和CBSS前台展示方案不统一，导致统一余额播报返回结果存在两套数据，即可用余额和当前可用余额
 *
 * @author Wangkh
 */
public class UniBalanceInfo {
    /**
     * 账户余额，映射后台C++代码中的RSRV_STR1字段
     */
    private long acctBalance;

    /**
     * 可用预存款，映射后台C++代码中的RSRV_STR2字段
     */
    private long availPreFee;
    /**
     * 当前可用预存款
     */
    private long CurAvailPreFee;

    /**
     * 专项可用预存款，映射后台C++代码中的RSRV_STR3字段
     */
    private long availSpePreFee;

    /**
     * 当前专项可用预存款
     */
    private long CurAvailSpePreFee;

    /**
     * 普通可用预存款，映射后台C++代码中的RSRV_STR4字段
     */
    private long availOrdiPreFee;

    /**
     * 当前普通可用预存款
     */
    private long CurAvailOrdiPreFee;

    /**
     * 可用赠款，映射后台C++代码中的RSRV_STR5字段
     */
    private long availGrants;

    /**
     * 当前可用赠款
     */
    private long CurAvailGrants;
    /**
     * 普通可用预存款，映射后台C++代码中的RSRV_STR6字段
     */
    private long availSpeGrants;

    /**
     * 当前普通可用预存款
     */
    private long CurAvailSpeGrants;
    /**
     * 普通可用预存款，映射后台C++代码中的RSRV_STR7字段
     */
    private long availOrdGrants;
    /**
     * 当前普通可用预存款
     */
    private long CurAvailOrdGrants;
    /**
     * 冻结预存款，映射后台C++代码中的RSRV_STR8字段
     */
    private long frozenPreFee;
    /**
     * 专项冻结预存款，映射后台C++代码中的RSRV_STR9字段
     */
    private long frozenSpePreFee;
    /**
     * 普通冻结预存款，映射后台C++代码中的RSRV_STR10字段
     */
    private long frozenOrdPreFee;
    /**
     * 冻结赠款，映射后台C++代码中的RSRV_STR11字段
     */
    private long frozenGrants;
    /**
     * 专项冻结赠款，映射后台C++代码中的RSRV_STR12字段
     */
    private long frozenSpeGrants;
    /**
     * 普通冻结赠款，映射后台C++代码中的RSRV_STR13字段
     */
    private long frozenOrdGrants;
    /**
     * 可用额度，映射后台C++代码中的RSRV_STR14字段
     */
    private long avialCredit;
    /**
     * 专项赠款，映射后台C++代码中的RSRV_STR15字段
     */
    private long specialGrants;
    /**
     * 普通赠款，映射后台C++代码中的RSRV_STR16字段
     */
    private long ordinaryGrants;
    /**
     * 专项可用款，映射后台C++代码中的RSRV_STR17字段
     */
    private long availSpecialFee;

    /**
     * 当前专项可用款
     */
    private long CurAvailSpecialFee;
    /**
     * 账户当前可用余额，映射后台C++代码中的RSRV_STR18字段
     */
    private long acctCurAvailBalance;
    /**
     * 账户缴费前可用余额，映射后台C++代码中的ALL_MONEY字段
     */
    private long allMoney;
    /**
     * 账户缴费后可用余额,映射后台C++代码中的ALL_NEW_MONEY字段
     */
    private long allNewMoney;

    public long getAcctBalance() {
        return acctBalance;
    }

    public void setAcctBalance(long acctBalance) {
        this.acctBalance = acctBalance;
    }

    public long getAvailPreFee() {
        return availPreFee;
    }

    public void setAvailPreFee(long availPreFee) {
        this.availPreFee = availPreFee;
    }

    public long getAvailSpePreFee() {
        return availSpePreFee;
    }

    public void setAvailSpePreFee(long availSpePreFee) {
        this.availSpePreFee = availSpePreFee;
    }

    public long getAvailOrdiPreFee() {
        return availOrdiPreFee;
    }

    public void setAvailOrdiPreFee(long availOrdiPreFee) {
        this.availOrdiPreFee = availOrdiPreFee;
    }

    public long getAvailGrants() {
        return availGrants;
    }

    public void setAvailGrants(long availGrants) {
        this.availGrants = availGrants;
    }

    public long getAvailSpeGrants() {
        return availSpeGrants;
    }

    public void setAvailSpeGrants(long availSpeGrants) {
        this.availSpeGrants = availSpeGrants;
    }

    public long getAvailOrdGrants() {
        return availOrdGrants;
    }

    public void setAvailOrdGrants(long availOrdGrants) {
        this.availOrdGrants = availOrdGrants;
    }

    public long getFrozenPreFee() {
        return frozenPreFee;
    }

    public void setFrozenPreFee(long frozenPreFee) {
        this.frozenPreFee = frozenPreFee;
    }

    public long getFrozenSpePreFee() {
        return frozenSpePreFee;
    }

    public void setFrozenSpePreFee(long frozenSpePreFee) {
        this.frozenSpePreFee = frozenSpePreFee;
    }

    public long getFrozenOrdPreFee() {
        return frozenOrdPreFee;
    }

    public void setFrozenOrdPreFee(long frozenOrdPreFee) {
        this.frozenOrdPreFee = frozenOrdPreFee;
    }

    public long getFrozenGrants() {
        return frozenGrants;
    }

    public void setFrozenGrants(long frozenGrants) {
        this.frozenGrants = frozenGrants;
    }

    public long getFrozenSpeGrants() {
        return frozenSpeGrants;
    }

    public void setFrozenSpeGrants(long frozenSpeGrants) {
        this.frozenSpeGrants = frozenSpeGrants;
    }

    public long getFrozenOrdGrants() {
        return frozenOrdGrants;
    }

    public void setFrozenOrdGrants(long frozenOrdGrants) {
        this.frozenOrdGrants = frozenOrdGrants;
    }

    public long getAvialCredit() {
        return avialCredit;
    }

    public void setAvialCredit(long avialCredit) {
        this.avialCredit = avialCredit;
    }

    public long getSpecialGrants() {
        return specialGrants;
    }

    public void setSpecialGrants(long specialGrants) {
        this.specialGrants = specialGrants;
    }

    public long getOrdinaryGrants() {
        return ordinaryGrants;
    }

    public void setOrdinaryGrants(long ordinaryGrants) {
        this.ordinaryGrants = ordinaryGrants;
    }

    public long getAvailSpecialFee() {
        return availSpecialFee;
    }

    public void setAvailSpecialFee(long availSpecialFee) {
        this.availSpecialFee = availSpecialFee;
    }

    public long getAcctCurAvailBalance() {
        return acctCurAvailBalance;
    }

    public void setAcctCurAvailBalance(long acctCurAvailBalance) {
        this.acctCurAvailBalance = acctCurAvailBalance;
    }

    public long getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(long allMoney) {
        this.allMoney = allMoney;
    }

    public long getAllNewMoney() {
        return allNewMoney;
    }

    public void setAllNewMoney(long allNewMoney) {
        this.allNewMoney = allNewMoney;
    }

    public long getCurAvailPreFee() {
        return CurAvailPreFee;
    }

    public void setCurAvailPreFee(long CurAvailPreFee) {
        this.CurAvailPreFee = CurAvailPreFee;
    }

    public long getCurAvailSpePreFee() {
        return CurAvailSpePreFee;
    }

    public void setCurAvailSpePreFee(long CurAvailSpePreFee) {
        this.CurAvailSpePreFee = CurAvailSpePreFee;
    }

    public long getCurAvailOrdiPreFee() {
        return CurAvailOrdiPreFee;
    }

    public void setCurAvailOrdiPreFee(long CurAvailOrdiPreFee) {
        this.CurAvailOrdiPreFee = CurAvailOrdiPreFee;
    }

    public long getCurAvailGrants() {
        return CurAvailGrants;
    }

    public void setCurAvailGrants(long CurAvailGrants) {
        this.CurAvailGrants = CurAvailGrants;
    }

    public long getCurAvailSpeGrants() {
        return CurAvailSpeGrants;
    }

    public void setCurAvailSpeGrants(long CurAvailSpeGrants) {
        this.CurAvailSpeGrants = CurAvailSpeGrants;
    }

    public long getCurAvailOrdGrants() {
        return CurAvailOrdGrants;
    }

    public void setCurAvailOrdGrants(long CurAvailOrdGrants) {
        this.CurAvailOrdGrants = CurAvailOrdGrants;
    }

    public long getCurAvailSpecialFee() {
        return CurAvailSpecialFee;
    }

    public void setCurAvailSpecialFee(long CurAvailSpecialFee) {
        this.CurAvailSpecialFee = CurAvailSpecialFee;
    }

    @Override
    public String toString() {
        return "UniBalanceInfo{" +
                "acctBalance=" + acctBalance +
                ", availPreFee=" + availPreFee +
                ", CurAvailPreFee=" + CurAvailPreFee +
                ", availSpePreFee=" + availSpePreFee +
                ", CurAvailSpePreFee=" + CurAvailSpePreFee +
                ", availOrdiPreFee=" + availOrdiPreFee +
                ", CurAvailOrdiPreFee=" + CurAvailOrdiPreFee +
                ", availGrants=" + availGrants +
                ", CurAvailGrants=" + CurAvailGrants +
                ", availSpeGrants=" + availSpeGrants +
                ", CurAvailSpeGrants=" + CurAvailSpeGrants +
                ", availOrdGrants=" + availOrdGrants +
                ", CurAvailOrdGrants=" + CurAvailOrdGrants +
                ", frozenPreFee=" + frozenPreFee +
                ", frozenSpePreFee=" + frozenSpePreFee +
                ", frozenOrdPreFee=" + frozenOrdPreFee +
                ", frozenGrants=" + frozenGrants +
                ", frozenSpeGrants=" + frozenSpeGrants +
                ", frozenOrdGrants=" + frozenOrdGrants +
                ", avialCredit=" + avialCredit +
                ", specialGrants=" + specialGrants +
                ", ordinaryGrants=" + ordinaryGrants +
                ", availSpecialFee=" + availSpecialFee +
                ", CurAvailSpecialFee=" + CurAvailSpecialFee +
                ", acctCurAvailBalance=" + acctCurAvailBalance +
                ", allMoney=" + allMoney +
                ", allNewMoney=" + allNewMoney +
                '}';
    }
}

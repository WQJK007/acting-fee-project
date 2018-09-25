package com.unicom.acting.fee.batch.writer;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.fee.batch.domain.DataBufHolder;
import com.unicom.acting.fee.batch.domain.QryOweFeeHolder;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.acting.fee.writeoff.service.OweFeeCommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Created on 2018-8-29.
 * @Author: zhengp11
 * @Version: 1.0
 * @Function:
 */
@Component("oweFeeInfoWriter")
@StepScope
public class OweFeeInfoWriter implements ItemWriter<BTradeCommInfo<TradeCommInfo>> {
    private static final Logger logger = LoggerFactory.getLogger(OweFeeInfoWriter.class);

    @Autowired
    private OweFeeCommService oweFeeCommServiceImpl;

    @Autowired
    private QryOweFeeHolder<FeeCommInfoIn> qryOweFeeHolder;

    @Autowired
    private DataBufHolder<TradeCommInfoOut> dataBufHolder;

    @Override
    public void write(List<? extends BTradeCommInfo<TradeCommInfo>>  btradeCommInfo){
        logger.info(btradeCommInfo.toString());
        int count = qryOweFeeHolder.getOrderNumber();
        FeeCommInfoIn feeCommInfoIn = qryOweFeeHolder.getListInstance().get(count);
        feeCommInfoIn.setTargetData("0"); //取欠费信息
        TradeCommInfo tradeCommInfo = btradeCommInfo.get(0).getTradeCommonInfo();
        TradeCommInfoOut tradeCommInfoOut = oweFeeCommServiceImpl.genOweFeeCommInfoOut(feeCommInfoIn,tradeCommInfo);
        dataBufHolder.getListInstance().add(tradeCommInfoOut);
    }

}

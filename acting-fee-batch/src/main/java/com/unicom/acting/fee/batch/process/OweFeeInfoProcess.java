package com.unicom.acting.fee.batch.process;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.fee.batch.domain.BatchLinkInfo;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.FeeCommInfoIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**************************************
 * @Created on 2018-8-29.      ******
 * @Author: zhengp11            ******
 * @Version: 1.0                ******
 * @Function:                   ******
 *************************************/
@Component("oweFeeInfoProcess")
@StepScope
public class OweFeeInfoProcess implements ItemProcessor<BTradeCommInfo<TradeCommInfo>,BTradeCommInfo<TradeCommInfo>> {

    private static final Logger logger = LoggerFactory.getLogger(OweFeeInfoProcess.class);

    @Autowired
    private CalculateService calculateService;

    @Override
    public BTradeCommInfo<TradeCommInfo> process(BTradeCommInfo<TradeCommInfo> btradeCommInfo) {
        TradeCommInfo tradeCommInfo = btradeCommInfo.getTradeCommonInfo();
        logger.info("处理欠费查询入库信息！");

        calculateService.calc(tradeCommInfo);

        btradeCommInfo.setTradeCommonInfo(tradeCommInfo);
        logger.info("处理欠费查询入库信息完成！");

        return btradeCommInfo;
    }
}

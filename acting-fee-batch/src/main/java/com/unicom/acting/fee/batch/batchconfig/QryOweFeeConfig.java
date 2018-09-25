package com.unicom.acting.fee.batch.batchconfig;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.fee.batch.initializer.ParamsLoader;
import com.unicom.acting.fee.batch.listener.QryOweFeeStepListener;
import com.unicom.acting.fee.batch.process.OweFeeInfoProcess;
import com.unicom.acting.fee.batch.reader.OweFeeInfoReader;
import com.unicom.acting.fee.batch.writer.OweFeeInfoWriter;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @Created on 2018-8-29.
 * @Author: zhengp11
 * @Version: 1.0
 * @Function:
 */
@EnableBatchProcessing
@Configuration
@ComponentScan("com.unicom.acting.fee.batch")
public class QryOweFeeConfig {

    private static final Logger logger = LoggerFactory.getLogger(QryOweFeeConfig.class);

    @Autowired
    private JdbcBaseDao jdbcBaseDao;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private ParamsLoader paramsLoader;

    @Bean("dataSource")
    public DataSource getRepositoryDataSource() {
        logger.info("JobConfig 数据库连接获取");
        return jdbcBaseDao.getJdbcTemplate(DbTypes.ACT_RDS,"*").getJdbcTemplate().getDataSource();
    }

    @Bean("qryOweFeeJob")
    public Job createQryDebFeeJob(@Qualifier("step-qryowefee") Step qryOweFeeStep)
    {
        return this.jobBuilderFactory.get("qryOweFeeJob")
                .start(qryOweFeeStep)
                .build();
    }

    @Bean("step-qryowefee")
    public Step qryOweFeeStep(@Qualifier("oweFeeInfoReader") OweFeeInfoReader reader,
                                 @Qualifier("oweFeeInfoProcess") OweFeeInfoProcess process,
                                 @Qualifier("oweFeeInfoWriter") OweFeeInfoWriter writer,
                              @Qualifier("listener-qryOwefee") QryOweFeeStepListener listener) {
        return this.stepBuilderFactory.get("step-qryowefee")
                .<BTradeCommInfo<TradeCommInfo>,BTradeCommInfo<TradeCommInfo>>chunk(1)
                .reader(reader)
                .processor(process)
                .writer(writer)
                .listener(listener)
                .build();

    }
}

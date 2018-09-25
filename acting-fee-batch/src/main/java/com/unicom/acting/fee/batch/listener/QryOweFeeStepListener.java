package com.unicom.acting.fee.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import javax.batch.api.listener.StepListener;

/**
 * @Created on 2018-9-12.
 * @Author: zhengp11
 * @Version: 1.0
 * @Function:
 */
@StepScope
@Component("listener-qryOwefee")
public class QryOweFeeStepListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(QryOweFeeStepListener.class);


    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobExecution jobEx = stepExecution.getJobExecution();
        logger.info("#############" + jobEx.getJobId() + ":" + stepExecution.getStepName() + " begin execute #############");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        JobExecution jobEx = stepExecution.getJobExecution();
        logger.info("===========>" + jobEx.getJobId() + ":" + stepExecution.getStepName() + " after execute <===============");
        String exitCode = stepExecution.getExitStatus().getExitCode();
        if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) &&
                stepExecution.getSkipCount() > 0) {
            return new ExitStatus("COMPLETED with SKIPS");
        }
        else {
            return stepExecution.getExitStatus();
        }
    }
}

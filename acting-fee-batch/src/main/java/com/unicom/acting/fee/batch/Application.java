package com.unicom.acting.fee.batch;

import com.unicom.skyark.component.SkyArkApplication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**************************************
 * @Created on 2018-9-4.      ******
 * @Author: zhengp11            ******
 * @Version: 1.0                ******
 * @Function:                   ******
 *************************************/
@SkyArkApplication(exclude= {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class, DataSourceHealthIndicatorAutoConfiguration.class})
@RestController
public class Application {

    @Autowired
    @Qualifier("qryOweFeeJob")
    private Job myJob;

    @Autowired
    private JobLauncher jobLauncher;

    @RequestMapping(value = "/startjob", method = RequestMethod.GET)
    public String startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("startId", "0")
                .addString("endId", "9999")
                .addString("userId","1113110400000728")
                .addString("provinceCode", "11")
                .addString("eparchyCode","0010")
                .addString("netTypeCode","50")
                .addLong("curTime", System.currentTimeMillis())
                .addString("jobName", "欠费查询")
                .toJobParameters();
        this.jobLauncher.run(myJob, jobParameters);
        return "success";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }


}

package com.cbss.microservice;

import com.unicom.act.framework.common.constants.SysTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Wangkh
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class})
@EnableHystrix
@EnableCaching
@ComponentScan(basePackages = {SysTypes.BUSI_PATH, "com.cbss"})
public class ActingFeeTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActingFeeTestApplication.class, args);
    }

}

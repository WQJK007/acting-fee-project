package com.unicom.acting.fee.batch.domain;

import com.unicom.acting.batch.common.domain.JobInstancesHolder;
import org.springframework.stereotype.Component;

/**
 * @Created on 2018-9-20.
 * @Author: zhengp11
 * @Version: 1.0
 * @Function:
 */
@Component("databufholder")
public class DataBufHolder<T> extends JobInstancesHolder<T> {

}

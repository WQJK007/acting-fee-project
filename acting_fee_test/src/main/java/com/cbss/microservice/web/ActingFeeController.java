package com.cbss.microservice.web;

import com.cbss.microservice.domain.QryOweFeeInfoRsp;
import com.cbss.microservice.service.QryOweFeeService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.unicom.act.framework.common.constants.SysTypes;
import com.unicom.act.framework.exception.CbssException;
import com.unicom.act.framework.util.StringUtil;
import com.unicom.act.framework.web.BaseController;
import com.unicom.act.framework.web.data.BaseRsp;
import com.unicom.act.framework.web.hystrix.CbssRouteType;
import com.unicom.act.framework.web.hystrix.annotation.CustomParamRouteHystrixCommand;
import com.unicom.act.framework.web.hystrix.annotation.HystrixRouteProperty;
import com.unicom.act.framework.web.hystrix.annotation.RouteParam;
import com.unicom.act.framework.web.util.ServiceStandardUtil;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.owefee.service.OweFeeService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wangkh
 */
@RequestMapping("/acting/fee/test")
@Api(description = "账务费用查询测试", tags = "queryOweFee", value = "账务费用查询测试")
@RestController
public class ActingFeeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ActingFeeController.class);
    @Autowired
    private QryOweFeeService qryOweFeeService;

    @CustomParamRouteHystrixCommand(
            customRouteProperties = {
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT1, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread1", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT2, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread2", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT3, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread3", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT4, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread4", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT5, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread5", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT6, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread6", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT7, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread7", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),
                    @HystrixRouteProperty(routeExp = CbssRouteType.ACT8, hystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread8", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class)),},
            otherRouteHystrixCommand = @HystrixCommand(groupKey = "feetest", commandKey = "queryOweFee", threadPoolKey = "queryOweFeeThread", fallbackMethod = "fallBackOweFeeInfo", ignoreExceptions = CbssException.class))
    @ApiOperation(value = "账务费用查询测试", notes = "账务费用查询测试", response = BaseRsp.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "输入的参数无效"), @ApiResponse(code = 404, message = "找不到结果数据")})
    @RequestMapping(value = "/qryowefee/{provinceId}/{eparchyCode}", method = RequestMethod.GET)
    public BaseRsp queryOweFee(@ApiParam(name = "provinceId", value = "省份编码") @PathVariable @RouteParam String provinceId,
                               @ApiParam(name = "eparchyCode", value = "地市编码") @PathVariable String eparchyCode,
                               @ApiParam(name = "QUERY_TYPE", value = "查询类型(1:业务号码，2：按账户)", required = true) @RequestParam(name = "QUERY_TYPE") String queryType,
                               @ApiParam(name = "ACCT_ID", value = "账户ID") @RequestParam(required = false, name = "ACCT_ID") String acctId,
                               @ApiParam(name = "SERVICE_CLASS_CODE", value = "网别") @RequestParam(required = false, name = "SERVICE_CLASS_CODE") String serviceClassCode,
                               @ApiParam(name = "AREA_CODE", value = "长途区号") @RequestParam(required = false, name = "AREA_CODE") String areaCode,
                               @ApiParam(name = "SERIAL_NUMBER", value = "业务号码") @RequestParam(required = false, name = "SERIAL_NUMBER") String serialNumber,
                               @ApiParam(name = "CHARGE_TYPE", value = "查询类别（1：指定用户，2：安账户）", required = true) @RequestParam(name = "CHARGE_TYPE") String chargeType,
                               @ApiParam(name = "BAD_DEBT_TAG", value = "呆坏账标识（0：非呆坏账、1：呆账、2:  坏账、3：立即结账）", required = true) @RequestParam(name = "BAD_DEBT_TAG") String badDebtTag,
                               @RequestHeader(name = SysTypes.CBSSMSGRAY, required = false) String headerGray,
                               @RequestHeader(name = SysTypes.CBSSMSCONSUMER, required = false) String headerConsumer) throws CbssException {
        TradeCommInfoIn tradeCommInfoIn = new TradeCommInfoIn();
        tradeCommInfoIn.setQryBillType("1");
        tradeCommInfoIn.setProvinceCode(provinceId);
        tradeCommInfoIn.setEparchyCode(eparchyCode);
        if ("1".equals(queryType)) {
            if (StringUtil.isEmptyCheckNullStr(serialNumber)) {
                throw new CbssException(SysTypes.BUSI_ERROR_CODE, "传入的业务号码为空，参数：SERIAL_NUMBER！");
            }
            if (StringUtil.isEmptyCheckNullStr(serviceClassCode)) {
                throw new CbssException(SysTypes.BUSI_ERROR_CODE, "传入的网别为空，参数：SERVICE_CLASS_CODE！");
            }
            tradeCommInfoIn.setSerialNumber(serialNumber);
            if (serviceClassCode.length() == 4) {
                tradeCommInfoIn.setNetTypeCode(serviceClassCode.substring(serviceClassCode.length() - 2));
            } else {
                tradeCommInfoIn.setNetTypeCode(serviceClassCode);
            }
        } else if ("2".equals(queryType)) {
            if (StringUtil.isEmptyCheckNullStr(acctId)) {
                throw new CbssException(SysTypes.BUSI_ERROR_CODE, "传入的账户标识为空，参数：ACCT_ID！");
            }
            tradeCommInfoIn.setAcctId(acctId);
        } else {
            throw new CbssException(SysTypes.BUSI_ERROR_CODE, "传入的查询类型参数有误，参数:QUERY_TYPE！");
        }
        tradeCommInfoIn.setTargetData("2");
        //查询类别 1 账户下指定用户的余额查询（账户ID+用户ID） 0:账户余额查询
        if ("1".equals(chargeType)) {
            tradeCommInfoIn.setWriteoffMode("2");
        } else {
            tradeCommInfoIn.setWriteoffMode("1");
        }
        tradeCommInfoIn.setTradeEnter("1");//校验大合账
        tradeCommInfoIn.setHeaderGray(headerGray);
        //调用欠费查询
        List<QryOweFeeInfoRsp> qryOweFeeInfoRsps = qryOweFeeService.qryOweFeeInfo(tradeCommInfoIn);
        return ServiceStandardUtil.createSuccessRsp(qryOweFeeInfoRsps);
    }

    public BaseRsp fallBackOweFeeInfo(String provinceId, String eparchyCode, String queryType, String acctId, String serviceClassCode, String areaCode, String serialNumber, String chargeType, String badDebtTag, String headerGray, String headerConsumer, Throwable e) {
        logger.error(e.getMessage(), e);
        return ServiceStandardUtil.createErrorRsp(e);
    }

}

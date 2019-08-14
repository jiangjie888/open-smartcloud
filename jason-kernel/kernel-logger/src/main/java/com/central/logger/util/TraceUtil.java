package com.central.logger.util;

import com.central.core.utils.HttpContext;
import com.central.core.utils.SpringUtil;
import com.central.core.utils.ToolUtil;
import com.central.logger.chain.enums.RpcPhaseEnum;
import com.central.logger.config.properties.LogProperties;
import com.central.logger.entity.SendingTCLog;
import com.central.logger.entity.SendingTraceLog;
import com.central.logger.service.LogProducerService;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 日志记录工具
 */
public class TraceUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void trace(
            MethodSignature methodSignature, RpcPhaseEnum rpcPhaseEnum, String traceId, String spanId, String parentSpanId) {
        trace(methodSignature, rpcPhaseEnum, traceId, spanId, parentSpanId, "");
    }

    public static void trace(
            MethodSignature methodSignature, RpcPhaseEnum rpcPhaseEnum, String traceId, String spanId, String parentSpanId, String errorMessage) {

        if (isTraceFlag()) {

            String servletPath = "";

            try {
                servletPath = HttpContext.getRequest().getServletPath();
            } catch (NullPointerException e) {
                //为空代表当前没有http请求
            }

            SendingTraceLog sendingTraceLog = new SendingTraceLog();
            sendingTraceLog.setIp(ToolUtil.getIP());
            sendingTraceLog.setAppCode(ToolUtil.getApplicationName());
            sendingTraceLog.setCreateTimestamp(System.currentTimeMillis());
            sendingTraceLog.setParentSpanId(parentSpanId);
            sendingTraceLog.setSpanId(spanId);
            sendingTraceLog.setRpcPhase(rpcPhaseEnum.name());
            sendingTraceLog.setServletPath(servletPath);
            sendingTraceLog.setTraceId(traceId);
            sendingTraceLog.setContent(errorMessage);

            try {
                getLogProducer().sendTraceMsg(sendingTraceLog);
            } catch (Exception e) {
                logger.error("发送trace消息错误！", e);
            }
        }

    }

    public static void trace(String requestPath, Long useTime) {

        if (isTraceFlag()) {

            SendingTCLog sendingTCLog = new SendingTCLog();
            sendingTCLog.setCreateTime(new Date());
            sendingTCLog.setRequestPath(requestPath);
            sendingTCLog.setUseTime(useTime);

            try {
                getLogProducer().sendTcMsg(sendingTCLog);
            } catch (Exception e) {
                logger.error("发送trace tc消息错误！", e);
            }
        }

    }

    private static LogProducerService getLogProducer() {
        return SpringUtil.getBean(LogProducerService.class);
    }

    private static LogProperties getLogProperties() {
        return SpringUtil.getBean(LogProperties.class);
    }

    private static boolean isTraceFlag() {

        Boolean traceFlag = null;

        try {
            traceFlag = getLogProperties().getTrace();
        } catch (Exception e) {
            logger.error("获取trace！", e);
            traceFlag = false;
        }

        if (traceFlag == null) {
            return false;
        } else {
            return traceFlag;
        }

    }
}

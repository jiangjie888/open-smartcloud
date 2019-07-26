package com.smartcloud.logger.chain.aop;

import com.central.core.model.api.base.AbstractBaseRequest;
import com.central.core.utils.ToolUtil;
import com.smartcloud.logger.chain.context.ParentSpanIdHolder;
import com.smartcloud.logger.chain.context.SpanIdHolder;
import com.smartcloud.logger.chain.context.TraceIdHolder;
import com.smartcloud.logger.chain.enums.RpcPhaseEnum;
import com.smartcloud.logger.util.TraceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import static com.central.core.model.constants.AopSortConstants.CHAIN_ON_CONSUMMER_SORT;


/**
 * 基于调用链的服务治理系统的设计（feign远程调用层的aop处理）
 */
@Aspect
@Order(CHAIN_ON_CONSUMMER_SORT)
public class ChainOnConsumerAop {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* *..api.*+.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {

        MethodSignature methodSignature = null;
        Signature signature = point.getSignature();
        if (signature != null && signature instanceof MethodSignature) {
            methodSignature = (MethodSignature) signature;
        }

        long begin = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("开始记录consumer aop耗时！");
        }

        //获取当前节点的spanId
        String currentSpanId = SpanIdHolder.get();

        //获取当前节点的parentSpanId
        String parentSpanId = ParentSpanIdHolder.get();

        //获取traceId
        String traceId = TraceIdHolder.get();

        if (logger.isDebugEnabled()) {
            logger.debug("consumer aop 获取参数！" + (System.currentTimeMillis() - begin));
        }

        try {

            //报告:开始调用远程服务
            TraceUtil.trace(methodSignature, RpcPhaseEnum.P2, traceId, currentSpanId, parentSpanId);

            //设置方法的参数，添加上requestNo和spanId
            setRequestParam(point);

            if (logger.isDebugEnabled()) {
                logger.debug("consumer aop 开始调用远程服务前！" + (System.currentTimeMillis() - begin));
            }

            Object result = point.proceed();

            if (logger.isDebugEnabled()) {
                logger.debug("consumer aop 调用完远程服务！" + (System.currentTimeMillis() - begin));
            }

            if (logger.isDebugEnabled()) {
                logger.debug("consumer aop 发送调用成功！" + (System.currentTimeMillis() - begin));
            }

            return result;

        } catch (Throwable exception) {

            String exceptionMsg = ToolUtil.getExceptionMsg(exception);

            //报告:调用远程服务失败
            TraceUtil.trace(methodSignature, RpcPhaseEnum.EP2, traceId, currentSpanId, parentSpanId, exceptionMsg);

            if (logger.isDebugEnabled()) {
                logger.debug("consumer aop 记录完错误日志！" + (System.currentTimeMillis() - begin));
            }

            throw exception;

        }

    }

    /**
     * 设置被拦截方法的参数内部的spanId和RequestNo的值
     */
    private void setRequestParam(ProceedingJoinPoint point) {
        Object[] params = point.getArgs();
        for (Object param : params) {
            if (param instanceof AbstractBaseRequest) {
                AbstractBaseRequest abstractBaseRequest = (AbstractBaseRequest) param;
                abstractBaseRequest.setRequestNo(TraceIdHolder.get());
                abstractBaseRequest.setSpanId(SpanIdHolder.get());
            }
        }
    }
}

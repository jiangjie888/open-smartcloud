package com.central.core.autoconfigure.aop;

import com.central.core.model.constants.AopSortConstants;
import com.central.core.utils.CheckUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * 参数校验的aop
 */
@Aspect
@Order(AopSortConstants.PARAM_VALIDATE_AOP_SORT)
public class ParamValidateAop {

    @Pointcut(value = "@annotation(com.central.core.annotation.ParamValidator)")
    private void cutService() {

    }

    @Around("cutService()")
    public Object doInvoke(ProceedingJoinPoint point) throws Throwable {

        //获取拦截方法的参数
        Object[] methodParams = point.getArgs();

        //如果请求参数为空，直接跳过aop
        if (methodParams == null || methodParams.length <= 0) {
            return point.proceed();
        } else {

            //如果参数中，包含BaseValidatingParam的子类就开始校验参数
            CheckUtil.validateParameters(methodParams);
            return point.proceed();
        }
    }
}

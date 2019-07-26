package com.central.core.autoconfigure.aop;

import com.central.core.model.reqres.request.RequestData;
import com.central.core.utils.RequestDataHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import static com.central.core.model.constants.AopSortConstants.REQUEST_DATA_AOP_SORT;

/**
 * 对控制器调用过程中,提供一种RequestData便捷调用的aop
 */
@Aspect
@Order(REQUEST_DATA_AOP_SORT)
public class RequestDataAop {

    @Pointcut("execution(* *..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
        Object[] params = point.getArgs();

        //如果方法有参数并且参数中有RequestData对象，就临时保存RequestData到RequestDataHolder
        if (params != null && params.length > 0) {
            if (params[0] instanceof RequestData) {
                RequestDataHolder.put((RequestData) params[0]);
            }
        }

        //是否需要删除ThreadLocal的标识，如果proceed()过程中有异常
        //会执行ExceptionHandler，并在ExceptionHandler中清除ThreadLocal中的内容
        boolean needToRemove = true;
        try {
            return point.proceed();
        } catch (Throwable exception) {
            needToRemove = false;
            throw exception;
        } finally {
            if (needToRemove) {
                RequestDataHolder.remove();
            }
        }
    }
}

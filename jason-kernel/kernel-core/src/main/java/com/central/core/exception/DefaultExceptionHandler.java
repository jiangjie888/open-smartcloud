package com.central.core.exception;

import com.central.core.exception.enums.CoreExceptionEnum;
import com.central.core.model.reqres.response.ErrorResponseData;
import com.central.core.model.reqres.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.central.core.model.constants.AopSortConstants.DEFAULT_EXCEPTION_HANDLER_SORT;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 */
@ControllerAdvice
@Order(DEFAULT_EXCEPTION_HANDLER_SORT)
public class DefaultExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 拦截各个服务的具体异常 500
     */
    @ExceptionHandler(ApiServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData apiService(ApiServiceException e) {
        log.error("服务具体异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getExceptionClassName());
        return errorResponseData;
    }

    /**
     * 拦截请求为空的异常 500
     */
    @ExceptionHandler(RequestEmptyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData emptyRequest(RequestEmptyException e) {
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * 拦截业务异常 500
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData notFount(ServiceException e) {
        log.info("业务异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * 拦截未知的运行时异常
     * 返回状态码:500
     */
    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData serverError(Exception e) {
        log.error("运行时异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(CoreExceptionEnum.SERVICE_ERROR.getCode(), CoreExceptionEnum.SERVICE_ERROR.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }*/


    /**
     * IllegalArgumentException异常处理返回json
     * 状态码:400
     */
    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseData badRequestException(IllegalArgumentException e) {
        //return defHandler(HttpStatus.BAD_REQUEST.value(), e.getMessage(),e);
        log.error("服务BAD_REQUEST:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }


    /*private ResponseData defHandler(Integer code,String msg,Exception e) {
        log.error(msg, e);
        ErrorResponseData errorResponseData = new ErrorResponseData(code, msg);
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }*/
}

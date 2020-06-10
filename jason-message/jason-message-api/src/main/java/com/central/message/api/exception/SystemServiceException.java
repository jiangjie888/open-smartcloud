package com.central.message.api.exception;


import com.central.core.exception.AbstractBaseExceptionEnum;
import com.central.core.exception.ApiServiceException;

/**
 * 系统管理服务抛出的异常
 */
public class SystemServiceException extends ApiServiceException {

    public SystemServiceException(AbstractBaseExceptionEnum exception) {
        super(exception);
    }

    @Override
    public String getExceptionClassName() {
        return SystemServiceException.class.getName();
    }

}

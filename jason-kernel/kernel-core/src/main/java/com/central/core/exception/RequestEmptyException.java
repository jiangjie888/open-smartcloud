package com.central.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常的封装
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestEmptyException extends ServiceException {

    public RequestEmptyException() {
        super(400, "请求数据不完整或格式错误！");
    }

    public RequestEmptyException(String errorMessage) {
        super(400, errorMessage);
    }

    /**
     * 不拷贝栈信息，提高性能
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}

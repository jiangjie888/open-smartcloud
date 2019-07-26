package com.central.demo.order.core.exception;

import com.central.core.model.exception.AbstractBaseExceptionEnum;

/**
 * 订单异常
 */
public enum OrderExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 错误枚举
     */
    ORDER_NULL(500, "没有订单"),
    ORDER_ERROR(500, "订单系统内部异常");


    OrderExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

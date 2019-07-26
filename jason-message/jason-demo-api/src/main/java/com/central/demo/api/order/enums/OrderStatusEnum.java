package com.central.demo.api.order.enums;

/**
 * 订单状态
 */
public enum OrderStatusEnum {

    /**
     * 未完成
     */
    NOT_SUCCESS(1),

    /**
     * 已完成
     */
    SUCCESS(2);

    private Integer status;

    OrderStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

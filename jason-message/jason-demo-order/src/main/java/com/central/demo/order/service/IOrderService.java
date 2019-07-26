package com.central.demo.order.service;


import com.central.demo.api.order.model.GoodsOrder;

/**
 * 订单表 服务类
 */
public interface IOrderService {


    /**
     * 测试下单业务
     */
    Long makeTestOrder();

    /**
     * 完成订单
     *
     * 说明：
     *  本业务为分布式事务解决方案之可靠消息最终一致性方案的实例
     */
    void finishOrder(Long orderId);


    //
    GoodsOrder findById(Long id);
}

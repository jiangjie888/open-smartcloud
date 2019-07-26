package com.central.demo.api.order;


import com.central.demo.api.order.model.GoodsOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 商品订单的接口
 */
@RequestMapping("/api/goodsOrder")
public interface GoodsOrderApi {

    /**
     * 根据订单id查询订单记录
     */
    @RequestMapping("/findOrderById")
    GoodsOrder findOrderById(@RequestParam("orderId") Long orderId);

}

package com.central.demo.order.controller;

import com.central.core.model.reqres.request.RequestData;
import com.central.core.model.reqres.response.ResponseData;
import com.central.demo.api.order.model.GoodsOrder;
import com.central.demo.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@Api(tags = "订单模块api")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     *
     */
    @ApiOperation(value = "下订单")
    @RequestMapping(value = { "/place" }, method = { RequestMethod.GET })
    public String place() {
        Long orderId = orderService.makeTestOrder();
        return "order_id = " + orderId;
    }


    /**
     * 完成订单
     */
    @ApiOperation(value = "完成订单")
    @RequestMapping(value = { "/finish" }, method = { RequestMethod.POST })
    public String finish(@RequestParam("orderId") Long orderId) {
        orderService.finishOrder(orderId);
        return "success";
    }

    /**
     * 测试RequestData
     */
    @RequestMapping(value = { "/test" }, method = { RequestMethod.POST }, produces="application/json;charset=UTF-8")
    public Object test(RequestData requestData) {

        String orderId = requestData.getString("goodsName");
        System.out.println(orderId);

        Integer number = requestData.getInteger("count");
        System.out.println(number);

        GoodsOrder goodsOrder = requestData.parse(GoodsOrder.class);
        System.out.println(goodsOrder);

        return ResponseData.success(goodsOrder);
    }
}

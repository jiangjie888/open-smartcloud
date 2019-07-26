package com.central.demo.order.provider;
import com.central.core.utils.ToolUtil;
import com.central.demo.api.order.GoodsOrderApi;
import com.central.demo.api.order.model.GoodsOrder;
import com.central.demo.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 消息服务的接口
 */
@RestController
public class GoodsOrderProvider implements GoodsOrderApi {

    @Autowired
    private IOrderService orderService;

    @Override
    public GoodsOrder findOrderById(@RequestParam("orderId") Long orderId) {

        if (ToolUtil.isEmpty(orderId)) {
            return null;
        } else {
            return orderService.findById(orderId);
        }
    }
}
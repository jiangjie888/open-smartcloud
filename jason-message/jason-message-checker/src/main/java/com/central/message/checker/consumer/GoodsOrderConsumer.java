package com.central.message.checker.consumer;

import com.central.demo.api.order.GoodsOrderApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 消息服务的接口
 */
@FeignClient("message-order")
public interface GoodsOrderConsumer extends GoodsOrderApi {

}
package com.central.demo.order.consumer;

import com.central.message.api.MessageServiceApi;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 消息服务消费者
 *
 */
@Api(tags = "消息服务(feign) API")
@FeignClient("message-service")
public interface MessageServiceConsumer extends MessageServiceApi {

}

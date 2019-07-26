package com.central.message.checker.consumer;

import com.central.message.api.MessageServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 消息服务消费者
 */
@FeignClient("message-service")
public interface MessageServiceConsumer extends MessageServiceApi {
}

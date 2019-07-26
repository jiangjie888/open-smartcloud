package com.central.message.checker.consumer;



import com.central.demo.api.account.FlowRecordApi;
import org.springframework.cloud.openfeign.FeignClient;



/**
 * 消息服务的接
 */
@FeignClient("message-account")
public interface FlowRecordConsumer extends FlowRecordApi {

}
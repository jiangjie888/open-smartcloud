package com.central.demo.api.account;


import com.central.demo.api.account.model.FlowRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 消息服务的接口
 */
@RequestMapping("/api/flowRecord")
public interface FlowRecordApi {

    /**
     * 根据订单id查询流水记录
     */
    @RequestMapping("/getFlowRecordByOrderId")
    FlowRecord findOrderFlowRecord(@RequestParam("orderId") Long orderId);

}

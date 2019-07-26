package com.central.demo.account.provider;


import com.central.demo.account.service.IFlowRecordService;
import com.central.demo.api.account.FlowRecordApi;
import com.central.demo.api.account.model.FlowRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息服务的接口
 */
@Api(tags = "流水模块")
@RestController
public class FlowRecordProvider implements FlowRecordApi {

    @Autowired
    private IFlowRecordService flowRecordService;

    @ApiOperation(value = "根据订单id查询流水记录")
    @Override
    public FlowRecord findOrderFlowRecord(@RequestParam("orderId") Long orderId) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId",orderId);

        List<FlowRecord> orders = flowRecordService.findList(params);

        if (orders != null && !orders.isEmpty()) {
            return orders.get(0);
        } else {
            return null;
        }

    }

}
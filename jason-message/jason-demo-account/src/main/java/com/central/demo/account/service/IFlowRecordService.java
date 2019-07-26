package com.central.demo.account.service;

import com.central.demo.api.account.model.FlowRecord;
import com.central.demo.api.order.GoodsFlowParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流水记录 服务类
 * </p>
 */
public interface IFlowRecordService {

    /**
     * 记录订单流水
     */
    void recordFlow(GoodsFlowParam goodsFlowParam);

    int save(FlowRecord flowRecord);

    int update(FlowRecord flowRecord);

    //int delete(Map<String, Object> params);

    FlowRecord findById(Long id);

    List<FlowRecord> findList(Map<String, Object> params);

}

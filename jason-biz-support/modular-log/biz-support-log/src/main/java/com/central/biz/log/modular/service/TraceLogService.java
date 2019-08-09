package com.central.biz.log.modular.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.biz.log.api.entity.TraceLog;
import com.central.biz.log.modular.dao.TraceLogMapper;
import com.central.biz.log.modular.factory.TraceLogFactory;
import com.central.biz.log.modular.model.TraceLogCondition;
import com.central.biz.log.modular.model.TraceLogParams;
import com.central.core.model.page.PageResultPlus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 */
@Service
public class TraceLogService extends ServiceImpl<TraceLogMapper, TraceLog> {

    /**
     * 获取调用链日志列表（没有查询条件的）
     */
    public PageResultPlus<TraceLog> getTraceLogList(TraceLogParams traceLogParams) {
        Long traceLogCount = this.baseMapper.getTraceLogCount();

        if (traceLogCount == null) {
            traceLogCount = 0L;
        }

        if (traceLogParams.getGtValue() == null) {
            traceLogParams.setGtValue(traceLogCount);
        }

        List<TraceLog> commonLogList = this.baseMapper.getTraceLogList(traceLogParams);
        return TraceLogFactory.getResponse(commonLogList, traceLogCount, traceLogParams);
    }

    /**
     * 获取调用链日志列表（带查询条件的）
     */
    public PageResultPlus<TraceLog> getTraceLogListByCondition(TraceLogCondition traceLogCondition) {
        List<TraceLog> commonLogList = this.baseMapper.getTraceLogListByCondition(traceLogCondition);
        return TraceLogFactory.getResponseCondition(commonLogList, traceLogCondition);
    }
}

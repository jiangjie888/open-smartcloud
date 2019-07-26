package com.central.biz.log.modular.service;

import cn.stylefeng.roses.biz.log.api.entity.TraceLog;
import cn.stylefeng.roses.biz.log.modular.factory.TraceLogFactory;
import cn.stylefeng.roses.biz.log.modular.mapper.TraceLogMapper;
import cn.stylefeng.roses.biz.log.modular.model.TraceLogCondition;
import cn.stylefeng.roses.biz.log.modular.model.TraceLogParams;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
    public PageResult<TraceLog> getTraceLogList(TraceLogParams traceLogParams) {
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
    public PageResult<TraceLog> getTraceLogListByCondition(TraceLogCondition traceLogCondition) {
        List<TraceLog> commonLogList = this.baseMapper.getTraceLogListByCondition(traceLogCondition);
        return TraceLogFactory.getResponseCondition(commonLogList, traceLogCondition);
    }
}

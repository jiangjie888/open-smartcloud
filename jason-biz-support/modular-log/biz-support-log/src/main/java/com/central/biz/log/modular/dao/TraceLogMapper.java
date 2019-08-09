package com.central.biz.log.modular.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.biz.log.api.entity.TraceLog;
import com.central.biz.log.modular.model.TraceLogCondition;
import com.central.biz.log.modular.model.TraceLogParams;

import java.util.List;


public interface TraceLogMapper extends BaseMapper<TraceLog> {

    /**
     * 获取traceLog的总长度
     */
    Long getTraceLogCount();

    /**
     * 获取调用链日志列表
     */
    List<TraceLog> getTraceLogList(TraceLogParams traceLogParams);

    /**
     * 获取调用链日志列表(条件查询)
     */
    List<TraceLog> getTraceLogListByCondition(TraceLogCondition traceLogCondition);

    /**
     * 获取调用链日志列表(条件查询和分页)
     */
    List<TraceLog> getTraceLogListPageByCondition(Page<TraceLog> page, TraceLogCondition traceLogCondition);
}

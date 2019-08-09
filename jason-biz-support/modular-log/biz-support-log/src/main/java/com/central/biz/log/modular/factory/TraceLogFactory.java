package com.central.biz.log.modular.factory;

import cn.hutool.core.date.DateUtil;
import com.central.biz.log.api.entity.TraceLog;
import com.central.biz.log.modular.model.TraceLogCondition;
import com.central.biz.log.modular.model.TraceLogParams;
import com.central.core.model.page.PageResultPlus;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.utils.ToolUtil;

import java.util.List;

/**
 * 查询调用链日志的参数构造器
 */
public class TraceLogFactory {

    /**
     * 获取请求参数
     */
    public static Object getRequest(RequestData requestData) {

        String traceId = requestData.getString("traceId");
        String appCode = requestData.getString("appCode");
        String rpcPhase = requestData.getString("rpcPhase");
        String beginTime = requestData.getString("beginTime");
        String endTime = requestData.getString("endTime");

        if (ToolUtil.isAllEmpty(traceId, appCode, rpcPhase, beginTime, endTime)) {

            TraceLogParams traceLogParams = new TraceLogParams();

            Integer pageNo = requestData.getInteger("pageNo");
            Integer pageSize = requestData.getInteger("pageSize");
            Long gtValue = requestData.getLong("gtValue");

            if (pageNo != null) {
                traceLogParams.setPageNo(pageNo);
            } else {
                traceLogParams.setPageNo(1);
            }
            if (pageSize != null) {
                traceLogParams.setPageSize(pageSize);
            } else {
                traceLogParams.setPageSize(100);
            }
            if (gtValue != null) {
                traceLogParams.setGtValue(gtValue);
            }

            return traceLogParams;

        } else {
            TraceLogCondition traceLogCondition = requestData.parse(TraceLogCondition.class);

            if (traceLogCondition.getPageNo() == null) {
                traceLogCondition.setPageNo(1);
            }
            if (traceLogCondition.getPageSize() == null) {
                traceLogCondition.setPageSize(100);
            }

            if (beginTime != null) {
                traceLogCondition.setBeginTime(DateUtil.parse(beginTime).getTime());
            }
            if (endTime != null) {
                traceLogCondition.setEndTime(DateUtil.parse(endTime).getTime());
            }

            traceLogCondition.setLimitOffset((long) ((traceLogCondition.getPageNo() - 1) * traceLogCondition.getPageSize()));

            return traceLogCondition;
        }
    }

    /**
     * 创建分页的响应结果
     */
    public static PageResultPlus<TraceLog> getResponse(List<TraceLog> traceLogs, Long traceLogCount, TraceLogParams traceLogParams) {
        PageResultPlus<TraceLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(traceLogs);
        pageResultPlus.setTotal(traceLogCount);
        pageResultPlus.setCurrent((long)traceLogParams.getPageNo());
        pageResultPlus.setSize((long)traceLogParams.getPageSize());

        long a = traceLogCount % traceLogParams.getPageSize() == 0 ? 0 : 1;
        pageResultPlus.setTotalPage((long) (a + traceLogCount / traceLogParams.getPageSize()));

        return pageResultPlus;
    }

    /**
     * 创建分页的响应结果(条件查询的)
     */
    public static PageResultPlus<TraceLog> getResponseCondition(List<TraceLog> traceLogs, TraceLogCondition traceLogCondition) {
        PageResultPlus<TraceLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(traceLogs);
        pageResultPlus.setCurrent((long)traceLogCondition.getPageNo());
        pageResultPlus.setSize((long)traceLogCondition.getPageSize());

        return pageResultPlus;
    }
}
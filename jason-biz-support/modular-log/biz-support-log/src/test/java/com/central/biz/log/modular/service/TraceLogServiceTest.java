package com.central.biz.log.modular.service;

import base.BaseJunit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.biz.log.api.entity.TraceLog;
import com.central.biz.log.modular.dao.TraceLogMapper;
import com.central.biz.log.modular.model.TraceLogCondition;
import com.central.biz.log.modular.model.TraceLogParams;
import com.central.core.model.page.PageResultPlus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 日志测试类
 */
public class TraceLogServiceTest extends BaseJunit {

    @Autowired
    private TraceLogMapper traceLogMapper;

    @Test
    public void getCommonLogCount() {
        Long traceLogCount = traceLogMapper.getTraceLogCount();
        System.err.println(traceLogCount);
    }

    @Test
    public void getCommonLogList() {

        long current = System.currentTimeMillis();

        Long traceLogCount = traceLogMapper.getTraceLogCount();
        TraceLogParams traceLogParams = new TraceLogParams();

        traceLogParams.setGtValue(traceLogCount);
        traceLogParams.setPageSize(100);
        traceLogParams.setPageNo(20000);

        List<TraceLog> traceLogList = traceLogMapper.getTraceLogList(traceLogParams);

        PageResultPlus<TraceLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(traceLogList);
        pageResultPlus.setTotal(traceLogCount);
        pageResultPlus.setCurrent(20000L);
        pageResultPlus.setSize(100L);

        long a = traceLogCount % 100 == 0 ? 0 : 1;
        pageResultPlus.setTotalPage((long) (a + traceLogCount / 100));

        //System.err.println(pageResult);
        System.out.println(System.currentTimeMillis() - current);
    }

    @Test
    public void getCommonLogListByCondition() {

        long current = System.currentTimeMillis();

        TraceLogCondition traceLogCondition = new TraceLogCondition();
        traceLogCondition.setRpcPhase("L1");
        traceLogCondition.setPageSize(100);
        traceLogCondition.setLimitOffset(1000000L);

        List<TraceLog> traceLog = traceLogMapper.getTraceLogListByCondition(traceLogCondition);

        PageResultPlus<TraceLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(traceLog);
        pageResultPlus.setCurrent(1L);
        pageResultPlus.setSize(100L);

        //pageResult.setTotalRows(commonLogCount);
        //long a = commonLogCount % 100 == 0 ? 0 : 1;
        //pageResult.setTotalPage((int) (a + commonLogCount / 100));

        //System.err.println(pageResult);
        System.err.println(System.currentTimeMillis() - current);

    }

    @Test
    public void getCommonLogListPageByCondition() {

        long current = System.currentTimeMillis();

        TraceLogCondition traceLogCondition = new TraceLogCondition();
        traceLogCondition.setRpcPhase("L1");

        List<TraceLog> commonLogList = traceLogMapper.getTraceLogListPageByCondition(new Page<>(100000, 100),
                traceLogCondition);

        PageResultPlus<TraceLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(commonLogList);
        pageResultPlus.setCurrent(100000L);
        pageResultPlus.setSize(100L);

        //pageResult.setTotalRows(commonLogCount);
        //long a = commonLogCount % 100 == 0 ? 0 : 1;
        //pageResult.setTotalPage((int) (a + commonLogCount / 100));

        //System.err.println(pageResult);
        System.err.println(System.currentTimeMillis() - current);

    }
}

package com.central.biz.log.modular.service;

import base.BaseJunit;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.biz.log.api.entity.CommonLog;
import com.central.biz.log.modular.dao.CommonLogMapper;
import com.central.biz.log.modular.model.CommonLogCondition;
import com.central.biz.log.modular.model.CommonLogParams;
import com.central.core.model.page.PageResultPlus;
import com.smartcloud.logger.entity.SendingCommonLog;
import com.smartcloud.logger.service.LogProducerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 日志测试类
 */
public class CommonLogServiceTest extends BaseJunit {

    @Autowired
    private LogProducerService logProducerService;

    @Autowired
    private CommonLogMapper commonLogMapper;

    @Test
    public void addCommonLog() throws InterruptedException {
        long begin = System.currentTimeMillis();
        for (int j = 0; j < 50000; j++) {
            String requestNo = RandomUtil.randomString(32);
            for (int k = 0; k < 100; k++) {
                CommonLog commonLog = new CommonLog();
                commonLog.setAccountId(RandomUtil.randomString(32));
                commonLog.setAppCode("ECOM");
                commonLog.setLevel("ERROR");
                commonLog.setClassName(RandomUtil.randomString(10));
                commonLog.setLogContent(RandomUtil.randomString(1000));
                commonLog.setRequestNo(requestNo);
                commonLog.setUrl("asdfdsf");
                commonLog.setMethodName("123123");
                commonLog.setIp("127.0.0.1");
                commonLog.setRequestData("aaaaa");
                commonLog.setCreateTimestamp(System.currentTimeMillis());
                SendingCommonLog sendingCommonLog = new SendingCommonLog();
                BeanUtil.copyProperties(commonLog, sendingCommonLog);
                logProducerService.sendMsg(sendingCommonLog);
                Thread.sleep(1L);
            }
        }
    }

    @Test
    public void getCommonLogCount() {
        Long commonLogCount = commonLogMapper.getCommonLogCount();
        System.err.println(commonLogCount);
    }

    @Test
    public void getCommonLogList() {

        long current = System.currentTimeMillis();

        Long commonLogCount = commonLogMapper.getCommonLogCount();
        CommonLogParams commonLogCondition = new CommonLogParams();

        commonLogCondition.setGtValue(commonLogCount);
        commonLogCondition.setPageSize(100);
        commonLogCondition.setPageNo(1);

        List<CommonLog> commonLogList = commonLogMapper.getCommonLogList(commonLogCondition);

        PageResultPlus<CommonLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(commonLogList);
        pageResultPlus.setTotal(commonLogCount);
        pageResultPlus.setCurrent(1L);
        pageResultPlus.setSize(100L);

        long a = commonLogCount % 100 == 0 ? 0 : 1;
        pageResultPlus.setTotalPage((long) (a + commonLogCount / 100));

        System.err.println(pageResultPlus);
        System.out.println(System.currentTimeMillis() - current);
    }

    @Test
    public void getCommonLogListByCondition() {

        long current = System.currentTimeMillis();

        CommonLogCondition commonLogCondition = new CommonLogCondition();
        commonLogCondition.setLogLevel("ERROR");
        commonLogCondition.setPageSize(100);
        commonLogCondition.setLimitOffset(100000L);

        List<CommonLog> commonLogList = commonLogMapper.getCommonLogListByCondition(commonLogCondition);

        PageResultPlus<CommonLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(commonLogList);
        pageResultPlus.setCurrent(1L);
        pageResultPlus.setSize(100L);

        //pageResult.setTotalRows(commonLogCount);
        //long a = commonLogCount % 100 == 0 ? 0 : 1;
        //pageResult.setTotalPage((int) (a + commonLogCount / 100));

        //System.err.println(pageResult);
        System.out.println(System.currentTimeMillis() - current);

    }

    @Test
    public void getCommonLogListPageByCondition() {

        long current = System.currentTimeMillis();

        CommonLogCondition commonLogCondition = new CommonLogCondition();
        commonLogCondition.setLogLevel("ERROR");

        List<CommonLog> commonLogList = commonLogMapper.getCommonLogListPageByCondition(new Page<>(1000, 100),
                commonLogCondition);

        PageResultPlus<CommonLog> pageResultPlus = new PageResultPlus<>();
        pageResultPlus.setRows(commonLogList);
        pageResultPlus.setCurrent(1L);
        pageResultPlus.setSize(100L);

        //pageResult.setTotalRows(commonLogCount);
        //long a = commonLogCount % 100 == 0 ? 0 : 1;
        //pageResult.setTotalPage((int) (a + commonLogCount / 100));

        //System.err.println(pageResult);
        System.out.println(System.currentTimeMillis() - current);

    }
}

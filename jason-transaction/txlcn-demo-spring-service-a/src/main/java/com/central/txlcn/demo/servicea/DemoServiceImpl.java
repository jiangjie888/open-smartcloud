package com.central.txlcn.demo.servicea;

import com.central.txlcn.demo.common.db.domain.Demo;
import com.central.txlcn.demo.common.spring.ServiceBClient;
import com.central.txlcn.demo.common.spring.ServiceCClient;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
@Slf4j
public class DemoServiceImpl implements com.central.txlcn.demo.servicea.DemoService {
    @Resource
    private com.central.txlcn.demo.servicea.DemoMapper demoMapper;
    @Resource
    private ServiceBClient serviceBClient;
    @Resource
    private ServiceCClient serviceCClient;

    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String execute(String value, String exFlag, String flag) {
        /*Demo demo = new Demo();
        demo.setGroupId("abcdefg");
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName("service-a");
        demoMapper.save(demo);*/

        String dResp = serviceBClient.rpc(value);
        // step2. call remote ServiceB
        String eResp = serviceCClient.rpc(value);
        // step3. execute local transaction
        Demo demo = new Demo();
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(Transactions.getApplicationId());
        demoMapper.save(demo);


        // 置异常标志，DTX 回滚
        if (Objects.nonNull(exFlag)) {
            throw new IllegalStateException("by exFlag");
        }
        //return "ok-service-a";
        return dResp + " > " + eResp + " > " + "ok-service-a";
    }
}
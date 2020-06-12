package com.central.sharding.modular.service;

import base.BaseJunit;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.central.logger.entity.SendingCommonLog;
import com.central.sharding.modular.model.TOrder;
import io.shardingjdbc.core.api.HintManager;
import io.shardingjdbc.core.hint.HintManagerHolder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2020-06-11 18:11
 **/
public class TOrderServiceTest extends BaseJunit {

    @Autowired
    private ITOrderService torderService;

    public static Long orderId = 150L;

    @Test
    public void addOrder() throws InterruptedException {

        for (int i = 1; i <= 100; i++) {
            TOrder order = new TOrder();
            order.setOrderId(orderId);
            order.setUserId((long)i);
            order.setGoodsName("GoodsName" + i);
            order.setStatus(0);
            order.setCount(1);
            order.setSum(150.021);
            orderId++;
            if(i==3){
                HintManagerHolder.clear();
                HintManager hintManager = HintManager.getInstance();
                hintManager.addDatabaseShardingValue("t_order", "user_id", 3L);
                hintManager.addTableShardingValue("t_order", "order_id", 3L);
                System.out.println(orderId);
            }
            torderService.save(order);

        }
    }
}

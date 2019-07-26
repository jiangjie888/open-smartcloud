package com.central.demo.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.central.core.model.exception.RequestEmptyException;
import com.central.core.model.exception.ServiceException;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.utils.RandomUtil;
import com.central.core.utils.ToolUtil;
import com.central.demo.api.order.enums.OrderStatusEnum;
import com.central.demo.api.order.model.GoodsOrder;
import com.central.demo.order.consumer.MessageServiceConsumer;
import com.central.demo.order.core.exception.OrderExceptionEnum;
import com.central.demo.order.dao.OrderDao;
import com.central.demo.order.service.IOrderService;
import com.central.message.api.enums.MessageQueueEnum;
import com.central.message.api.model.ReliableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MessageServiceConsumer messageServiceConsumer;

    @Override
    public Long makeTestOrder() {

        //创建预发送消息（未完成的订单）
        GoodsOrder goods = createGoods();

        //下单
        orderDao.save(goods);

        //返回订单号
        return goods.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishOrder(Long orderId) {

        if (ToolUtil.isEmpty(orderId)) {
            throw new RequestEmptyException();
        }

        GoodsOrder order = this.findById(orderId);

        if (order == null) {
            throw new ServiceException(OrderExceptionEnum.ORDER_NULL);
        }

        if (OrderStatusEnum.SUCCESS.getStatus().equals(order.getStatus())) {
            return;
        }

        //创建预发送消息
        ReliableMessage reliableMessage = createMessage(order);

        //预发送消息（只是保存到数据库表中）
        ReliableMessage resutlMessage = messageServiceConsumer.preSaveMessage(reliableMessage);

        //更新订单为成功状态(百分之50几率失败，模拟错误数据)（此处错误已添加到消息表的数据会被roses-message-checker轮询时删除掉）
        //updateToSuccess(order);

        //确认消息（发送中）
        messageServiceConsumer.confirmAndSendMessage(reliableMessage.getMessageId());


    }

    private ReliableMessage createMessage(GoodsOrder goodsOrder) {
        String messageId = IdWorker.getIdStr();
        String messageBody = JSON.toJSONString(goodsOrder);
        String queue = MessageQueueEnum.MAKE_ORDER.name();
        ReliableMessage reliableMessage = new ReliableMessage(messageId, messageBody, queue);
        reliableMessage.setBizUniqueId(goodsOrder.getId());
        return reliableMessage;
    }

    private GoodsOrder createGoods() {
        GoodsOrder goodsOrder = new GoodsOrder();
        //goodsOrder.setId(IdWorker.getId());
        goodsOrder.setGoodsName("商品" + RandomUtil.randomNumbers(4));
        goodsOrder.setCount(Integer.valueOf(RandomUtil.randomNumbers(1)));

        goodsOrder.setSum(new BigDecimal(RandomUtil.randomDouble(10.0, 50.0)).setScale(2, RoundingMode.HALF_UP));
        goodsOrder.setStatus(OrderStatusEnum.NOT_SUCCESS.getStatus());  //未完成的订单
        goodsOrder.setUserId(IdWorker.getId());
        goodsOrder.setCreatorUserId(IdWorker.getId());
        goodsOrder.setCreationTime(new Date());

        return goodsOrder;
    }

    private void updateToSuccess(GoodsOrder order) {
        order.setStatus(OrderStatusEnum.SUCCESS.getStatus());
        orderDao.update(order);

        int random = RandomUtil.randomInt(100);
        if (random > 50) {
            return;
        } else {
            throw new ServiceException(OrderExceptionEnum.ORDER_ERROR);
        }
    }

    public GoodsOrder findById(Long id)
    {
        return orderDao.findById(id);
    }
}

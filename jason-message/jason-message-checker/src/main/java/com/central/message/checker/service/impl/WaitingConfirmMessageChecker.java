package com.central.message.checker.service.impl;

import com.central.core.model.page.PageResult;
import com.central.demo.api.order.enums.OrderStatusEnum;
import com.central.demo.api.order.model.GoodsOrder;
import com.central.message.api.model.ReliableMessage;
import com.central.message.checker.consumer.GoodsOrderConsumer;
import com.central.message.checker.consumer.MessageServiceConsumer;
import com.central.message.checker.service.AbstractMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 处理状态为“待确认”但已超时的消息
 */
@Service
public class WaitingConfirmMessageChecker extends AbstractMessageChecker {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageServiceConsumer messageServiceConsumer;

    @Autowired
    private GoodsOrderConsumer goodsOrderConsumer;

    @Override
    protected void processMessage(Map<String, ReliableMessage> messages) {
        for (Map.Entry<String, ReliableMessage> entry : messages.entrySet()) {
            ReliableMessage message = entry.getValue();
            try {

                Long orderId = message.getBizUniqueId();

                if (orderId == null) {

                    //如果订单失败，则删掉没用的消息
                    messageServiceConsumer.deleteMessageByMessageId(message.getMessageId());
                } else {
                    GoodsOrder order = goodsOrderConsumer.findOrderById(orderId);

                    //如果订单成功，则确认消息并发送
                    if (order != null && OrderStatusEnum.SUCCESS.equals(order.getStatus())) {
                        messageServiceConsumer.confirmAndSendMessage(message.getMessageId());
                    } else {

                        //如果订单失败，则删掉没用的消息
                        messageServiceConsumer.deleteMessageByMessageId(message.getMessageId());
                    }
                }

            } catch (Exception e) {
                logger.error("处理待确认消息异常！messageId=" + message.getMessageId(), e);
            }
        }
    }

    @Override
    protected PageResult<ReliableMessage> getPageResult(Map<String, Object> params) {
        return messageServiceConsumer.listPagetWaitConfimTimeOutMessages(params);
    }

}

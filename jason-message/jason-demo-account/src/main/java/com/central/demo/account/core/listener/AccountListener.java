package com.central.demo.account.core.listener;

import com.alibaba.fastjson.JSON;
import com.central.core.model.exception.ServiceException;
import com.central.core.utils.ToolUtil;
import com.central.demo.account.service.IFlowRecordService;
import com.central.demo.api.order.GoodsFlowParam;
import com.central.message.api.enums.MessageQueueEnum;
import com.central.message.api.exception.MessageExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class AccountListener implements MessageListener {

    //private static   String  destination = MessageQueueEnum.MAKE_ORDER.name();

    @Autowired
    private IFlowRecordService flowRecordService;

    @Override
    //@JmsListener(destination = JmsConfig.TOPIC,containerFactory = "jmsListenerContainerTopic")
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                String messageBody = ((TextMessage) message).getText();

                if (ToolUtil.isEmpty(messageBody)) {
                    throw new ServiceException(MessageExceptionEnum.MESSAGE_BODY_CANT_EMPTY);
                }

                GoodsFlowParam goodsFlowParam = JSON.parseObject(messageBody, GoodsFlowParam.class);
                flowRecordService.recordFlow(goodsFlowParam);

            } catch (JMSException ex) {
                throw new ServiceException(MessageExceptionEnum.MESSAGE_QUEUE_ERROR);
            }

        } else {
            throw new ServiceException(MessageExceptionEnum.MESSAGE_TYPE_ERROR);
        }
    }

}
package com.central.message.core.activemq.impl;

import com.central.core.exception.RequestEmptyException;
import com.central.core.utils.ToolUtil;
import com.central.message.api.model.ReliableMessage;
import com.central.message.core.activemq.MessageSender;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

/**
 * activemq实现的消息发送器
 */
public class ActiveMqMessageSender implements MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    public void sendMessage(ReliableMessage reliableMessage) {

        if (reliableMessage == null ||
                ToolUtil.isOneEmpty(reliableMessage.getConsumerQueue(), reliableMessage.getMessageBody())) {
            throw new RequestEmptyException();
        }

        Destination destination = new ActiveMQQueue(reliableMessage.getConsumerQueue());
        //jmsTemplate.convertAndSend(reliableMessage.getConsumerQueue(),reliableMessage.getMessageBody());
        //jmsMessagingTemplate.convertAndSend(destination, reliableMessage.getMessageBody());

        jmsTemplate.setDefaultDestinationName(reliableMessage.getConsumerQueue());

        //设置ack确认为client方式
        jmsTemplate.setSessionAcknowledgeMode(JmsProperties.AcknowledgeMode.CLIENT.getMode());

        //发送消息
        jmsTemplate.send(session -> session.createTextMessage(reliableMessage.getMessageBody()));
    }


    /**
     * @desc 延时发送
     */
    /* public void delaySend(String text, String queueName, Long time) {
        //获取连接工厂
        ConnectionFactory connectionFactory = this.jmsMessagingTemplate.getConnectionFactory();
        try {
            //获取连接
            Connection connection = connectionFactory.createConnection();
            connection.start();
            //获取session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建一个消息队列
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage message = session.createTextMessage(text);
            //设置延迟时间
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
            //发送
            producer.send(message);
            session.commit();
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }*/
}

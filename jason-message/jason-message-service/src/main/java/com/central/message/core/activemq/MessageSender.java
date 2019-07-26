package com.central.message.core.activemq;

import com.central.message.api.model.ReliableMessage;

/**
 * mq消息队列的发送者（若想拓展mq，则可实现此接口，并在configuration中配置即可）
 *
 */
public interface MessageSender {

    /**
     * 发送消息
     */
    void sendMessage(ReliableMessage reliableMessage);
}

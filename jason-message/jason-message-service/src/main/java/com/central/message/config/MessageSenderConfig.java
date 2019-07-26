package com.central.message.config;

import com.central.message.core.activemq.MessageSender;
import com.central.message.core.activemq.impl.ActiveMqMessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息发送者配置
 */
@Configuration
public class MessageSenderConfig {

    @Bean
    public MessageSender messageSender() {
        return new ActiveMqMessageSender();
    }
}

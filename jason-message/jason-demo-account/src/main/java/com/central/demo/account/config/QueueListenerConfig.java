package com.central.demo.account.config;

import com.central.demo.account.core.listener.AccountListener;
import com.central.message.api.enums.MessageQueueEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import javax.jms.ConnectionFactory;

/**
 * 消息队列监听配置
 */
@Configuration
public class QueueListenerConfig {

    @Autowired
    private AccountListener accountListener;

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer factory = new DefaultMessageListenerContainer();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationName(MessageQueueEnum.MAKE_ORDER.name());
        //设置连接数
        //factory.setConcurrency("1-10");
        //重连间隔时间
        //factory.setRecoveryInterval(1000L);
        factory.setMessageListener(accountListener);
        return factory;
    }
}

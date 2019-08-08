package com.central.oauth.modular.consumer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import feign.Request;
import feign.Request.Options;

import static com.sun.xml.internal.ws.api.message.Packet.Status.Request;

public class ServiceFeignConfiguration {
    @Value("${ribbon.ConnectTimeout:60000}")
    private int connectTimeout;

    @Value("${ribbon.ReadTimeout:60000}")
    private int readTimeout;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }
}

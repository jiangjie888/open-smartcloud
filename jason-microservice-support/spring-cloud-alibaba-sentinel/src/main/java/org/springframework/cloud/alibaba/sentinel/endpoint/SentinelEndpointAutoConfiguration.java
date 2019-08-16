//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.endpoint;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.alibaba.sentinel.SentinelProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({Endpoint.class})
@EnableConfigurationProperties({SentinelProperties.class})
public class SentinelEndpointAutoConfiguration {
    public SentinelEndpointAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint
    public SentinelEndpoint sentinelEndPoint(SentinelProperties sentinelProperties) {
        return new SentinelEndpoint(sentinelProperties);
    }
}

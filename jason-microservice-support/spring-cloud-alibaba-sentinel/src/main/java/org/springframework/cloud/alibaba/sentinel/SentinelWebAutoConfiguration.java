//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(
        name = {"spring.cloud.sentinel.enabled"},
        matchIfMissing = true
)
@EnableConfigurationProperties({SentinelProperties.class})
public class SentinelWebAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SentinelWebAutoConfiguration.class);
    @Autowired
    private SentinelProperties properties;

    public SentinelWebAutoConfiguration() {
    }

    @Bean
    @ConditionalOnProperty(
            name = {"spring.cloud.sentinel.filter.enabled"},
            matchIfMissing = true
    )
    public FilterRegistrationBean sentinelFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean();
        org.springframework.cloud.alibaba.sentinel.SentinelProperties.Filter filterConfig = this.properties.getFilter();
        if (filterConfig.getUrlPatterns() == null || filterConfig.getUrlPatterns().isEmpty()) {
            List<String> defaultPatterns = new ArrayList();
            defaultPatterns.add("/*");
            filterConfig.setUrlPatterns(defaultPatterns);
        }

        registration.addUrlPatterns((String[])filterConfig.getUrlPatterns().toArray(new String[0]));
        Filter filter = new CommonFilter();
        registration.setFilter(filter);
        registration.setOrder(filterConfig.getOrder());
        log.info("[Sentinel Starter] register Sentinel with urlPatterns: {}.", filterConfig.getUrlPatterns());
        return registration;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.custom;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.adapter.servlet.config.WebServletConfig;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.alibaba.sentinel.SentinelProperties;
import org.springframework.cloud.alibaba.sentinel.datasource.converter.JsonConverter;
import org.springframework.cloud.alibaba.sentinel.datasource.converter.XmlConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(
        name = {"spring.cloud.sentinel.enabled"},
        matchIfMissing = true
)
@EnableConfigurationProperties({SentinelProperties.class})
public class SentinelAutoConfiguration {
    @Value("${project.name:${spring.application.name:}}")
    private String projectName;
    @Autowired
    private SentinelProperties properties;
    @Autowired
    private Optional<UrlCleaner> urlCleanerOptional;
    @Autowired
    private Optional<UrlBlockHandler> urlBlockHandlerOptional;
    @Autowired
    private Optional<RequestOriginParser> requestOriginParserOptional;

    public SentinelAutoConfiguration() {
    }

    @PostConstruct
    private void init() {
        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.log.dir")) && StringUtils.hasText(this.properties.getLog().getDir())) {
            System.setProperty("csp.sentinel.log.dir", this.properties.getLog().getDir());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.log.use.pid")) && this.properties.getLog().isSwitchPid()) {
            System.setProperty("csp.sentinel.log.use.pid", String.valueOf(this.properties.getLog().isSwitchPid()));
        }

        if (StringUtils.isEmpty(System.getProperty("project.name")) && StringUtils.hasText(this.projectName)) {
            System.setProperty("project.name", this.projectName);
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.api.port")) && StringUtils.hasText(this.properties.getTransport().getPort())) {
            System.setProperty("csp.sentinel.api.port", this.properties.getTransport().getPort());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.dashboard.server")) && StringUtils.hasText(this.properties.getTransport().getDashboard())) {
            System.setProperty("csp.sentinel.dashboard.server", this.properties.getTransport().getDashboard());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.heartbeat.interval.ms")) && StringUtils.hasText(this.properties.getTransport().getHeartbeatIntervalMs())) {
            System.setProperty("csp.sentinel.heartbeat.interval.ms", this.properties.getTransport().getHeartbeatIntervalMs());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.heartbeat.client.ip")) && StringUtils.hasText(this.properties.getTransport().getClientIp())) {
            System.setProperty("csp.sentinel.heartbeat.client.ip", this.properties.getTransport().getClientIp());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.charset")) && StringUtils.hasText(this.properties.getMetric().getCharset())) {
            System.setProperty("csp.sentinel.charset", this.properties.getMetric().getCharset());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.metric.file.single.size")) && StringUtils.hasText(this.properties.getMetric().getFileSingleSize())) {
            System.setProperty("csp.sentinel.metric.file.single.size", this.properties.getMetric().getFileSingleSize());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.metric.file.total.count")) && StringUtils.hasText(this.properties.getMetric().getFileTotalCount())) {
            System.setProperty("csp.sentinel.metric.file.total.count", this.properties.getMetric().getFileTotalCount());
        }

        if (StringUtils.isEmpty(System.getProperty("csp.sentinel.flow.cold.factor")) && StringUtils.hasText(this.properties.getFlow().getColdFactor())) {
            System.setProperty("csp.sentinel.flow.cold.factor", this.properties.getFlow().getColdFactor());
        }

        if (StringUtils.hasText(this.properties.getServlet().getBlockPage())) {
            WebServletConfig.setBlockPage(this.properties.getServlet().getBlockPage());
        }

        this.urlBlockHandlerOptional.ifPresent(WebCallbackManager::setUrlBlockHandler);
        this.urlCleanerOptional.ifPresent(WebCallbackManager::setUrlCleaner);
        this.requestOriginParserOptional.ifPresent(WebCallbackManager::setRequestOriginParser);
        if (this.properties.isEager()) {
            InitExecutor.doInit();
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(
            name = {"org.springframework.web.client.RestTemplate"}
    )
    @ConditionalOnProperty(
            name = {"resttemplate.sentinel.enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    public SentinelBeanPostProcessor sentinelBeanPostProcessor(ApplicationContext applicationContext) {
        return new SentinelBeanPostProcessor(applicationContext);
    }

    @Bean
    public SentinelDataSourceHandler sentinelDataSourceHandler(DefaultListableBeanFactory beanFactory) {
        return new SentinelDataSourceHandler(beanFactory);
    }

    @ConditionalOnClass({XmlMapper.class})
    protected static class SentinelXmlConfiguration {
        private XmlMapper xmlMapper = new XmlMapper();

        protected SentinelXmlConfiguration() {
        }

        @Bean({"sentinel-xml-flow-converter"})
        public XmlConverter xmlFlowConverter() {
            return new XmlConverter(this.xmlMapper, FlowRule.class);
        }

        @Bean({"sentinel-xml-degrade-converter"})
        public XmlConverter xmlDegradeConverter() {
            return new XmlConverter(this.xmlMapper, DegradeRule.class);
        }

        @Bean({"sentinel-xml-system-converter"})
        public XmlConverter xmlSystemConverter() {
            return new XmlConverter(this.xmlMapper, SystemRule.class);
        }

        @Bean({"sentinel-xml-authority-converter"})
        public XmlConverter xmlAuthorityConverter() {
            return new XmlConverter(this.xmlMapper, AuthorityRule.class);
        }

        @Bean({"sentinel-xml-param-flow-converter"})
        public XmlConverter xmlParamFlowConverter() {
            return new XmlConverter(this.xmlMapper, ParamFlowRule.class);
        }
    }

    @ConditionalOnClass({ObjectMapper.class})
    protected static class SentinelConverterConfiguration {
        private ObjectMapper objectMapper = new ObjectMapper();

        protected SentinelConverterConfiguration() {
        }

        @Bean({"sentinel-json-flow-converter"})
        public JsonConverter jsonFlowConverter() {
            return new JsonConverter(this.objectMapper, FlowRule.class);
        }

        @Bean({"sentinel-json-degrade-converter"})
        public JsonConverter jsonDegradeConverter() {
            return new JsonConverter(this.objectMapper, DegradeRule.class);
        }

        @Bean({"sentinel-json-system-converter"})
        public JsonConverter jsonSystemConverter() {
            return new JsonConverter(this.objectMapper, SystemRule.class);
        }

        @Bean({"sentinel-json-authority-converter"})
        public JsonConverter jsonAuthorityConverter() {
            return new JsonConverter(this.objectMapper, AuthorityRule.class);
        }

        @Bean({"sentinel-json-param-flow-converter"})
        public JsonConverter jsonParamFlowConverter() {
            return new JsonConverter(this.objectMapper, ParamFlowRule.class);
        }
    }
}

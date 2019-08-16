//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.endpoint;

import com.alibaba.csp.sentinel.adapter.servlet.config.WebServletConfig;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.log.LogBase;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.alibaba.sentinel.SentinelProperties;

@Endpoint(
        id = "sentinel"
)
public class SentinelEndpoint {
    private final SentinelProperties sentinelProperties;

    public SentinelEndpoint(SentinelProperties sentinelProperties) {
        this.sentinelProperties = sentinelProperties;
    }

    @ReadOperation
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap();
        if (this.sentinelProperties.isEnabled()) {
            result.put("appName", AppNameUtil.getAppName());
            result.put("logDir", LogBase.getLogBaseDir());
            result.put("logUsePid", LogBase.isLogNameUsePid());
            result.put("blockPage", WebServletConfig.getBlockPage());
            result.put("metricsFileSize", SentinelConfig.singleMetricFileSize());
            result.put("metricsFileCharset", SentinelConfig.charset());
            result.put("totalMetricsFileCount", SentinelConfig.totalMetricFileCount());
            result.put("consoleServer", TransportConfig.getConsoleServer());
            result.put("clientIp", TransportConfig.getHeartbeatClientIp());
            result.put("heartbeatIntervalMs", TransportConfig.getHeartbeatIntervalMs());
            result.put("clientPort", TransportConfig.getPort());
            result.put("coldFactor", this.sentinelProperties.getFlow().getColdFactor());
            result.put("filter", this.sentinelProperties.getFilter());
            result.put("datasource", this.sentinelProperties.getDatasource());
            Map<String, Object> rules = new HashMap();
            result.put("rules", rules);
            rules.put("flowRules", FlowRuleManager.getRules());
            rules.put("degradeRules", DegradeRuleManager.getRules());
            rules.put("systemRules", SystemRuleManager.getRules());
            rules.put("authorityRule", AuthorityRuleManager.getRules());
            rules.put("paramFlowRule", ParamFlowRuleManager.getRules());
        }

        return result;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.alibaba.sentinel.datasource.config.DataSourcePropertiesConfiguration;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(
        prefix = "spring.cloud.sentinel"
)
@Validated
public class SentinelProperties {
    private boolean eager = false;
    private boolean enabled = true;
    private Map<String, DataSourcePropertiesConfiguration> datasource;
    private SentinelProperties.Transport transport;
    private SentinelProperties.Metric metric;
    private SentinelProperties.Servlet servlet;
    private SentinelProperties.Filter filter;
    private SentinelProperties.Flow flow;
    private SentinelProperties.Log log;

    public SentinelProperties() {
        this.datasource = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.transport = new SentinelProperties.Transport();
        this.metric = new SentinelProperties.Metric();
        this.servlet = new SentinelProperties.Servlet();
        this.filter = new SentinelProperties.Filter();
        this.flow = new SentinelProperties.Flow();
        this.log = new SentinelProperties.Log();
    }

    public boolean isEager() {
        return this.eager;
    }

    public void setEager(boolean eager) {
        this.eager = eager;
    }

    public SentinelProperties.Flow getFlow() {
        return this.flow;
    }

    public void setFlow(SentinelProperties.Flow flow) {
        this.flow = flow;
    }

    public SentinelProperties.Transport getTransport() {
        return this.transport;
    }

    public void setTransport(SentinelProperties.Transport transport) {
        this.transport = transport;
    }

    public SentinelProperties.Metric getMetric() {
        return this.metric;
    }

    public void setMetric(SentinelProperties.Metric metric) {
        this.metric = metric;
    }

    public SentinelProperties.Servlet getServlet() {
        return this.servlet;
    }

    public void setServlet(SentinelProperties.Servlet servlet) {
        this.servlet = servlet;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SentinelProperties.Filter getFilter() {
        return this.filter;
    }

    public void setFilter(SentinelProperties.Filter filter) {
        this.filter = filter;
    }

    public Map<String, DataSourcePropertiesConfiguration> getDatasource() {
        return this.datasource;
    }

    public void setDatasource(Map<String, DataSourcePropertiesConfiguration> datasource) {
        this.datasource = datasource;
    }

    public SentinelProperties.Log getLog() {
        return this.log;
    }

    public void setLog(SentinelProperties.Log log) {
        this.log = log;
    }

    public static class Log {
        private String dir;
        private boolean switchPid = false;

        public Log() {
        }

        public String getDir() {
            return this.dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public boolean isSwitchPid() {
            return this.switchPid;
        }

        public void setSwitchPid(boolean switchPid) {
            this.switchPid = switchPid;
        }
    }

    public static class Filter {
        private int order = -2147483648;
        private List<String> urlPatterns;
        private boolean enabled = true;

        public Filter() {
        }

        public int getOrder() {
            return this.order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public List<String> getUrlPatterns() {
            return this.urlPatterns;
        }

        public void setUrlPatterns(List<String> urlPatterns) {
            this.urlPatterns = urlPatterns;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Transport {
        private String port = "8719";
        private String dashboard = "";
        private String heartbeatIntervalMs;
        private String clientIp;

        public Transport() {
        }

        public String getHeartbeatIntervalMs() {
            return this.heartbeatIntervalMs;
        }

        public void setHeartbeatIntervalMs(String heartbeatIntervalMs) {
            this.heartbeatIntervalMs = heartbeatIntervalMs;
        }

        public String getPort() {
            return this.port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getDashboard() {
            return this.dashboard;
        }

        public void setDashboard(String dashboard) {
            this.dashboard = dashboard;
        }

        public String getClientIp() {
            return this.clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }
    }

    public static class Metric {
        private String fileSingleSize;
        private String fileTotalCount;
        private String charset = "UTF-8";

        public Metric() {
        }

        public String getFileSingleSize() {
            return this.fileSingleSize;
        }

        public void setFileSingleSize(String fileSingleSize) {
            this.fileSingleSize = fileSingleSize;
        }

        public String getFileTotalCount() {
            return this.fileTotalCount;
        }

        public void setFileTotalCount(String fileTotalCount) {
            this.fileTotalCount = fileTotalCount;
        }

        public String getCharset() {
            return this.charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }
    }

    public static class Servlet {
        private String blockPage;

        public Servlet() {
        }

        public String getBlockPage() {
            return this.blockPage;
        }

        public void setBlockPage(String blockPage) {
            this.blockPage = blockPage;
        }
    }

    public static class Flow {
        private String coldFactor = "3";

        public Flow() {
        }

        public String getColdFactor() {
            return this.coldFactor;
        }

        public void setColdFactor(String coldFactor) {
            this.coldFactor = coldFactor;
        }
    }
}

package com.central.search.core.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.elasticsearch")
public class ElasticSearchProperties {
    private String clusterName;
    private String clusterNodes;
    private XPack xpack;
    private Map<String, String> properties = new HashMap<String, String>();

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public XPack getXpack() {
        return xpack;
    }

    public void setXpack(XPack xpack) {
        this.xpack = xpack;
    }
}
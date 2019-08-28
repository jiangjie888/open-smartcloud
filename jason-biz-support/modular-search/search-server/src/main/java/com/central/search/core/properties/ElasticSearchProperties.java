package com.central.search.core.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.elasticsearch")
public class ElasticSearchProperties {
    private String clusterName;
    private String clusterNodes;
    private XPack xpack;

    private int connectTimeOut = 1000; // 连接超时时间
    private int socketTimeOut = 30000; // 连接超时时间
    private int connectionRequestTimeOut = 500; // 获取连接的超时时间
    private int maxConnectNum = 100; // 最大连接数
    private int maxConnectPerRoute = 100; // 最大路由连接数


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

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public int getConnectionRequestTimeOut() {
        return connectionRequestTimeOut;
    }

    public void setConnectionRequestTimeOut(int connectionRequestTimeOut) {
        this.connectionRequestTimeOut = connectionRequestTimeOut;
    }

    public int getMaxConnectNum() {
        return maxConnectNum;
    }

    public void setMaxConnectNum(int maxConnectNum) {
        this.maxConnectNum = maxConnectNum;
    }

    public int getMaxConnectPerRoute() {
        return maxConnectPerRoute;
    }

    public void setMaxConnectPerRoute(int maxConnectPerRoute) {
        this.maxConnectPerRoute = maxConnectPerRoute;
    }

}
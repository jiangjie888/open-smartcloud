package org.springframework.cloud.alibaba.sentinel;

public interface SentinelConstants {
    String PROPERTY_PREFIX = "spring.cloud.sentinel";
    String BLOCK_TYPE = "block";
    String FALLBACK_TYPE = "fallback";
    String FLOW_DATASOURCE_NAME = "edas-flow";
    String DEGRADE_DATASOURCE_NAME = "edas-degrade";
}

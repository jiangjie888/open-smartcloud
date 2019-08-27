package com.central.search.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import cn.hutool.core.util.RandomUtil;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticSearchAutoConfig /*implements DisposableBean*/ {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutoConfig.class);
    private final ElasticsearchProperties properties;

    public ElasticSearchAutoConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TransportClient transportClient(){
        return new PreBuiltXPackTransportClient(settings()).addTransportAddresses(addresses());
    }
    /**
     * .put("client.transport.sniff", true)
     * .put("client.transport.ignore_cluster_name", false)
     * .put("client.transport.ping_timeout", clientPingTimeout)
     * .put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)
     *
     * @return Settings
     */
    private Settings settings() {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", properties.getClusterName());
        properties.getProperties().forEach(builder::put);
        return builder.build();
    }

    private TransportAddress[] addresses() {
        String clusterNodesStr = properties.getClusterNodes();
        Assert.hasText(clusterNodesStr, "Cluster nodes source must not be null or empty!");
        String[] nodes = StringUtils.delimitedListToStringArray(clusterNodesStr, ",");

        return Arrays.stream(nodes).map(node -> {
            String[] segments = StringUtils.delimitedListToStringArray(node, ":");
            Assert.isTrue(segments.length == 2,
                    () -> String.format("Invalid cluster node %s in %s! Must be in the format host:port!", node, clusterNodesStr));
            String host = segments[0].trim();
            String port = segments[1].trim();
            Assert.hasText(host, () -> String.format("No host name given cluster node %s!", node));
            Assert.hasText(port, () -> String.format("No port given in cluster node %s!", node));
            return new TransportAddress(toInetAddress(host), Integer.valueOf(port));
        }).toArray(TransportAddress[]::new);
    }

    private static InetAddress toInetAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    /*@Autowired
    private ElasticSearchProperties properties;

    private Releasable releasable;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        if (isNotBlank(properties.getClusterName()) && isNotBlank(properties.getClusterNodes())) {
            Builder builder = Settings.builder();
            builder.put("cluster.name", properties.getClusterName());
            // 创建client
            String[] arr = properties.getClusterNodes().split(",");
            TransportClient client = null;
            String connectionUrl = RandomUtil.randomEle(arr);

            String[] hostportarr = connectionUrl.split(":");
            String host = hostportarr[0];
            Integer port = Integer.valueOf(hostportarr[1]);
            if (properties.getXpack() != null && isNotBlank(properties.getXpack().getUser()) && isNotBlank(properties.getXpack().getPassword())) {
                builder.put("xpack.security.user", properties.getXpack().getUser() + ":" + properties.getXpack().getPassword());
                client = new PreBuiltXPackTransportClient(builder.build()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            } else {
                client = new PreBuiltTransportClient(builder.build()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            }

            releasable = client;
            return client;
        }
        return null;
    }

    private boolean isNotBlank(String clusterName) {
        return clusterName != null && clusterName.length() > 0;
    }

    @Override
    public void destroy() throws Exception {
        if (this.releasable != null) {
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Closing Elasticsearch client");
                }
                try {
                    this.releasable.close();
                } catch (NoSuchMethodError ex) {
                    // Earlier versions of Elasticsearch had a different method
                    // name
                    ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(Releasable.class, "release"), this.releasable);
                }
            } catch (final Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error closing Elasticsearch client: ", ex);
                }
            }
        }
    }*/
}
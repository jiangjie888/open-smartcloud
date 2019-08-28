package com.central.search.config;

import java.util.ArrayList;
import com.central.search.core.properties.ElasticSearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;


@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchAutoConfig /*implements DisposableBean*/ {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutoConfig.class);
    private final ElasticSearchProperties properties;
    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";
    private ArrayList<HttpHost> nodes = null;


    public ElasticSearchAutoConfig(ElasticSearchProperties properties) {
        this.properties = properties;
    }

    public ArrayList<HttpHost> getNodes(){
        nodes = new ArrayList<>();
        String clusterNodesStr = properties.getClusterNodes();
        Assert.hasText(clusterNodesStr, "Cluster nodes source must not be null or empty!");
        String[] hostStrs = StringUtils.delimitedListToStringArray(clusterNodesStr, ",");
        for (String host : hostStrs) {
            //nodes.add(new HttpHost(host, port, schema));
            nodes.add(HttpHost.create(host));
        }
        return nodes;
    }

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {

        /** 用户认证对象 */
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        /** 设置账号密码 */
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(properties.getXpack().getUser(), properties.getXpack().getPassword()));
        /** 创建rest client对象 */
        RestClientBuilder builder = RestClient.builder(getNodes().toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(new RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(properties.getConnectTimeOut());
                requestConfigBuilder.setSocketTimeout(properties.getSocketTimeOut());
                requestConfigBuilder.setConnectionRequestTimeout(properties.getConnectionRequestTimeOut());
                return requestConfigBuilder;
            }
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(properties.getMaxConnectNum());
                httpClientBuilder.setMaxConnPerRoute(properties.getMaxConnectPerRoute());
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder;
            }
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
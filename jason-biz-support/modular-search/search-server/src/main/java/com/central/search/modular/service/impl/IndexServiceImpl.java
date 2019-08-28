package com.central.search.modular.service.impl;

import cn.hutool.core.util.StrUtil;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.central.core.exception.ServiceException;
import com.central.core.model.page.PageResult;
import com.central.search.modular.controller.TestController;
import com.central.search.modular.dto.IndexDto;
import com.central.search.modular.model.IndexVo;
import com.central.search.modular.service.IIndexService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.index.shard.DocsStats;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 索引
 */
@Slf4j
@Service
public class IndexServiceImpl implements IIndexService {

    //private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Override
    public void create(IndexDto indexDto) {
            // setting
            Settings settings = Settings.builder()
                    .put("index.number_of_shards", indexDto.getNumberOfShards())
                    .put("index.number_of_replicas", indexDto.getNumberOfReplicas())
                    .build();
            CreateIndexRequest request = new CreateIndexRequest(indexDto.getIndexName());//创建索引
            //创建的每个索引都可以有与之关联的特定设置。
            request.settings(settings);

            if (StrUtil.isNotEmpty(indexDto.getMappingsSource())) {
                //mappings
                //创建索引时创建文档类型映射
            /*request.mapping("tweet",//类型定义
                    "  {\n" +
                            "    \"tweet\": {\n" +
                            "      \"properties\": {\n" +
                            "        \"message\": {\n" +
                            "          \"type\": \"text\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }",//类型映射，需要的是一个JSON字符串
                    XContentType.JSON);*/
                request.mapping(indexDto.getMappingsSource(), XContentType.JSON);
            }
            //为索引设置一个别名
            //request.alias(new Alias("twitter_alias"));
            //可选参数
            //request.timeout(TimeValue.timeValueMinutes(2));//超时,等待所有节点被确认(使用TimeValue方式)
            //request.timeout("2m");//超时,等待所有节点被确认(使用字符串方式)

            //request.masterNodeTimeout(TimeValue.timeValueMinutes(1));//连接master节点的超时时间(使用TimeValue方式)
            //request.masterNodeTimeout("1m");//连接master节点的超时时间(使用字符串方式)

            //request.waitForActiveShards(2);//在创建索引API返回响应之前等待的活动分片副本的数量，以int形式表示。
            //request.waitForActiveShards(ActiveShardCount.DEFAULT);//在创建索引API返回响应之前等待的活动分片副本的数量，以ActiveShardCount形式表示。
            //同步执行
            try{
                CreateIndexResponse createIndexResponse = highLevelClient.indices().create(request, RequestOptions.DEFAULT);
                //异步执行
                //异步执行创建索引请求需要将CreateIndexRequest实例和ActionListener实例传递给异步方法：
                //CreateIndexResponse的典型监听器如下所示：
                //异步方法不会阻塞并立即返回。
                /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        //如果执行成功，则调用onResponse方法;
                    }

                    @Override
                    public void onFailure(Exception e) {
                        //如果失败，则调用onFailure方法。
                    }
                };
                highLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, listener);//要执行的CreateIndexRequest和执行完成时要使用的ActionListener
                */
                //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
                boolean acknowledged = createIndexResponse.isAcknowledged();//指示是否所有节点都已确认请求
                boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();//指示是否在超时之前为索引中的每个分片启动了必需的分片副本数
            } catch (IOException e) {
                e.printStackTrace();
                log.error("创建索引异常："+e.getMessage());
            }
    }

    @Override
    public void delete(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);//指定要删除的索引名称
        //可选参数：
        //request.timeout(TimeValue.timeValueMinutes(2)); //设置超时，等待所有节点确认索引删除（使用TimeValue形式）
        // request.timeout("2m"); //设置超时，等待所有节点确认索引删除（使用字符串形式）

        //request.masterNodeTimeout(TimeValue.timeValueMinutes(1));////连接master节点的超时时间(使用TimeValue方式)
        // request.masterNodeTimeout("1m");//连接master节点的超时时间(使用字符串方式)

        //设置IndicesOptions控制如何解决不可用的索引以及如何扩展通配符表达式
        //request.indicesOptions(IndicesOptions.lenientExpandOpen());
        try {
        //同步执行
        AcknowledgedResponse acknowledgedResponse = highLevelClient.indices().delete(request);
        /*//异步执行删除索引请求需要将DeleteIndexRequest实例和ActionListener实例传递给异步方法：
        //DeleteIndexResponse的典型监听器如下所示：
        //异步方法不会阻塞并立即返回。
        ActionListener<DeleteIndexResponse> listener = new ActionListener<DeleteIndexResponse>() {
            @Override
            public void onResponse(DeleteIndexResponse deleteIndexResponse) {
                //如果执行成功，则调用onResponse方法;
            }

            @Override
            public void onFailure(Exception e) {
                //如果失败，则调用onFailure方法。
            }
        };
        client.indices().deleteAsync(request, listener);*/

        //Delete Index Response
        //返回的DeleteIndexResponse允许检索有关执行的操作的信息，如下所示：
        boolean acknowledged = acknowledgedResponse.isAcknowledged();//是否所有节点都已确认请求


        //如果找不到索引，则会抛出ElasticsearchException：
        /*try {
            request = new DeleteIndexRequest("does_not_exist");
            highLevelClient.indices().delete(request);
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                //如果没有找到要删除的索引，要执行某些操作
            }
        }*/
        } catch (IOException e) {
            e.printStackTrace();
            log.error("删除索引异常："+e.getMessage());
        }
    }

    @Override
    public PageResult<IndexVo> list(String queryStr, String... indices) {
        List<IndexVo> indexList = new ArrayList<>();
        //ClusterHealthRequest request = new ClusterHealthRequest(indices);

        //request.timeout(TimeValue.timeValueSeconds(50));
        //request.timeout("50s");
        //request.masterNodeTimeout(TimeValue.timeValueSeconds(20));
        //request.masterNodeTimeout("20s");
        //request.waitForStatus(ClusterHealthStatus.YELLOW);
        //request.waitForYellowStatus();
        try
        {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indices);
            if (StrUtil.isNotEmpty(queryStr)) {
                searchRequest.indices(queryStr);
            }
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits= searchResponse.getHits();
            Iterator<SearchHit> iterator = searchHits.iterator();
            while(iterator.hasNext()) {
                //获取单个索引的状态信息
                SearchHit item = iterator.next();
                IndexVo vo = new IndexVo();
                vo.setIndexName(item.getIndex());

                /*//获取文档数据
                DocsStats docsStats = stat.get.getStatus();
                vo.setDocsCount(docsStats.getCount());
                vo.setDocsDeleted(docsStats.getDeleted());
                //获取存储数据
                vo.setStoreSizeInBytes(getKB(stat.getTotal().getStore().getSizeInBytes()));
                //获取查询数据
                SearchStats.Stats searchStats = stat.getTotal().getSearch().getTotal();
                vo.setQueryCount(searchStats.getQueryCount());
                vo.setQueryTimeInMillis(searchStats.getQueryTimeInMillis() / 1000D);*/

                indexList.add(vo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("list索引状态信息异常："+e.getMessage());
        }
        return PageResult.<IndexVo>builder().data(indexList).code(0).build();
    }


    @Override
    public boolean indexExists(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        try {
            return highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("indexExists异常："+e.getMessage());
            return false;
        }
    }

    /**
     * bytes 转换为 kb
     */
    private Double getKB(Long bytes) {
        if (bytes == null) {
            return 0D;
        }
        return bytes / 1024D;
    }

    @Override
    public Map<String, Object> show(String indexName) {
        Map<String, Object> result = new HashMap<>(1);
        /*ImmutableOpenMap<String, IndexMetaData>  stat = highLevelClient.cluster()
                .prepareState().setIndices(indexName).execute().actionGet()
                .getState()
                .getMetaData()
                .getIndices();*/

        try{
        //获取单个索引的状态信息
        GetIndexRequest requestIndex = new GetIndexRequest().indices(indexName);
        GetIndexResponse indexMetaData = highLevelClient.indices().get(requestIndex,RequestOptions.DEFAULT);
        //IndexMetaData indexMetaData = stat.get(indexName);
        //获取settings数据
        ImmutableOpenMap<String, Settings> settMap = indexMetaData.getSettings();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappOpenMap = indexMetaData.getMappings();
        ImmutableOpenMap<String, List<AliasMetaData>> aliasesOpenMap = indexMetaData.getAliases();
        //ImmutableOpenMap<String, MappingMetaData> mappOpenMap = indexMetaData.getMappings();
        //ImmutableOpenMap<String, AliasMetaData> aliasesOpenMap = indexMetaData.getAliases();

        Map<String, Object> indexMap = new HashMap<>(3);
        Map<String, Object> mappMap = new HashMap<>(mappOpenMap.size());
        Map<String, Object> aliasesMap = new HashMap<>(aliasesOpenMap.size());
        indexMap.put("aliases", aliasesMap);
        indexMap.put("settings", settMap);
        indexMap.put("mappings", mappMap);
        result.put(indexName, indexMap);
        //获取mappings数据
        /*for (ObjectCursor<String> key : mappOpenMap.v.keys()) {
            MappingMetaData data = mappOpenMap..get(key.value);
            Map<String, Object> dataMap = data.getSourceAsMap();
            mappMap.put(key.value, dataMap);
        }
        //获取aliases数据
        for (ObjectCursor<String> key : aliasesOpenMap.keys()) {
            AliasMetaData data = aliasesOpenMap.get(key.value);
            aliasesMap.put(key.value, data.alias());
        }*/

        } catch (IOException e) {
            e.printStackTrace();
            log.error("show索引异常："+e.getMessage());
        }
        return result;
    }
}

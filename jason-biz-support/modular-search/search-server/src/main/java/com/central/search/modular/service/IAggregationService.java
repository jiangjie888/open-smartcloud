package com.central.search.modular.service;

import java.io.IOException;
import java.util.Map;

/**
 */
public interface IAggregationService {
    /**
     * 访问统计聚合查询
     * @param indexName 索引名
     * @param routing es的路由
     * @return
     */
    Map<String, Object> requestStatAgg(String indexName, String routing);
}

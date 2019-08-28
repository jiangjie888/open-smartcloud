package com.central.search.modular.service;

import com.central.core.model.page.PageResult;
import com.central.search.modular.dto.IndexDto;
import com.central.search.modular.model.IndexVo;

import java.io.IOException;
import java.util.Map;

/**
 * 索引
 */
public interface IIndexService {
    /**
     * 创建索引
     */
    void create(IndexDto indexDto);

    /**
     * 删除索引
     * @param indexName 索引名
     */
    void delete(String indexName);

    /**
     * 索引列表
     * @param queryStr 搜索字符串
     * @param indices 默认显示的索引名
     */
    PageResult<IndexVo> list(String queryStr, String... indices);

    /*
    * 索引是否存在
    * */
    boolean indexExists(String indexName);

    /**
     * 显示索引明细
     * @param indexName 索引名
     */
    Map<String, Object> show(String indexName);
}

package com.central.search.modular.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.central.core.model.page.PageResult;
import com.central.search.api.entity.SearchDto;
import com.central.search.core.util.SearchBuilder;
import com.central.search.modular.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 通用搜索
 */
@Slf4j
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private RestHighLevelClient highLevelClient;

    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    @Override
    public PageResult<JSONObject> strQuery(String indexName, SearchDto searchDto) {
        PageResult<JSONObject> result = new PageResult<JSONObject>();
        try{
            result = SearchBuilder.builder(highLevelClient, indexName)
                .setStringQuery(searchDto.getQueryStr())
                .addSort(searchDto.getSortCol(), SortOrder.DESC)
                .setIsHighlight(searchDto.getIsHighlighter())
                .getPage(searchDto.getPage(), searchDto.getLimit());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("通用搜索异常："+e.getMessage());
        }
        return result;
    }
}

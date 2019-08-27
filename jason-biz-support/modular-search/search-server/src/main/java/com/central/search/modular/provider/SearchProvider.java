package com.central.search.modular.provider;

import com.alibaba.fastjson.JSONObject;
import com.central.core.model.page.PageResult;
import com.central.search.api.SearchApi;
import com.central.search.api.entity.SearchDto;
import com.central.search.modular.service.IAggregationService;
import com.central.search.modular.service.ISearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 通用搜索
 */
@Slf4j
@RestController
@Api(tags = "搜索模块api")
public class SearchProvider implements SearchApi {
    @Autowired
    private ISearchService searchService;
    @Autowired
    private IAggregationService aggregationService;

    /**
     * 访问统计聚合查询
     * @param indexName 索引名
     * @param routing es的路由
     */
    @Override
    @ApiOperation(value = "访问统计聚合查询")
    public Map<String, Object> requestStatAgg(@PathVariable String indexName, @PathVariable String routing) {
        return aggregationService.requestStatAgg(indexName, routing);
    }

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @Override
    @ApiOperation(value = "查询文档列表")
    public PageResult<JSONObject> strQuery(@PathVariable String indexName, @RequestBody(required = false) SearchDto searchDto) {
        if (searchDto == null) {
            searchDto = new SearchDto();
        }
        return searchService.strQuery(indexName, searchDto);
    }


}
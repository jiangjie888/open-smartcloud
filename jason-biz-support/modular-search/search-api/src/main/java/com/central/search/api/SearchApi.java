package com.central.search.api;

import com.alibaba.fastjson.JSONObject;
import com.central.core.model.page.PageResult;
import com.central.search.api.entity.SearchDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 搜索相关的远程调用
 */

@RequestMapping("/api/search")
public interface SearchApi {
    /**
     * 访问统计聚合查询
     * @param indexName 索引名
     * @param routing es的路由
     */
    @GetMapping(value = "/requestStat/{indexName}/{routing}")
    Map<String, Object> requestStatAgg(@PathVariable("indexName") String indexName, @PathVariable("routing") String routing);

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @PostMapping(value = "/listDoc/{indexName}")
    PageResult<JSONObject> strQuery(@PathVariable("indexName") String indexName, @RequestBody SearchDto searchDto);
}

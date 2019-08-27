package com.central.search.client.modular.consumer.fallback;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.central.core.model.page.PageResult;
import com.central.search.api.entity.SearchDto;
import com.central.search.client.modular.consumer.SearchServiceConsumer;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * searchService降级工场
 */
@Slf4j
@Component
public class SearchServiceFallbackFactory implements FallbackFactory<SearchServiceConsumer> {
    @Override
    public SearchServiceConsumer create(Throwable throwable) {
        return new SearchServiceConsumer() {
            @Override
            public PageResult<JSONObject> strQuery(String indexName, SearchDto searchDto) {
                log.error("通过索引{}搜索异常:{}", indexName, throwable);
                return PageResult.<JSONObject>builder().build();
            }

            @Override
            public Map<String, Object> requestStatAgg(String indexName, String routing) {
                log.error("通过索引{}搜索异常:{}", indexName, throwable);
                return MapUtil.newHashMap(0);
            }
        };
    }
}

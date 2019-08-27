package com.central.search.client.modular.consumer;


import com.central.search.api.SearchApi;
import com.central.search.client.modular.consumer.fallback.SearchServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "全文搜索消费者")
@FeignClient(name = "search-server", fallbackFactory = SearchServiceFallbackFactory.class, decode404 = true)
public interface SearchServiceConsumer extends SearchApi {

}

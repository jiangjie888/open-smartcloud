package com.central.zuul.core.filter.pre;

import com.central.core.model.constants.SecurityConstants;
import com.central.zuul.core.constants.ZuulFiltersOrder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * requestNo生成过滤器
 */
@Component
public class RequestNoGenerateFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return ZuulFiltersOrder.REQUEST_NO_GENERATE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();

        //生成唯一请求号uuid
        String requestNo = IdWorker.getIdStr();
        currentContext.set(SecurityConstants.REQUEST_NO_HEADER_NAME, requestNo);
        currentContext.addZuulRequestHeader(SecurityConstants.REQUEST_NO_HEADER_NAME, requestNo);
        response.addHeader(SecurityConstants.REQUEST_NO_HEADER_NAME, requestNo);

        return null;
    }
}

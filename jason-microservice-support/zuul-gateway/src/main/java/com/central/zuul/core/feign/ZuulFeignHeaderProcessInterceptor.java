package com.central.zuul.core.feign;

import com.central.core.feign.FeignHeaderProcessInterceptor;
import com.central.core.model.constants.SecurityConstants;
import com.netflix.zuul.context.RequestContext;
import feign.RequestTemplate;

/**
 * zuul对feign拦截器的拓展
 */
public class ZuulFeignHeaderProcessInterceptor extends FeignHeaderProcessInterceptor {

    @Override
    public void addOtherHeaders(RequestTemplate requestTemplate) {

        RequestContext currentContext = RequestContext.getCurrentContext();
        Object contextObject = currentContext.get(SecurityConstants.REQUEST_NO_HEADER_NAME);

        requestTemplate.header(SecurityConstants.REQUEST_NO_HEADER_NAME, contextObject == null ? "" : contextObject.toString());
    }
}
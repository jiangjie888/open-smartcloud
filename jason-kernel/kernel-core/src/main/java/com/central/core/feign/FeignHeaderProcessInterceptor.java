package com.central.core.feign;

import com.central.core.utils.HttpContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign远程调用添加header的过滤器
 */
@Slf4j
public class FeignHeaderProcessInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            if (log.isDebugEnabled()) {
                log.debug("被调环境中不存在request对象，则不往header里添加当前请求环境的header!");
            }
            return;
        } else {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
        }
        this.addOtherHeaders(requestTemplate);
    }

    public void addOtherHeaders(RequestTemplate requestTemplate) {
        /*RequestContext currentContext = RequestContext.getCurrentContext();
        Object contextObject = currentContext.get(RosesConstants.REQUEST_NO_HEADER_NAME);
        requestTemplate.header(RosesConstants.REQUEST_NO_HEADER_NAME, contextObject == null ? "" : contextObject.toString());*/
    }
}

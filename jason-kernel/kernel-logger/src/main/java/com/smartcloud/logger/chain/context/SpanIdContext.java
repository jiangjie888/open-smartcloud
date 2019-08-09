package com.smartcloud.logger.chain.context;

import com.central.core.model.api.base.AbstractBaseRequest;
import com.central.core.model.constants.SecurityConstants;
import com.central.core.utils.HttpContext;
import com.central.core.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * header中的spanId的上下文,获取上个请求的spanId，和holder的区别是，holder放的是本应用的spanId
 */
@Slf4j
public class SpanIdContext {

    /**
     * 通过http请求的header中获取spanId
     */
    public static String getSpanIdByHttpHeader() {
        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            if (log.isDebugEnabled()) {
                log.info("获取spanId失败，当前不是http请求环境！");
            }
            return "";
        } else {
            String requestId = request.getHeader(SecurityConstants.SPAN_ID_HEADER_NAME);
            if (ToolUtil.isEmpty(requestId)) {
                return "";
            } else {
                return requestId;
            }
        }
    }

    /**
     * 通过请求参数获取spanId，参数必须是AbstractBaseRequest的子类
     */
    public static String getSpanIdByRequestParam(Object[] params) {

        if (params == null || params.length <= 0) {
            return "";
        } else {
            for (Object paramItem : params) {
                if (paramItem instanceof AbstractBaseRequest) {
                    AbstractBaseRequest abstractBaseRequest = (AbstractBaseRequest) paramItem;
                    return abstractBaseRequest.getSpanId();
                }
            }
            return "";
        }
    }

}

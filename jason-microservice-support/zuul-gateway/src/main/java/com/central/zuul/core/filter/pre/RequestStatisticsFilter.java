package com.central.zuul.core.filter.pre;


import cn.hutool.core.util.StrUtil;
import com.central.logger.monitor.PointUtil;
import com.central.zuul.core.constants.ZuulFiltersOrder;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RequestStatisticsFilter extends ZuulFilter {
    private final static String UNKNOWN_STR = "unknown";

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return ZuulFiltersOrder.LOG_POINT_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));

        //埋点
        PointUtil.debug("0","request-statistics",
                "ip="+this.getIpAddr(req)
                        +"&browser="+userAgent.getBrowser()
                        +"&operatingSystem="+userAgent.getOperatingSystem());
        return null;
    }

    /**
     * 获取Ip地址
     */
    public  String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (isEmptyIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (isEmptyIP(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (isEmptyIP(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                    if (isEmptyIP(ip)) {
                        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                        if (isEmptyIP(ip)) {
                            ip = request.getRemoteAddr();
                        }
                    }
                }
            }
        }
        return ip;
    }

    private boolean isEmptyIP(String ip) {
        if (StrUtil.isEmpty(ip) || UNKNOWN_STR.equalsIgnoreCase(ip)) {
            return true;
        }
        return false;
    }
}
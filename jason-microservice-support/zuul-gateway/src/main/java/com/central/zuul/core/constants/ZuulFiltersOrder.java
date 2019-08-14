package com.central.zuul.core.constants;

/**
 * 网关的常量
 */
public interface ZuulFiltersOrder {

    /**
     * 请求号生成器过滤器顺序
     */
    int REQUEST_NO_GENERATE_FILTER_ORDER = -10;

    /**
     * 用户token验证的过滤器顺序
     */
    int USER_TOKEN_FILTER_ORDER = 20;

    /*
    * 日志埋点过滤器顺序
    * */
    int LOG_POINT_FILTER_ORDER = 30;


    /**
     * 路径资源校验的顺序
     */
    int PATH_MATCH_FILTER_ORDER = 40;

}

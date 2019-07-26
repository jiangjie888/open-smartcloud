package com.central.sentinel.config;

import org.springframework.cloud.alibaba.sentinel.datasource.config.NacosDataSourceProperties;
import org.springframework.cloud.alibaba.sentinel.datasource.config.DataSourcePropertiesConfiguration;
import org.springframework.cloud.alibaba.sentinel.datasource.converter.SentinelConverter;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.central.core.model.reqres.response.ResponseData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Sentinel配置类
 */
public class SentinelAutoConfigure {
    public SentinelAutoConfigure() {
        WebCallbackManager.setUrlBlockHandler(new CustomUrlBlockHandler());
    }

    /**
     * 限流、熔断统一处理类
     */
    public class CustomUrlBlockHandler implements UrlBlockHandler {
        @Override
        public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
            ResponseData result = ResponseData.error("flow-limiting");
            httpServletResponse.getWriter().print(JSONUtil.toJsonStr(result));
        }
    }
}

package com.central.core.utils;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.central.core.converter.RequestDataMessageConvert;
import com.central.core.converter.RequestDataTypeMethodProcessor;
import com.central.core.converter.TokenArgumentResolver;
import com.central.core.model.api.UserService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 统一请求转化器默认配置
 */
public class MvcAdapter {

    public static RequestMappingHandlerAdapter requestMappingHandlerAdapter(
            RequestMappingHandlerAdapter original,
            FastJsonHttpMessageConverter fastJsonHttpMessageConverter,
            RequestDataMessageConvert requestDataMessageConvert,
            UserService userService) {

        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(requestDataMessageConvert);

        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new RequestDataTypeMethodProcessor(converters));
        argumentResolvers.add(new TokenArgumentResolver(userService));
        original.setCustomArgumentResolvers(argumentResolvers);

        List<HttpMessageConverter<?>> list = new LinkedList<>();

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);  // see SPR-7316
        list.add(stringHttpMessageConverter);

        list.add(fastJsonHttpMessageConverter);
        original.setMessageConverters(list);
        return original;
    }
}
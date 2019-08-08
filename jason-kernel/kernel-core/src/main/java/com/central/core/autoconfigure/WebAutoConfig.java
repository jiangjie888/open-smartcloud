package com.central.core.autoconfigure;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.central.core.autoconfigure.aop.RequestDataAop;
import com.central.core.base.controller.GlobalErrorView;
import com.central.core.converter.RequestDataMessageConvert;
import com.central.core.model.api.UserService;
import com.central.core.utils.MvcAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 默认web配置
 * WebMvcConfigurationSupport-->不需要返回逻辑视图,可以选择继承此类；Spring Boot的WebMvc自动配置失效(WebMvcAutoConfiguration自动化配置)
 * WebMvcCofigurer-->返回逻辑视图,可以选择实现此方法,重写addInterceptor方法
 */
@Configuration
public class WebAutoConfig extends WebMvcConfigurationSupport {
    //@Resource
    //private BaseInterceptor baseInterceptor;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    ///返回逻辑视图，添加自己的拦截器
    /*@Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }*/

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要配置2：----------- 告知拦截器：/static/admin/** 与 /static/user/** 不需要拦截 （配置的是 路径）
        registry.addInterceptor(baseInterceptor).excludePathPatterns("/static/**");
    }*/

    /**
     * 增加swagger的支持，静态文件支持
     */
    //@ConditionalOnProperty(name = "jason.swagger.enabled", matchIfMissing = true)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //super.addResourceHandlers(registry);
    }

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
   /* @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new TokenArgumentResolver(userService));
    }*/

    /**
     * 默认错误页面，返回json
     */
    @Bean("error")
    public GlobalErrorView error() {
        return new GlobalErrorView();
    }

    /**
     * 控制器层临时缓存RequestData的aop
     */
    @Bean
    public RequestDataAop requestDataAop() {
        return new RequestDataAop();
    }

    /**
     * RequestData,@LoginUser,Response解析器，fastjson的converter
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(
            FastJsonHttpMessageConverter fastJsonHttpMessageConverter,
            RequestDataMessageConvert requestDataMessageConvert) {

        /*// 1、需要先定义一个 convert 转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        //3、在convert中添加配置信息.
        fastConverter.setFastJsonConfig(fastJsonConfig);*/

        return MvcAdapter.requestMappingHandlerAdapter(
                super.requestMappingHandlerAdapter(), fastJsonHttpMessageConverter, requestDataMessageConvert,userService);
    }

    /**
     * RequestData解析器
     */
    @Bean
    public RequestDataMessageConvert requestDataMessageConvert() {
        return new RequestDataMessageConvert();
    }

    /**
     * 时间转化器
     */
    @PostConstruct
    public void addConversionConfig() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if ((initializer != null ? initializer.getConversionService() : null) != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new StringToDateConverter());
        }
    }

    public class StringToDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String dateString) {
            return DateUtil.parse(dateString);
        }
    }
}



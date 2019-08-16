//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.custom;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

public class SentinelBeanPostProcessor implements MergedBeanDefinitionPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(SentinelBeanPostProcessor.class);
    private final ApplicationContext applicationContext;
    private ConcurrentHashMap<String, SentinelRestTemplate> cache = new ConcurrentHashMap();

    public SentinelBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (this.checkSentinelProtect(beanDefinition, beanType)) {
            SentinelRestTemplate sentinelRestTemplate;
            if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                sentinelRestTemplate = (SentinelRestTemplate)((StandardMethodMetadata)beanDefinition.getSource()).getIntrospectedMethod().getAnnotation(SentinelRestTemplate.class);
            } else {
                sentinelRestTemplate = (SentinelRestTemplate)beanDefinition.getResolvedFactoryMethod().getAnnotation(SentinelRestTemplate.class);
            }

            this.checkSentinelRestTemplate(sentinelRestTemplate, beanName);
            this.cache.put(beanName, sentinelRestTemplate);
        }

    }

    private void checkSentinelRestTemplate(SentinelRestTemplate sentinelRestTemplate, String beanName) {
        this.checkBlock4RestTemplate(sentinelRestTemplate.blockHandlerClass(), sentinelRestTemplate.blockHandler(), beanName, "block");
        this.checkBlock4RestTemplate(sentinelRestTemplate.fallbackClass(), sentinelRestTemplate.fallback(), beanName, "fallback");
    }

    private void checkBlock4RestTemplate(Class<?> blockClass, String blockMethod, String beanName, String type) {
        if (blockClass != Void.TYPE || !StringUtils.isEmpty(blockMethod)) {
            if (blockClass != Void.TYPE && StringUtils.isEmpty(blockMethod)) {
                log.error("{} class attribute exists but {} method attribute is not exists in bean[{}]", new Object[]{type, type, beanName});
                throw new IllegalArgumentException(type + " class attribute exists but " + type + " method attribute is not exists in bean[" + beanName + "]");
            } else if (blockClass == Void.TYPE && !StringUtils.isEmpty(blockMethod)) {
                log.error("{} method attribute exists but {} class attribute is not exists in bean[{}]", new Object[]{type, type, beanName});
                throw new IllegalArgumentException(type + " method attribute exists but " + type + " class attribute is not exists in bean[" + beanName + "]");
            } else {
                Class[] args = new Class[]{HttpRequest.class, byte[].class, ClientHttpRequestExecution.class, BlockException.class};
                String argsStr = Arrays.toString(Arrays.stream(args).map((clazz) -> {
                    return clazz.getSimpleName();
                }).toArray());
                Method foundMethod = ClassUtils.getStaticMethod(blockClass, blockMethod, args);
                if (foundMethod == null) {
                    log.error("{} static method can not be found in bean[{}]. The right method signature is {}#{}{}, please check your class name, method name and arguments", new Object[]{type, beanName, blockClass.getName(), blockMethod, argsStr});
                    throw new IllegalArgumentException(type + " static method can not be found in bean[" + beanName + "]. The right method signature is " + blockClass.getName() + "#" + blockMethod + argsStr + ", please check your class name, method name and arguments");
                } else if (!ClientHttpResponse.class.isAssignableFrom(foundMethod.getReturnType())) {
                    log.error("{} method return value in bean[{}] is not ClientHttpResponse: {}#{}{}", new Object[]{type, beanName, blockClass.getName(), blockMethod, argsStr});
                    throw new IllegalArgumentException(type + " method return value in bean[" + beanName + "] is not ClientHttpResponse: " + blockClass.getName() + "#" + blockMethod + argsStr);
                } else {
                    if (type.equals("block")) {
                        BlockClassRegistry.updateBlockHandlerFor(blockClass, blockMethod, foundMethod);
                    } else {
                        BlockClassRegistry.updateFallbackFor(blockClass, blockMethod, foundMethod);
                    }

                }
            }
        }
    }

    private boolean checkSentinelProtect(RootBeanDefinition beanDefinition, Class<?> beanType) {
        return beanType == RestTemplate.class && (this.checkStandardMethodMetadata(beanDefinition) || this.checkMethodMetadataReadingVisitor(beanDefinition));
    }

    private boolean checkStandardMethodMetadata(RootBeanDefinition beanDefinition) {
        return beanDefinition.getSource() instanceof StandardMethodMetadata && ((StandardMethodMetadata)beanDefinition.getSource()).isAnnotated(SentinelRestTemplate.class.getName());
    }

    private boolean checkMethodMetadataReadingVisitor(RootBeanDefinition beanDefinition) {
        return beanDefinition.getSource() instanceof MethodMetadataReadingVisitor && ((MethodMetadataReadingVisitor)beanDefinition.getSource()).isAnnotated(SentinelRestTemplate.class.getName());
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.cache.containsKey(beanName)) {
            StringBuilder interceptorBeanNamePrefix = new StringBuilder();
            SentinelRestTemplate sentinelRestTemplate = (SentinelRestTemplate)this.cache.get(beanName);
            interceptorBeanNamePrefix.append(StringUtils.uncapitalize(SentinelProtectInterceptor.class.getSimpleName())).append("_").append(sentinelRestTemplate.blockHandlerClass().getSimpleName()).append(sentinelRestTemplate.blockHandler()).append("_").append(sentinelRestTemplate.fallbackClass().getSimpleName()).append(sentinelRestTemplate.fallback());
            RestTemplate restTemplate = (RestTemplate)bean;
            String interceptorBeanName = interceptorBeanNamePrefix + "@" + bean.toString();
            this.registerBean(interceptorBeanName, sentinelRestTemplate, (RestTemplate)bean);
            SentinelProtectInterceptor sentinelProtectInterceptor = (SentinelProtectInterceptor)this.applicationContext.getBean(interceptorBeanName, SentinelProtectInterceptor.class);
            restTemplate.getInterceptors().add(0, sentinelProtectInterceptor);
        }

        return bean;
    }

    private void registerBean(String interceptorBeanName, SentinelRestTemplate sentinelRestTemplate, RestTemplate restTemplate) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)this.applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SentinelProtectInterceptor.class);
        beanDefinitionBuilder.addConstructorArgValue(sentinelRestTemplate);
        beanDefinitionBuilder.addConstructorArgValue(restTemplate);
        BeanDefinition interceptorBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanFactory.registerBeanDefinition(interceptorBeanName, interceptorBeanDefinition);
    }
}

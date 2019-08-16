//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.custom;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.alibaba.sentinel.SentinelProperties;
import org.springframework.cloud.alibaba.sentinel.datasource.config.AbstractDataSourceProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class SentinelDataSourceHandler implements SmartInitializingSingleton {
    private static final Logger log = LoggerFactory.getLogger(SentinelDataSourceHandler.class);
    private List<String> dataTypeList = Arrays.asList("json", "xml");
    private final String DATA_TYPE_FIELD = "dataType";
    private final String CUSTOM_DATA_TYPE = "custom";
    private final String CONVERTER_CLASS_FIELD = "converterClass";
    private final DefaultListableBeanFactory beanFactory;
    @Autowired
    private SentinelProperties sentinelProperties;

    public SentinelDataSourceHandler(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void afterSingletonsInstantiated() {
        this.sentinelProperties.getDatasource().forEach((dataSourceName, dataSourceProperties) -> {
            try {
                List<String> validFields = dataSourceProperties.getValidField();
                if (validFields.size() != 1) {
                    log.error("[Sentinel Starter] DataSource " + dataSourceName + " multi datasource active and won't loaded: " + dataSourceProperties.getValidField());
                    return;
                }

                AbstractDataSourceProperties abstractDataSourceProperties = dataSourceProperties.getValidDataSourceProperties();
                abstractDataSourceProperties.preCheck(dataSourceName);
                this.registerBean(abstractDataSourceProperties, dataSourceName + "-sentinel-" + (String)validFields.get(0) + "-datasource");
            } catch (Exception var5) {
                log.error("[Sentinel Starter] DataSource " + dataSourceName + " build error: " + var5.getMessage(), var5);
            }

        });
    }

    private void registerBean(AbstractDataSourceProperties dataSourceProperties, String dataSourceName) {
        Map<String, Object> propertyMap = (Map)Arrays.stream(dataSourceProperties.getClass().getDeclaredFields()).collect(HashMap::new, (m, v) -> {
            try {
                v.setAccessible(true);
                m.put(v.getName(), v.get(dataSourceProperties));
            } catch (IllegalAccessException var5) {
                log.error("[Sentinel Starter] DataSource " + dataSourceName + " field: " + v.getName() + " invoke error");
                throw new RuntimeException("[Sentinel Starter] DataSource " + dataSourceName + " field: " + v.getName() + " invoke error", var5);
            }
        }, HashMap::putAll);
        propertyMap.put("converterClass", dataSourceProperties.getConverterClass());
        propertyMap.put("dataType", dataSourceProperties.getDataType());
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(dataSourceProperties.getFactoryBeanName());
        propertyMap.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(dataSourceProperties.getClass(), propertyName);
            if (null != field) {
                if ("dataType".equals(propertyName)) {
                    String dataType = StringUtils.trimAllWhitespace(propertyValue.toString());
                    if ("custom".equals(dataType)) {
                        try {
                            if (StringUtils.isEmpty(dataSourceProperties.getConverterClass())) {
                                throw new RuntimeException("[Sentinel Starter] DataSource " + dataSourceName + "dataType is custom, please set converter-class property");
                            }

                            String customConvertBeanName = "sentinel-" + dataSourceProperties.getConverterClass();
                            if (!this.beanFactory.containsBean(customConvertBeanName)) {
                                this.beanFactory.registerBeanDefinition(customConvertBeanName, BeanDefinitionBuilder.genericBeanDefinition(Class.forName(dataSourceProperties.getConverterClass())).getBeanDefinition());
                            }

                            builder.addPropertyReference("converter", customConvertBeanName);
                        } catch (ClassNotFoundException var9) {
                            log.error("[Sentinel Starter] DataSource " + dataSourceName + " handle " + dataSourceProperties.getClass().getSimpleName() + " error, class name: " + dataSourceProperties.getConverterClass());
                            throw new RuntimeException("[Sentinel Starter] DataSource " + dataSourceName + " handle " + dataSourceProperties.getClass().getSimpleName() + " error, class name: " + dataSourceProperties.getConverterClass(), var9);
                        }
                    } else {
                        if (!this.dataTypeList.contains(StringUtils.trimAllWhitespace(propertyValue.toString()))) {
                            throw new RuntimeException("[Sentinel Starter] DataSource " + dataSourceName + " dataType: " + propertyValue + " is not support now. please using these types: " + this.dataTypeList.toString());
                        }

                        builder.addPropertyReference("converter", "sentinel-" + propertyValue.toString() + "-" + dataSourceProperties.getRuleType().getName() + "-converter");
                    }
                } else {
                    if ("converterClass".equals(propertyName)) {
                        return;
                    }

                    Optional.ofNullable(propertyValue).ifPresent((v) -> {
                        builder.addPropertyValue(propertyName, v);
                    });
                }

            }
        });
        this.beanFactory.registerBeanDefinition(dataSourceName, builder.getBeanDefinition());
        AbstractDataSource newDataSource = (AbstractDataSource)this.beanFactory.getBean(dataSourceName);
        this.logAndCheckRuleType(newDataSource, dataSourceName, dataSourceProperties.getRuleType().getClazz());
        dataSourceProperties.postRegister(newDataSource);
    }

    private void logAndCheckRuleType(AbstractDataSource dataSource, String dataSourceName, Class<? extends AbstractRule> ruleClass) {
        Object ruleConfig;
        try {
            ruleConfig = dataSource.loadConfig();
        } catch (Exception var6) {
            log.error("[Sentinel Starter] DataSource " + dataSourceName + " loadConfig error: " + var6.getMessage(), var6);
            return;
        }

        if (ruleConfig instanceof List) {
            List convertedRuleList = (List)ruleConfig;
            if (CollectionUtils.isEmpty(convertedRuleList)) {
                log.warn("[Sentinel Starter] DataSource {} rule list is empty.", dataSourceName);
            } else if (convertedRuleList.stream().noneMatch((rule) -> {
                return rule.getClass() == ruleClass;
            })) {
                log.error("[Sentinel Starter] DataSource {} none rules are {} type.", dataSourceName, ruleClass.getSimpleName());
                throw new IllegalArgumentException("[Sentinel Starter] DataSource " + dataSourceName + " none rules are " + ruleClass.getSimpleName() + " type.");
            } else {
                if (!convertedRuleList.stream().allMatch((rule) -> {
                    return rule.getClass() == ruleClass;
                })) {
                    log.warn("[Sentinel Starter] DataSource {} all rules are not {} type.", dataSourceName, ruleClass.getSimpleName());
                } else {
                    log.info("[Sentinel Starter] DataSource {} load {} {}", new Object[]{dataSourceName, convertedRuleList.size(), ruleClass.getSimpleName()});
                }

            }
        } else {
            log.error("[Sentinel Starter] DataSource " + dataSourceName + " rule class is not List<" + ruleClass.getSimpleName() + ">. Class: " + ruleConfig.getClass());
            throw new IllegalArgumentException("[Sentinel Starter] DataSource " + dataSourceName + " rule class is not List<" + ruleClass.getSimpleName() + ">. Class: " + ruleConfig.getClass());
        }
    }
}

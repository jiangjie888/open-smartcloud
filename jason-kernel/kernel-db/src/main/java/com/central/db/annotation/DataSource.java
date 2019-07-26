package com.central.db.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * 数据源选择
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DataSourceConfigurationSelector.class)
public @interface DataSource {
    String name();
}
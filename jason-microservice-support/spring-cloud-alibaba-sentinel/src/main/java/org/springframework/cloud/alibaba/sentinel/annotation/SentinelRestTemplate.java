package org.springframework.cloud.alibaba.sentinel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SentinelRestTemplate {
    String blockHandler() default "";

    Class<?> blockHandlerClass() default void.class;

    String fallback() default "";

    Class<?> fallbackClass() default void.class;
}

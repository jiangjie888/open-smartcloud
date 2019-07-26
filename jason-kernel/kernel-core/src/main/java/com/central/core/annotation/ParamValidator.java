package com.central.core.annotation;

import java.lang.annotation.*;

/**
 * 参数校验的标识，如果方法上加了这个注解，则会校验本参数
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamValidator {

}
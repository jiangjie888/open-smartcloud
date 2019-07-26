package com.smartcloud.logger.config.properties;

import lombok.Data;

/**
 * 日志记录的参数配置
 */
@Data
public class LogProperties {

    /**
     * 日志记录的总开关（通过kafka）
     */
    private Boolean kafka = false;

    /**
     * 记录日志的级别（逗号隔开）
     */
    private String level = "error,info";

    /**
     * 是否开启trace链式记录
     */
    private Boolean trace = true;

}

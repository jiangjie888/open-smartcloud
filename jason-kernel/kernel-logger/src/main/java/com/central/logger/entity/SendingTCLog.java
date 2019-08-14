package com.central.logger.entity;

import lombok.Data;

import java.util.Date;

/**
 * 请求时间记录日志
 */
@Data
public class SendingTCLog {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 花费时间（毫秒）
     */
    private Long useTime;

    /**
     * 创建时间
     */
    private Date createTime;

}

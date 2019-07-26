package com.central.biz.log.modular.model;

import lombok.Data;

/**
 * 调用链日志列表查询条件
 */
@Data
public class TraceLogParams {

    /**
     * 第几页
     */
    private Integer pageNo;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 自增主键where的条件值
     */
    private Long gtValue;

}

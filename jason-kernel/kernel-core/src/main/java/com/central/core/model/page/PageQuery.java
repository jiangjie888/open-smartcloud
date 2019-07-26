package com.central.core.model.page;

import lombok.Data;

/**
 * 分页查询的请求参数封装
 */
@Data
public class PageQuery {

    /**
     * 每页的条数
     */
    private Integer limit;

    /**
     * 页编码(第几页)
     */
    private Integer page;

    /**
     * 排序方式(asc 或者 desc)
     */
    private String sort;

    /**
     * 排序的字段名称
     */
    private String[] order;

    public PageQuery() {
    }

    public PageQuery(Integer pageSize, Integer pageNo, String sort, String... orderByField) {
        this.limit = pageSize;
        this.page = pageNo;
        this.sort = sort;
        this.order = orderByField;
    }
}

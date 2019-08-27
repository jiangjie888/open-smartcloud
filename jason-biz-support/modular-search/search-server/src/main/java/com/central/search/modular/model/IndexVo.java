package com.central.search.modular.model;

import lombok.Data;


@Data
public class IndexVo {
    /**
     * 索引名
     */
    private String indexName;
    /**
     * 文档数
     */
    private Long docsCount;
    /**
     * 文档删除数
     */
    private Long docsDeleted;
    /**
     * 索引大小(kb)
     */
    private Double storeSizeInBytes;
    /**
     * 总查询数
     */
    private Long queryCount;
    /**
     * 总查询耗时(s)
     */
    private Double queryTimeInMillis;
}
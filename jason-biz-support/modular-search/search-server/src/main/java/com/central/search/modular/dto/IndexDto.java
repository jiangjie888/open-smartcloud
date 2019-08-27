package com.central.search.modular.dto;

import lombok.Data;


@Data
public class IndexDto {
    /**
     * 索引名
     */
    private String indexName;
    /**
     * 分片数 number_of_shards
     */
    private Integer numberOfShards;
    /**
     * 副本数 number_of_replicas
     */
    private Integer numberOfReplicas;
    /**
     * 类型
     */
    private String type;
    /**
     * mappings内容
     */
    private String mappingsSource;
}
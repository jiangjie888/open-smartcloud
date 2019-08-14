package com.central.logger.entity;

import lombok.Data;

/**
 * 日志链实体类
 */
@Data
public class SendingTraceLog {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 请求路径
     */
    private String servletPath;

    /**
     * rpc调用类型，
     * G1,     //网关发送请求(网关前置拦截器)
     * <p>
     * P1,     //控制器接受到请求(controllerAOP)
     * <p>
     * P2,     //准备调用远程服务(consumerAOP)
     * <p>
     * P3,     //被调用端接收到请求(providerAOP)
     * <p>
     * EP1,    //控制器处理过程中出错(controllerAOP)
     * <p>
     * EP2,    //feign远程调用，调用方出错(consumerAOP)
     * <p>
     * EP3,    //feign远程调用，被调用方出错(providerAOP)
     * <p>
     * G2,     //网关接收到成功请求(网关后置拦截器)
     * <p>
     * EG2,    //网关接收到错误响应(网关后置拦截器)
     * <p>
     * TC      //记录请求耗时的类型
     */
    private String rpcPhase;

    /**
     * 唯一请求号
     */
    private String traceId;

    /**
     * 节点id
     */
    private String spanId;

    /**
     * 节点父id
     */
    private String parentSpanId;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 生成时间戳
     */
    private Long createTimestamp;

}

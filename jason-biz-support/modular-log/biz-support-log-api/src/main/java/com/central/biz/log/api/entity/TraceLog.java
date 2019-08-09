package com.central.biz.log.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
@TableName("log_trace_log")
public class TraceLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
     * 应用编码
     */
    @TableField("APP_CODE")
    private String appCode;
    /**
     * ip地址
     */
    @TableField("IP")
    private String ip;
    /**
     * 请求路径
     */
    @TableField("SERVLET_PATH")
    private String servletPath;
    /**
     * rpc调用类型，
     * G1,     //网关发送请求
     * <p>
     * G2,     //接收网关请求（切controller）
     * <p>
     * P1,     //调用端发送请求（切consumer）
     * <p>
     * P2,     //被调用端接收到请求（切provider）
     * <p>
     * P3,     //被调用端发送响应成功
     * <p>
     * P4,     //调用端接收到响应成功
     * <p>
     * EP3,    //被调用端发送响应失败
     * <p>
     * EP4,    //调用端接收到响应失败
     * <p>
     * G3,     //控制器响应网关成功
     * <p>
     * G4,     //网关接收到成功请求
     * <p>
     * EG3,    //控制器接收到错误响应
     * <p>
     * EG4,    //网关接收到错误响应
     */
    @TableField("RPC_PHASE")
    private String rpcPhase;
    /**
     * 唯一请求号
     */
    @TableField("TRACE_ID")
    private String traceId;
    /**
     * 节点id
     */
    @TableField("SPAN_ID")
    private String spanId;
    /**
     * 节点父id
     */
    @TableField("PARENT_SPAN_ID")
    private String parentSpanId;
    /**
     * 日志内容
     */
    @TableField("CONTENT")
    private String content;
    /**
     * 生成时间戳
     */
    @TableField("CREATE_TIMESTAMP")
    private Long createTimestamp;


    @Override
    public String toString() {
        return "TraceLog{" +
                ", id=" + id +
                ", appCode=" + appCode +
                ", ip=" + ip +
                ", servletPath=" + servletPath +
                ", rpcPhase=" + rpcPhase +
                ", traceId=" + traceId +
                ", spanId=" + spanId +
                ", parentSpanId=" + parentSpanId +
                ", content=" + content +
                ", createTimestamp=" + createTimestamp +
                "}";
    }
}

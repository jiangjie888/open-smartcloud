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
@TableName("log_common_log")
public class CommonLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
     * 应用编码
     */
    @TableField("APP_CODE")
    private String appCode;
    /**
     * 日志级别 info，error，warn，debug
     */
    @TableField("LEVEL")
    private String level;
    /**
     * 类名
     */
    @TableField("CLASS_NAME")
    private String className;
    /**
     * 打日志的方法的名称
     */
    @TableField("METHOD_NAME")
    private String methodName;
    /**
     * 远程访问IP地址
     */
    @TableField("IP")
    private String ip;
    /**
     * 用户账号id
     */
    @TableField("ACCOUNT_ID")
    private String accountId;
    /**
     * 日志号
     */
    @TableField("REQUEST_NO")
    private String requestNo;
    /**
     * 请求地址
     */
    @TableField("URL")
    private String url;
    /**
     * 请求的数据内容
     */
    @TableField("REQUEST_DATA")
    private String requestData;
    /**
     * 日志详情
     */
    @TableField("LOG_CONTENT")
    private String logContent;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIMESTAMP")
    private Long createTimestamp;


    @Override
    public String toString() {
        return "CommonLog{" +
                ", id=" + id +
                ", appCode=" + appCode +
                ", level=" + level +
                ", className=" + className +
                ", methodName=" + methodName +
                ", ip=" + ip +
                ", accountId=" + accountId +
                ", requestNo=" + requestNo +
                ", url=" + url +
                ", requestData=" + requestData +
                ", logContent=" + logContent +
                ", createTimestamp=" + createTimestamp +
                "}";
    }
}

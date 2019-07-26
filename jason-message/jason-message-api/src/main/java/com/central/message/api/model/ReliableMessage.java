package com.central.message.api.model;

import com.baomidou.mybatisplus.annotation.*;
import com.central.core.model.common.AuditedEntity;
import lombok.Data;
import java.io.Serializable;

/**
 * 消息服务实体表
 */
@Data
@TableName("reliable_message")
public class ReliableMessage  extends AuditedEntity<ReliableMessage> {


    private static final long serialVersionUID = -8690984571806904042L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消息ID
     */
    @TableField("message_id")
    private String messageId;
    /**
     * 消息内容
     */
    @TableField("message_body")
    private String messageBody;
    /**
     * 消息数据类型
     */
    @TableField("message_data_type")
    private String messageDataType;
    /**
     * 消费队列
     */
    @TableField("consumer_queue")
    private String consumerQueue;
    /**
     * 消息重发次数
     */
    @TableField("message_send_times")
    private Integer messageSendTimes;
    /**
     * 是否死亡
     * <p>
     * Y：已死亡
     * N：未死亡
     */
    @TableField("already_dead")
    private String alreadyDead;
    /**
     * 状态
     * <p>
     * WAIT_VERIFY：待确认
     * SENDING：发送中
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
    /**
     * 版本号
     */
    @Version
    private Long version = 0L;

    /**
     * 版本号  订单ID
     */
    @TableField("biz_unique_id")
    private Long bizUniqueId;

    public ReliableMessage() {

    }

    public ReliableMessage(String messageId, String messageBody, String consumerQueue) {
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.consumerQueue = consumerQueue;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

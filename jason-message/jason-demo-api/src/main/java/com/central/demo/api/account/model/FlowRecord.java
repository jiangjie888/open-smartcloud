package com.central.demo.api.account.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.central.core.model.common.AuditedEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 流水记录
 * </p>
 */
@Data
@TableName("flow_record")
public class FlowRecord extends AuditedEntity<FlowRecord> {


    private static final long serialVersionUID = -5030890590948664412L;

    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 流水名称
     */
    private String name;
    /**
     * 总价
     */
    private BigDecimal sum;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

package com.central.demo.api.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.central.core.model.common.AuditedEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单表
 * </p>
 */
@Data
@TableName("goods_order")
public class GoodsOrder extends AuditedEntity<GoodsOrder> {


    private static final long serialVersionUID = -3506105164727342707L;

    private Long id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 总金额
     */
    private BigDecimal sum;

    /**
     * 下单人id
     */
    private Long userId;

    /**
     * 订单状态：1.未完成   2.已完成
     */
    @TableField("status")
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

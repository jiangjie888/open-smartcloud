package com.central.sharding.modular.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
public class TOrder extends Model<TOrder> {

    private static final long serialVersionUID = 2318091599627353341L;

    @TableField(value = "order_id")
    private Long orderId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "goods_name")
    private String goodsName;

    @TableField(value = "count")
    private Integer count;

    @TableField(value = "sum")
    private double sum;

    @TableField(value = "status")
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
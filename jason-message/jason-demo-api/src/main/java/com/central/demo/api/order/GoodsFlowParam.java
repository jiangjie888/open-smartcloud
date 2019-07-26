package com.central.demo.api.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单流水记录的参数(本类同 GoodsOrder)
 * </p>

 */
@Data
public class GoodsFlowParam implements Serializable {


    private static final long serialVersionUID = -1709574927308964263L;
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
     * 创建时间
     */
    private Date creationTime;

    /**
     * 下单人id
     */
    private Long userId;


}

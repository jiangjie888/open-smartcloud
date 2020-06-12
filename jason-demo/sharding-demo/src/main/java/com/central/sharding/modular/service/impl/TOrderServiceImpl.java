package com.central.sharding.modular.service.impl;


import com.central.core.service.impl.SuperServiceImpl;
import com.central.sharding.modular.dao.TOrderMapper;
import com.central.sharding.modular.model.TOrder;
import com.central.sharding.modular.service.ITOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 订单服务
 */
@Slf4j
@Service
public class TOrderServiceImpl extends SuperServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

}

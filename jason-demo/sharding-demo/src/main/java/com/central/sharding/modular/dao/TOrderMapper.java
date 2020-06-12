package com.central.sharding.modular.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.db.mapper.SuperMapper;
import com.central.sharding.modular.model.TOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 订单管理
 */
public interface TOrderMapper extends SuperMapper<TOrder> {
	/**
	 * 分页查询订单列表
	 * @param page
	 * @param params
	 * @return
	 */
	//List<TOrder> findList(Page<TOrder> page, @Param("u") Map<String, Object> params);
}

package com.central.user.modular.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.model.user.SysUser;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.*;


/**
 * 用户管理
 */
public interface SysUserMapper extends SuperMapper<SysUser> {
	/**
	 * 分页查询用户列表
	 * @param page
	 * @param params
	 * @return
	 */
	List<SysUser> findList(Page<SysUser> page, @Param("u") Map<String, Object> params);
}

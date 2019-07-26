package com.central.infra.modular.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.model.user.SysRole;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色
 */
public interface SysRoleMapper extends SuperMapper<SysRole> {
	List<SysRole> findList(Page<SysRole> page, @Param("r") Map<String, Object> params);
}

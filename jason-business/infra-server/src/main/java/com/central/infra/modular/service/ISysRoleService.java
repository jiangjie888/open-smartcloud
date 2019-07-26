package com.central.infra.modular.service;



import com.central.core.model.page.PageResult;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.SysRole;
import com.central.core.service.ISuperService;

import java.util.Map;

/**
 */
public interface ISysRoleService extends ISuperService<SysRole> {
	void saveRole(SysRole sysRole);

	void deleteRole(Long id);

	/**
	 * 角色列表
	 * @param params
	 * @return
	 */
	PageResult<SysRole> findRoles(Map<String, Object> params);

	/**
	 * 新增或更新角色
	 * @param sysRole
	 * @return Result
	 */
	ResponseData saveOrUpdateRole(SysRole sysRole);
}

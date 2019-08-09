package com.central.user.modular.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.central.core.model.page.PageResult;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysRole;
import com.central.core.model.user.SysUser;
import com.central.core.service.ISuperService;

/**
 * 用户服务
 */
public interface ISysUserService extends ISuperService<SysUser> {
	/**
	 * 获取UserDetails对象
	 * @param username
	 * @return
	 */
	LoginAppUser findByUsername(String username);

	LoginAppUser findByOpenId(String username);

	LoginAppUser findByMobile(String username);
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	SysUser selectByUsername(String username);


	/**
	 * 通过SysUser 转换为 LoginAppUser，把roles和permissions也查询出来
	 * @param sysUser
	 * @return
	 */
	LoginAppUser getLoginAppUser(SysUser sysUser);


	/**
	 * 用户分配角色
	 * @param id
	 * @param roleIds
	 */
	void setRoleToUser(Long id, Set<Long> roleIds);

	/**
	 * 更新密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	ResponseData updatePassword(Long id, String oldPassword, String newPassword);

	/**
	 * 用户列表
	 * @param params
	 * @return
	 */
	PageResult<SysUser> findUsers(Map<String, Object> params);

	/**
	 * 用户角色列表
	 * @param userId
	 * @return
	 */
	List<SysRole> findRolesByUserId(Long userId);

	/**
	 * 状态变更
	 * @param params
	 * @return
	 */
	ResponseData updateEnabled(Map<String, Object> params);

	/**
	 * 更新
	 * @param sysUser
	 * @return
	 */
	ResponseData saveOrUpdateUser(SysUser sysUser);

	/**
	 * 查询全部用户
	 * @param params
	 * @return
	 */
	//List<SysUserExcel> findAllUsers(Map<String, Object> params);


	/**
	 * 删除用户
	 */
	boolean delUser(Long id);

	/**
	 * 保存用户
	 *
	 * @param users
	 */
	void saveUsers(List<SysUser> users);
}

package com.central.user.modular.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.exception.ServiceException;
import com.central.core.lock.DistributedLock;
import com.central.core.model.constants.CommonConstant;
import com.central.core.model.page.PageResult;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysMenu;
import com.central.core.model.user.SysRole;
import com.central.core.model.user.SysUser;
import com.central.core.service.impl.SuperServiceImpl;
import com.central.user.modular.dao.SysRoleMenuMapper;
import com.central.user.modular.dao.SysUserMapper;
import com.central.user.modular.dao.SysUserRoleMapper;
import com.central.user.modular.service.ISysPermissionService;
import com.central.user.modular.service.ISysUserService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;


/**
 * 用户服务
 */
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	private final static String LOCK_KEY_USERNAME = CommonConstant.LOCK_KEY_PREFIX+"username:";

	@Autowired
	private PasswordEncoder passwordEncoder;

	/*@Autowired
	private SysUserMapper sysUserMapper;*/

	@Autowired
	private ISysPermissionService sysPermissionService;

	@Resource
	private SysUserRoleMapper userRoleMapper;

	@Resource
	private SysRoleMenuMapper roleMenuMapper;

	@Autowired
	private DistributedLock lock;

	//@Autowired(required = false)
	//private TokenStore redisTokenStore;

	@Override
	public LoginAppUser findByUsername(String username) {
		QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
		wrapper.eq("username",username);
		//wrapper.and(q->q.eq("username", user.getUsername()));
		List<SysUser> users = baseMapper.selectList(wrapper);
		SysUser sysUser = getUser(users);
		return getLoginAppUser(sysUser);
	}

	@Override
	public LoginAppUser findByOpenId(String openid) {
		List<SysUser> users = baseMapper.selectList(
				new QueryWrapper<SysUser>().eq("openId", openid)
		);
		SysUser sysUser = getUser(users);
		return getLoginAppUser(sysUser);
	}

	@Override
	public LoginAppUser findByMobile(String mobile) {
		List<SysUser> users = baseMapper.selectList(
				new QueryWrapper<SysUser>().eq("mobile", mobile)
		);
		SysUser sysUser = getUser(users);
		return getLoginAppUser(sysUser);
	}

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	@Override
	public SysUser selectByUsername(String username) {
		List<SysUser> users = baseMapper.selectList(
				new QueryWrapper<SysUser>().eq("username", username)
		);
		return getUser(users);
	}

	private SysUser getUser(List<SysUser> users) {
		SysUser user = null;
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public LoginAppUser getLoginAppUser(SysUser sysUser) {
		if (sysUser != null) {
			LoginAppUser loginAppUser = new LoginAppUser();
			BeanUtils.copyProperties(sysUser, loginAppUser);

			List<SysRole> sysRoles = userRoleMapper.findRolesByUserId(sysUser.getId());
			// 设置角色
			loginAppUser.setRoles(sysRoles);

			if (!CollectionUtils.isEmpty(sysRoles)) {
				//Set<Long> roleIds = sysRoles.parallelStream().map(SuperEntity::getId).collect(Collectors.toSet());
				Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
				List<SysMenu> menus = roleMenuMapper.findMenusByRoleIds(roleIds, CommonConstant.PERMISSION);
				//List<SysPermission> permissions = sysPermissionService.findMenusByRoleIds(roleIds, CommonConstant.PERMISSION);
				if (!CollectionUtils.isEmpty(menus)) {
					//Set<String> permissions = menus.parallelStream().map(p -> p.getPathMethod()+":"+p.getPath()).collect(Collectors.toSet());
					Set<String> permissions = menus.parallelStream().map(p -> p.getPath()).collect(Collectors.toSet());
					// 设置权限集合
					loginAppUser.setPermissions(permissions);
				}
			}
			return loginAppUser;
		}
		return null;
	}




	/**
	 * 给用户设置角色
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void setRoleToUser(Long id, Set<Long> roleIds) {
		SysUser sysUser = baseMapper.selectById(id);
		if (sysUser == null) {
			throw new ServiceException(500,"用户不存在");
		}
		userRoleMapper.deleteUserRole(id, null);
		if (!CollectionUtils.isEmpty(roleIds)) {
			roleIds.forEach(roleId -> userRoleMapper.saveUserRoles(id, roleId));
		}
	}

	@Override
	public ResponseData updatePassword(Long id, String oldPassword, String newPassword) {
		SysUser sysUser = baseMapper.selectById(id);
		if (StrUtil.isNotBlank(oldPassword)) {
			if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
				return ResponseData.error("旧密码错误");
			}
		}
		if (StrUtil.isBlank(newPassword)) {
			newPassword = CommonConstant.DEF_USER_PASSWORD;
		}
		SysUser user = new SysUser();
		user.setId(id);
		user.setPassword(passwordEncoder.encode(newPassword));
		baseMapper.updateById(user);
		return ResponseData.success("修改成功");
	}

	@Override
	public PageResult<SysUser> findUsers(Map<String, Object> params) {
		Page<SysUser> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
		List<SysUser> list = baseMapper.findList(page, params);
		long total = page.getTotal();
		if (total > 0) {
			List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

			List<SysRole> sysRoles = userRoleMapper.findRolesByUserIds(userIds);
			list.forEach(u -> u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getUserId()))
					.collect(Collectors.toList())));
		}
		return PageResult.<SysUser>builder().data(list).code(0).count(total).build();
	}

	@Override
	public List<SysRole> findRolesByUserId(Long userId) {
		return userRoleMapper.findRolesByUserId(userId);
	}

	@Override
	public ResponseData updateEnabled(Map<String, Object> params) {
		Long id = MapUtils.getLong(params, "id");
		Boolean enabled = MapUtils.getBoolean(params, "enabled");

		SysUser appUser = baseMapper.selectById(id);
		if (appUser == null) {
			return ResponseData.error("用户不存在");
		}
		appUser.setEnabled(enabled);
		appUser.setLastModificationTime(new Date());

		int i = baseMapper.updateById(appUser);
		log.info("修改用户：{}", appUser);

		return i > 0 ? ResponseData.success(appUser) : ResponseData.error("更新失败");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseData saveOrUpdateUser(SysUser sysUser) {
		if (sysUser.getId() == null) {
			if (StringUtils.isBlank(sysUser.getType())) {
				sysUser.setType("BACKEND");
			}
			sysUser.setPassword(passwordEncoder.encode(CommonConstant.DEF_USER_PASSWORD));
			sysUser.setEnabled(Boolean.TRUE);
		}
		String username = sysUser.getUsername();
		boolean result = super.saveOrUpdateIdempotency(sysUser, lock
				, LOCK_KEY_USERNAME+username, new QueryWrapper<SysUser>().eq("username", username)
				, username+"已存在");
		//更新角色
		if (result && StrUtil.isNotEmpty(sysUser.getRoleId())) {
			userRoleMapper.deleteUserRole(sysUser.getId(), null);
			List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
			if (!CollectionUtils.isEmpty(roleIds)) {
				roleIds.forEach(roleId -> userRoleMapper.saveUserRoles(sysUser.getId(), Long.parseLong(roleId.toString())));
			}
		}
		return result ? ResponseData.success(sysUser) : ResponseData.error("操作失败");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean delUser(Long id) {
		userRoleMapper.deleteUserRole(id, null);
		return baseMapper.deleteById(id) > 0;
	}

	/*@Override
	public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
		List<SysUserExcel> sysUserExcels = new ArrayList<>();
		List<SysUser> list = baseMapper.findList(new Page<>(1, -1), params);

		for (SysUser sysUser : list) {
			SysUserExcel sysUserExcel = new SysUserExcel();
			BeanUtils.copyProperties(sysUser, sysUserExcel);
			sysUserExcels.add(sysUserExcel);
		}
		return sysUserExcels;
	}*/

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUsers(List<SysUser> users) {
		users.forEach(u -> baseMapper.insert(u));
	}

	/*@Transactional
	@Override
	public void addSysUser(SysUser sysUser) {
		String username = sysUser.getUserName();
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("用户名不能为空");
		}

		if (PhoneUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
			throw new IllegalArgumentException("用户名要包含英文字符");
		}

		if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
			throw new IllegalArgumentException("用户名不能包含@");
		}

		if (username.contains("|")) {
			throw new IllegalArgumentException("用户名不能包含|字符");
		}

		if (StringUtils.isBlank(sysUser.getPassword())) {
			throw new IllegalArgumentException("密码不能为空");
		}

		if (StringUtils.isBlank(sysUser.getNickname())) {
			sysUser.setNickname(username);
		}

		if (StringUtils.isBlank(sysUser.getType())) {
			sysUser.setType(UserType.APP.name());
		}

		SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
		if (persistenceUser != null && persistenceUser.getUsername() != null) {
			throw new IllegalArgumentException("用户名已存在");

		}

		sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
		sysUser.setEnabled(Boolean.TRUE);
		sysUser.setCreateTime(new Date());
		sysUser.setUpdateTime(sysUser.getCreateTime());
		sysUserDao.save(sysUser);
		log.info("添加用户：{}", sysUser);
	}*/
/*	@Transactional
	@Override
	public SysUser updateSysUser(SysUser sysUser) {
		sysUser.setLastModificationTime(new Date());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
			authentication = oAuth2Auth.getUserAuthentication();

			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Auth.getDetails();

			LoginAppUser user = SysUserUtil.getLoginAppUser();

			if (user != null) {

				if ( !ObjectUtils.notEqual(user.getId(),sysUser.getId()) ) {

					OAuth2AccessToken token = redisTokenStore.readAccessToken(details.getTokenValue());

					if (token != null) {

						if (!StringUtils.isBlank(sysUser.getUsername())) {
							user.setUsername(sysUser.getUsername());
						}

						if (!StringUtils.isBlank(sysUser.getEmail())) {
							user.setEmail(sysUser.getEmail());
						}

						if (!StringUtils.isBlank(sysUser.getNewPassword())) {
							user.setPassword(sysUser.getNewPassword());
						}

						if (sysUser.getRemarks() != null) {
							user.setRemarks(sysUser.getRemarks());
						}

						if (sysUser.getUserStatus() != null) {
							user.setUserStatus(sysUser.getUserStatus());
						}

						UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,
		                        null, user.getAuthorities());

						OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Auth.getOAuth2Request(), userAuthentication);
						oAuth2Authentication.setAuthenticated(true);
						redisTokenStore.storeAccessToken(token, oAuth2Authentication);

					}

				}

			}
		}

		sysUserDao.update(sysUser);
		log.info("修改用户：{}", sysUser);
		return sysUser;
	}*/
}

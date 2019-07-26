package com.central.core.model.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体绑定spring security
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginAppUser extends SysUser implements SocialUserDetails {

	private static final long serialVersionUID = 7292835308398882182L;

	//private Set<SysRole> sysRoles;

	private Set<String> permissions;

	/***
	 * 权限重写
	 */
	@JSONField(serialize=false)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new HashSet<>();
		if (!CollectionUtils.isEmpty(super.getRoles())) {
			super.getRoles().parallelStream().forEach(role -> {
				if (role.getRoleCode().startsWith("ROLE_")) {
					collection.add(new SimpleGrantedAuthority(role.getRoleCode()));
				} else {
					collection.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
				}
			});
		}

		/*if (!CollectionUtils.isEmpty(permissions)) {
			permissions.parallelStream().forEach(per -> {
				collection.add(new SimpleGrantedAuthority(per));
			});
		}*/
		return collection;
	}

	@JSONField(serialize=false)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JSONField(serialize=false)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JSONField(serialize=false)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//@JSONField(serialize=false)
	@Override
	public boolean isEnabled() {
		return getEnabled();
	}

	//@JSONField(serialize=false)
	@Override
	public String getUsername() {
		return super.getUsername();
	}

	//@JSONField(serialize=false)
	@Override
	public String getPassword() {
		return super.getPassword();
	}

	//@JSONField(serialize=false)
	@Override
	public String getUserId() {
		return getOpenId();
	}
}

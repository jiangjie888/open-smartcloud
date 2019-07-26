package com.central.zuul.modular.consumer;

import com.central.core.model.user.SysMenu;
import com.central.zuul.modular.consumer.fallback.MenuServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "infra-server", fallbackFactory = MenuServiceFallbackFactory.class, decode404 = true)
public interface MenuService {
	/**
	 * 角色菜单列表
	 * @param roleCodes
	 */
	@GetMapping(value = "/menus/findMenuByRoles/{roleCodes}")
	List<SysMenu> findByRoleCodes(@PathVariable("roleCodes") String roleCodes);
}

package com.central.zuul.modular.service.impl;

import com.central.core.model.user.SysMenu;
import com.central.oauth2.common.service.impl.DefaultPermissionServiceImpl;
import com.central.zuul.modular.consumer.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 请求权限判断service
 */
@Slf4j
@Service("permissionService")
public class PermissionServiceImpl extends DefaultPermissionServiceImpl {
    @Resource
    private MenuService menuService;

    @Override
    public List<SysMenu> findMenuByRoleCodes(String roleCodes) {
        return menuService.findByRoleCodes(roleCodes);
    }
}

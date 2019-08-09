package com.central.user.modular.service.impl;

import com.central.core.model.user.*;
import com.central.core.service.impl.SuperServiceImpl;
import com.central.user.modular.dao.SysPermissionMapper;
import com.central.user.modular.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 系统Api权限
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends SuperServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}

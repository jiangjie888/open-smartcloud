package com.central.core.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.central.core.model.common.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-07-24 10:16
 **/
    @Data
    @EqualsAndHashCode(callSuper = true)
    @TableName("sys_permission")
    public class SysPermission extends FullAuditedEntity<SysPermission> {

    private static final long serialVersionUID = 749360940290141180L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long systemCode;
    private String controllerName;
    private Boolean isViewPage;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
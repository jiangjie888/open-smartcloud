package com.central.core.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.central.core.model.common.FullAuditedEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends FullAuditedEntity<SysMenu> {

	private static final long serialVersionUID = 749360940290141180L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private Long parentId;
	private String name;
	private String css;
	private String url;
	private String path;
	private Integer sort;
	private Integer isMenu;
	private Integer type;
	private Boolean hidden;
	private Long clientId;

	@TableField(exist = false)
	private List<SysMenu> subMenus;
	@TableField(exist = false)
	private Long roleId;
	@TableField(exist = false)
	private Set<Long> menuIds;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

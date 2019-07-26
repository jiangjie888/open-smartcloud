package com.central.core.model.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.central.core.model.common.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends FullAuditedEntity<SysUser> {

	private static final long serialVersionUID = -7311959782558761790L;
	/**
	 * 
	 */

	/*private String username;
	private String password;
	private String nickname;
	private String headImgUrl;
	private String phone;
	private Integer sex;
	private Boolean enabled;
	private String type;
	private Date createTime;
	private Date updateTime;

	private List<SysRole> roles;
	private String roleId;
	private String oldPassword;
	private String newPassword;*/


	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	private String username;
	//@JSONField(serialize=false)
	private String password;
	private String nameCn;
	private String email;
	private String mobile;
	private String remarks;
	private Integer userStatus;
	private Boolean onLine;
	private Boolean enabled;
	private String type;
	private String openId;
	private Date lastLoginTime;


	//@JsonIgnore
	@JSONField(serialize=false)
	@TableField(exist = false)
	private String oldPassword;

	//@JsonIgnore
	@JSONField(serialize=false)
	@TableField(exist = false)
	private String newPassword;

	@TableField(exist = false)
	private List<SysRole> roles;
	@TableField(exist = false)
	private String roleId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

package com.central.core.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.central.core.model.common.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *  角色
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole  extends FullAuditedEntity<SysRole> {

	private static final long serialVersionUID = 4497149010220586111L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	private String roleName;
	private String roleCode;
	private Boolean isDefaultRole;
	private String roleDescription;
	private Long userId;

	/*private Boolean isDeleted;
	private long deleterUserId;
	private Date deletionTime;*/

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

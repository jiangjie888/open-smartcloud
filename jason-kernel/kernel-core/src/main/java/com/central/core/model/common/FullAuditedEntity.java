package com.central.core.model.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
public class FullAuditedEntity<T extends Model> extends AuditedEntity<T> {

    private static final long serialVersionUID = -4556972271613329267L;
    private Boolean isDeleted;//0:正常 1:删除
    private long deleterUserId;

    @JSONField(format="yyyy-MM-dd HH:mm")
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deletionTime;
}

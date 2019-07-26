package com.central.core.model.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @program: open-smartcloud
 * @description: 所有业务实体都需要继承的基类
 * @author: jason
 * @create: 2018-10-11 17:40
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AuditedEntity<T extends Model> extends Model<T> {

    private static final long serialVersionUID = 3308205274025676635L;



    //@TableId
    //private Long id;

    /*
     * 租户编码
     */
    //@TableField(value = "corp_code",fill = FieldFill.INSERT)
    //protected String corpCode;

    //private TPrimaryKey id;
    @TableField(value = "lastModifierUserId",fill = FieldFill.UPDATE)
    private Long lastModifierUserId;

    @TableField(value = "lastModificationTime",fill = FieldFill.UPDATE)
    @JSONField(format="yyyy-MM-dd HH:mm")
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastModificationTime;

    @TableField(value = "creatorUserId",fill = FieldFill.INSERT)
    private Long creatorUserId;

    @TableField(value = "creationTime",fill = FieldFill.INSERT)
    @JSONField(format="yyyy-MM-dd HH:mm")
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date creationTime = new Date();

    public AuditedEntity()
    {
        // 获取调用对象的参数化类型
        //ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        // 获取参数化类型对象的实际参数Class对象
        //Class clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        //Type[] types = parameterizedType.getActualTypeArguments();

        /*String string = new String();
        if(string.getClass().equals(clazz)) {
            String  uuid36 = UUID.randomUUID().toString();
            this.setId((T)uuid36);
        }*/
        /*if(types[0].getTypeName().equals("java.lang.String"))
        {
            String  uuid36 = UUID.randomUUID().toString();
            this.setId((TPrimaryKey)uuid36);
        }*/
        /*String string = new String();
        if (!(clazz instanceof String){
            String  uuid36 = UUID.randomUUID().toString();
            this.setId((T)uuid36);
        }*/
        //DateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //this.setRkey("");
        //this.isDelete = false;
        //this.creationTime = new Date();
    }
    //private Boolean isDelete;

    /*@Override
    protected Serializable pkVal() {
        return this.id;
    }*/
}

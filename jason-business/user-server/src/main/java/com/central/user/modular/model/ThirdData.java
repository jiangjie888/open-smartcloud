package com.central.user.modular.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("third_data_tmp")
public class ThirdData extends Model<ThirdData> {

    private static final long serialVersionUID = 2318091599627353341L;


    @TableField(value = "address")
    private String address;

    @TableField(value = "satisfied")
    private String satisfied;

    @TableField(value = "sparetime")
    private String sparetime;

    @TableField(value = "taskname")
    private String taskname;

    @TableField(value = "acceptuserdate")
    private Date acceptuserdate;

    @TableField(value = "monitor")
    private String monitor;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "home")
    private String home;

    @TableField(value = "receiveusername")
    private String receiveusername;

    @TableField(value = "condition0")
    private String condition;

    @TableField(value = "applydate")
    private Date applydate;

    @TableField(value = "windowname")
    private String windowname;

    @TableField(value = "applyway")
    private Integer applyway;

    @TableField(value = "certtype")
    private String certtype;

    @TableField(value = "is_charge")
    private Integer is_charge;

    @TableField(value = "applyername")
    private String applyername;

    @TableField(value = "banjieresult")
    private Integer banjieresult;

    @TableField(value = "step")
    private String step;

    @TableField(value = "flowsn")
    private String flowsn;

    @TableField(value = "certnum")
    private String certnum;

    @TableField(value = "status")
    private Integer status;
}
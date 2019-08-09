package com.central.user.modular.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-07-24 10:41
 **/
@Data
public class SysPermissionDto {

    private Long id;

    @NotBlank(message = "系统编码不允许为空")
    private Long systemCode;

    @NotBlank(message = "控制器和Action名称不允许为空")
    private String controllerName;

    @NotBlank(message = "是否视图页面")
    private Boolean isViewPage;
}

package com.central.infra.modular.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.central.core.model.page.PageQuery;
import com.central.core.model.page.PageResultPlus;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.SysPermission;
import com.central.core.utils.PageFactory;
import com.central.infra.modular.dto.SysPermissionDto;
import com.central.infra.modular.service.ISysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * @program: open-smartcloud
 * @description: 用户Rest API
 * @author: jason
 * @create: 2018-10-11 17:25
 **/
@Slf4j
@RestController
@Api(tags = "系统Api权限管理")
@RequestMapping("/permissions")
public class SysPermissionsController {

    @Autowired
    private ISysPermissionService sysPermissionService;


    @ApiOperation(value = "查询所有数据列表")
    @RequestMapping(value="/list",method=RequestMethod.GET)
    public ResponseData list(@RequestParam Map<String, Object> params) {
        List<SysPermission> output;
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        output = sysPermissionService.list(wrapper);
        return ResponseData.success(output);
    }

    @ResponseBody
    @ApiOperation(value = "查询数据列表分页显示")
    @RequestMapping(value = "/listPage", method = RequestMethod.POST)
    public PageResultPlus<SysPermission> listPage(@RequestBody PageQuery pageParam) {
        Page<SysPermission> page = PageFactory.createPage(pageParam);
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        Page<SysPermission> pageResults = (Page<SysPermission>) this.sysPermissionService.page(page,wrapper);
        return new PageResultPlus<>(pageResults);
    }


    @ApiOperation(value = "根据ID查询单条数据")
    @RequestMapping(value = "/findEntityById", method = RequestMethod.GET)
    public ResponseData findEntityById(@RequestParam Long id) {
        SysPermission output = sysPermissionService.getById(id);
        return ResponseData.success(output);
    }


    @ApiOperation(value = "更新单条数据")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ResponseData updateSysUser(@RequestBody SysPermission sysPermission) {
        sysPermissionService.updateById(sysPermission);
        return ResponseData.success("更新成功");
    }

    @ResponseBody
    @ApiOperation(value = "新增or更新")
    @RequestMapping(value = { "/saveOrUpdate" }, method =  RequestMethod.POST , produces="application/json;charset=UTF-8")
    public ResponseData saveOrUpdate(@RequestBody @Valid SysPermissionDto sysPermissionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            String message = bindingResult.getFieldError().getDefaultMessage();
            //自定义的返回，并将错误信息返回
            return ResponseData.error(message);
        }
        SysPermission entity = new SysPermission();
        BeanUtils.copyProperties(sysPermissionDto, entity);
        sysPermissionService.saveOrUpdate(entity);
        return ResponseData.success(entity);
    }

    /**
     * 删除
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping(value = { "/delete" }, method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public ResponseData delete(@RequestBody Long id) {
        sysPermissionService.removeById(id);
        return ResponseData.success("删除成功");
    }


}

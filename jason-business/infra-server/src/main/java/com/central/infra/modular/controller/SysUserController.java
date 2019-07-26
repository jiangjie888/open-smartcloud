package com.central.infra.modular.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.annotation.LoginUser;
import com.central.core.model.page.PageQuery;
import com.central.core.model.page.PageResult;
import com.central.core.model.page.PageResultPlus;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysRole;
import com.central.core.model.user.SysUser;
import com.central.core.utils.PageFactory;
import com.central.infra.modular.dto.SysUserDto;
import com.central.infra.modular.service.ISysUserService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 用户Rest API
 **/
@Slf4j
@RestController
@Api(tags = "用户模块api")
@RequestMapping("/users")
public class SysUserController {

    @Autowired
    private ISysUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/current")
    public LoginAppUser getCurrentUserInfo() {
        return SysUserUtil.getLoginAppUser();
    }*/

    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/current")
    public ResponseData getLoginAppUser(@LoginUser(isFull = true) SysUser user) {
        LoginAppUser loginAppUser = appUserService.getLoginAppUser(user);
        return ResponseData.success(loginAppUser);
    }
    

    //@PreAuthorize("hasAuthority('user:get/users/{id}')")
    @GetMapping("/{id}")
    @ApiOperation(value = "根据用户ID查询用户信息")
    public SysUser findUserById(@PathVariable Long id) {
        return appUserService.getById(id);
    }


    //@PreAuthorize("hasAuthority('user:put/users')")
    @PutMapping("/update")
    @ApiOperation(value = "更新用户信息")
    public void updateSysUser(@RequestBody SysUser sysUser) {
        appUserService.updateById(sysUser);
    }

    /**
     * 管理后台给用户分配角色
     *
     * @param id
     * @param roleIds
     */
    //@PreAuthorize("hasAuthority('user:post/users/{id}/roles')")
    @PostMapping("/users/{id}/roles")
    public void setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        appUserService.setRoleToUser(id, roleIds);
    }

    /**
     * 获取用户的角色
     *
     * @param
     * @return
     */
    //@PreAuthorize("hasAnyAuthority('user:get/users/{id}/roles')")
    @GetMapping("/{id}/roles")
    @ApiOperation(value = "获取用户的角色")
    public List<SysRole> findRolesByUserId(@PathVariable Long id) {
        return appUserService.findRolesByUserId(id);
    }


    //@PreAuthorize("hasAuthority('user:get/users')")
    @ResponseBody
    @ApiOperation(value = "分页显示用户查询列表1")
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "每页记录条数", required = true, dataType = "Integer")
    })*/
    @RequestMapping(value="/findUsers",method=RequestMethod.GET, produces = "application/json")
    public PageResult<SysUser> findUsers(@RequestParam Map<String, Object> params) {
        return appUserService.findUsers(params);
    }
    @ResponseBody
    @ApiOperation(value = "分页显示用户查询列表2")
    //@BussinessLog(value = "删除用户", key = "userId", dict = UserDict.class)
    @RequestMapping(value = "/listPage", method = RequestMethod.POST, produces = "application/json")
    public PageResultPlus<SysUser> listPage(@RequestBody PageQuery pageParam) {
        Page<SysUser> page = PageFactory.createPage(pageParam);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        //wrapper.lt("creationTime", ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        //wrapper.and(q->q.eq("status", MessageStatusEnum.WAIT_VERIFY.name()));
        // .eq("status", MessageStatusEnum.WAIT_VERIFY.name());
        Page<SysUser> pageResults = (Page<SysUser>) this.appUserService.page(page,wrapper);
        //pageResults.setTotal(pageResults.getRecords().size());
        return new PageResultPlus<>(pageResults);
    }



    @ApiOperation(value = "修改用户状态")
    @GetMapping("/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "enabled",value = "是否启用", required = true, dataType = "Boolean")
    })
    //@PreAuthorize("hasAnyAuthority('user:get/users/updateEnabled' ,'user:put/users/me')")
    public ResponseData updateEnabled(@RequestParam Map<String, Object> params){
        Long id = MapUtils.getLong(params, "id");
        if (id == 1L){
            return ResponseData.error("超级管理员不给予修改");
        }
        return appUserService.updateEnabled(params);
    }


    @PutMapping(value = "/password")
    //@PreAuthorize("hasAuthority('user:put/users/password')")
    @ApiOperation(value = "修改密码")
    public ResponseData updatePassword(@RequestBody SysUser sysUser) {
        if (StringUtils.isBlank(sysUser.getOldPassword())) {
            throw new IllegalArgumentException("旧密码不能为空");
        }

        if (StringUtils.isBlank(sysUser.getNewPassword())) {
            throw new IllegalArgumentException("新密码不能为空");
        }

        if (sysUser.getId() == 1L){
            return ResponseData.error("超级管理员不给予修改");
        }

        return appUserService.updatePassword(sysUser.getId(), sysUser.getOldPassword(), sysUser.getNewPassword());
    }

    //@PreAuthorize("hasAuthority('user:post/users/{id}/resetPassword' )")
    @PostMapping(value = "/{id}/resetPassword")
    @ApiOperation(value = "重置用户密码")
    public ResponseData resetPassword(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return ResponseData.error("超级管理员不给予修改");
        }
        appUserService.updatePassword(id, null, null);
        return ResponseData.success("重置成功");
    }



    /**
     * 新增or更新
     * @param SysUserDto
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "新增or更新")
    //@CacheEvict(value = "user", key = "#sysUser.username")
    @RequestMapping(value = { "/saveOrUpdate" }, method = { RequestMethod.POST }, produces="application/json;charset=UTF-8")
    public ResponseData saveOrUpdate(@RequestBody @Valid SysUserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            String message = bindingResult.getFieldError().getDefaultMessage();
            //自定义的返回，并将错误信息返回
            return ResponseData.error(message);
            /*//*List<ObjectError> errorList = result.getAllErrors();
            for(ObjectError error : errorList){
                System.out.println(error.getDefaultMessage());
            }
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);*/
        }
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(userDto, entity);
        return appUserService.saveOrUpdateUser(entity);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @DeleteMapping(value = "/users/{id}")
    public ResponseData delete(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return ResponseData.error("超级管理员不给予修改");
        }
        appUserService.delUser(id);
        return ResponseData.success("删除成功");
    }

    /**
     * 导出excel
     *
     * @return
    @PostMapping("/users/export")
    public void exportUser(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        List<SysUserExcel> result = appUserService.findAllUsers(params);
        //导出操作
        ExcelUtil.exportExcel(result, null, "用户", SysUserExcel.class, "user", response);
    }

    @PostMapping(value = "/users/import")
    public ResponseData importExcl(@RequestParam("file") MultipartFile excl) throws Exception {
        int rowNum = 0;
        if(!excl.isEmpty()) {
            List<SysUserExcel> list = ExcelUtil.importExcel(excl, 0, 1, SysUserExcel.class);
            rowNum = list.size();
            if (rowNum > 0) {
                List<SysUser> users = new ArrayList<>(rowNum);
                list.forEach(u -> {
                    SysUser user = new SysUser();
                    BeanUtil.copyProperties(u, user);
                    user.setPassword(CommonConstant.DEF_USER_PASSWORD);
                    user.setType(UserType.BACKEND.name());
                    users.add(user);
                });
                appUserService.saveUsers(users);
            }
        }
        return ResponseData.succeed("导入数据成功，一共【"+rowNum+"】行");
    }*/

    /**
     * 是否超级管理员
     */
    private boolean checkAdmin(long id) {
        return id == 1L;
    }

}

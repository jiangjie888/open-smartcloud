package com.central.oauth.modular.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.core.model.page.PageResult;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.SysPermission;
import com.central.oauth.modular.dto.ClientDto;
import com.central.oauth.modular.model.Client;
import com.central.oauth.modular.service.IClientService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "客户端应用")
@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @GetMapping
    @ApiOperation(value = "分页显示应用列表")
    public PageResult<Client> list(@RequestParam Map<String, Object> params) {
        return clientService.listClient(params, true);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取应用")
    public Client get(@PathVariable Long id) {
        return clientService.getById(id);
    }

    @GetMapping("/all")
    @ApiOperation(value = "所有应用")
    public List<Client> allClient() {
        PageResult<Client> page = clientService.listClient(Maps.newHashMap(), false);
        return page.getData();
    }

    @ApiOperation(value = "所有PC端应用")
    @RequestMapping(value="/listpc",method=RequestMethod.GET)
    public ResponseData allPcClient() {
        List<Client> output;
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("type",1);
        output = clientService.list(wrapper);
        return ResponseData.success(output);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除应用")
    public void delete(@PathVariable Long id) {
        clientService.delClient(id);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或者修改应用")
    public ResponseData saveOrUpdate(@RequestBody ClientDto clientDto) {
        return clientService.saveClient(clientDto);
        //return  clientService.saveOrUpdate(clientDto);
    }
}

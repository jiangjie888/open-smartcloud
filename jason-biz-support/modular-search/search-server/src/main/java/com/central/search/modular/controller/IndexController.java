package com.central.search.modular.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.model.page.PageResult;
import com.central.core.model.page.PageResultPlus;
import com.central.core.model.reqres.response.ResponseData;
import com.central.search.core.properties.IndexProperties;
import com.central.search.modular.dto.IndexDto;
import com.central.search.modular.model.IndexVo;
import com.central.search.modular.service.IIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 索引管理
 */
@Slf4j
@RestController
@Api(tags = "索引管理api")
@RequestMapping("/admin")
public class IndexController {
    @Autowired
    private IIndexService indexService;

    @Autowired
    private IndexProperties indexProperties;

    @PostMapping("/index/add")
    @ApiOperation(value = "创建索引")
    public ResponseData createIndex(@RequestBody IndexDto indexDto) {
        if (indexDto.getNumberOfShards() == null) {
            indexDto.setNumberOfShards(1);
        }
        if (indexDto.getNumberOfReplicas() == null) {
            indexDto.setNumberOfReplicas(0);
        }
        indexService.create(indexDto);
        return ResponseData.success("操作成功");
    }

    /**
     * 索引列表
     */
    @ApiOperation(value = "索引列表")
    @GetMapping("/indices")
    public PageResult<IndexVo> list(@RequestParam(required = false) String queryStr) {
        /*Page<SysUser> page = PageFactory.createPage(pageParam);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        //wrapper.lt("creationTime", ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        //wrapper.and(q->q.eq("status", MessageStatusEnum.WAIT_VERIFY.name()));
        // .eq("status", MessageStatusEnum.WAIT_VERIFY.name());
        Page<SysUser> pageResults = (Page<SysUser>) this.appUserService.page(page,wrapper);
        //pageResults.setTotal(pageResults.getRecords().size());
        return new PageResultPlus<>(pageResults);*/
        return indexService.list(queryStr, indexProperties.getShow());
    }
    /**
     * 索引明细
     */
    @ApiOperation(value = "索引明细")
    @GetMapping("/index/{indexName}")
    public ResponseData showIndex(@PathVariable String indexName) {
        Map<String, Object> result = indexService.show(indexName);
        return ResponseData.success(result);
    }

    /**
     * 删除索引
     */
    @ApiOperation(value = "删除索引")
    @DeleteMapping("/index/{indexName}")
    public ResponseData deleteIndex(@PathVariable String indexName) {
        indexService.delete(indexName);
        return ResponseData.success("操作成功");
    }
}

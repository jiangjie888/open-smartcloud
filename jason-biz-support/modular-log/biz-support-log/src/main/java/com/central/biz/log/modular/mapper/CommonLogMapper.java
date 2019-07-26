package com.central.biz.log.modular.mapper;


import cn.stylefeng.roses.biz.log.api.entity.CommonLog;
import cn.stylefeng.roses.biz.log.modular.model.CommonLogCondition;
import cn.stylefeng.roses.biz.log.modular.model.CommonLogParams;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * Mapper 接口
 */
public interface CommonLogMapper extends BaseMapper<CommonLog> {

    /**
     * 获取常规日志总数量
     */
    Long getCommonLogCount();

    /**
     * 获取常规日志列表
     */
    List<CommonLog> getCommonLogList(CommonLogParams commonLogParams);

    /**
     * 获取常规日志列表(条件查询)
     */
    List<CommonLog> getCommonLogListByCondition(CommonLogCondition commonLogCondition);

    /**
     * 获取常规日志列表(条件查询和分页)
     */
    List<CommonLog> getCommonLogListPageByCondition(Page<CommonLog> page, CommonLogCondition commonLogCondition);

}

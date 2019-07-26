package com.central.biz.log.modular.service;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 */
@Service
public class CommonLogService extends ServiceImpl<CommonLogMapper, CommonLog> {

    /**
     * 获取普通日志列表（没有查询条件的）
     */
    public PageResult<CommonLog> getCommonLogList(CommonLogParams commonLogParams) {
        Long commonLogCount = this.baseMapper.getCommonLogCount();

        if (commonLogParams.getGtValue() == null) {
            commonLogParams.setGtValue(commonLogCount);
        }

        List<CommonLog> commonLogList = this.baseMapper.getCommonLogList(commonLogParams);
        return CommonLogFactory.getResponse(commonLogList, commonLogCount, commonLogParams);
    }

    /**
     * 获取普通日志列表（带查询条件的）
     */
    public PageResult<CommonLog> getCommonLogListByCondition(CommonLogCondition commonLogCondition) {
        List<CommonLog> commonLogList = this.baseMapper.getCommonLogListByCondition(commonLogCondition);
        return CommonLogFactory.getResponseCondition(commonLogList, commonLogCondition);
    }

}

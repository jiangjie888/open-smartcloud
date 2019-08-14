package com.central.biz.log.modular.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.biz.log.api.entity.CommonLog;
import com.central.biz.log.modular.dao.CommonLogMapper;
import com.central.biz.log.modular.factory.CommonLogFactory;
import com.central.biz.log.modular.model.CommonLogCondition;
import com.central.biz.log.modular.model.CommonLogParams;
import com.central.core.model.page.PageResultPlus;
import com.central.db.annotation.DataSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 */
@Service
@DataSource(name = "log")
public class CommonLogService extends ServiceImpl<CommonLogMapper, CommonLog> {

    /**
     * 获取普通日志列表（没有查询条件的）
     */

    public PageResultPlus<CommonLog> getCommonLogList(CommonLogParams commonLogParams) {
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

    public PageResultPlus<CommonLog> getCommonLogListByCondition(CommonLogCondition commonLogCondition) {
        List<CommonLog> commonLogList = this.baseMapper.getCommonLogListByCondition(commonLogCondition);
        return CommonLogFactory.getResponseCondition(commonLogList, commonLogCondition);
    }
    public List<CommonLog> getList() {
        return  this.baseMapper.selectList(null);
    }

}

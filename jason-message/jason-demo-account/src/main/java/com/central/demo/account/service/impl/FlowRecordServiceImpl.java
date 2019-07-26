package com.central.demo.account.service.impl;


import com.central.core.model.exception.RequestEmptyException;
import com.central.core.model.exception.ServiceException;
import com.central.core.model.exception.enums.CoreExceptionEnum;
import com.central.core.utils.RandomUtil;
import com.central.core.utils.ToolUtil;
import com.central.demo.account.consumer.MessageServiceConsumer;
import com.central.demo.account.dao.FlowRecordDao;
import com.central.demo.account.service.IFlowRecordService;
import com.central.demo.api.account.model.FlowRecord;
import com.central.demo.api.order.GoodsFlowParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流水记录 服务实现类
 * </p>
 */
@Service
public class FlowRecordServiceImpl implements IFlowRecordService {


    @Autowired
    private FlowRecordDao flowRecordDao;

    @Autowired
    private MessageServiceConsumer messageServiceConsumer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordFlow(GoodsFlowParam goodsFlowParam) {

        if (goodsFlowParam == null) {
            throw new RequestEmptyException();
        }

        if (ToolUtil.isOneEmpty(goodsFlowParam.getUserId(), goodsFlowParam.getGoodsName(), goodsFlowParam.getSum())) {
            throw new RequestEmptyException();
        }

        //幂等判断
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId",goodsFlowParam.getId());

        List<FlowRecord> flowRecords = this.findList(params);
        if (flowRecords != null && !flowRecords.isEmpty()) {
            return;
        }

        FlowRecord flowRecord = new FlowRecord();
        flowRecord.setUserId(goodsFlowParam.getUserId());
        flowRecord.setSum(goodsFlowParam.getSum());
        flowRecord.setOrderId(goodsFlowParam.getId());
        flowRecord.setName(goodsFlowParam.getGoodsName());
        flowRecord.setCreationTime(new Date());
        flowRecord.setCreatorUserId(goodsFlowParam.getUserId());

        this.save(flowRecord);

        //测试可靠消息机制（结果百分之50几率成功）(如果此处有异常，会被roses-message-checker轮询处理，如果此处连续出现错误6次则可以通过查看消息表，人工干预)
        /*int random = RandomUtil.randomInt(100);
        if (random > 50) {
            throw new ServiceException(CoreExceptionEnum.SERVICE_ERROR);
        }*/

        //插入成功后要删除消息
        messageServiceConsumer.deleteMessageByBizId(flowRecord.getOrderId());

        String result="sdfdskafjldkasfjlda";
    }

    @Override
    public int save(FlowRecord flowRecord){
        return flowRecordDao.save(flowRecord);
    }

    @Override
    public int update(FlowRecord flowRecord){
        return flowRecordDao.update(flowRecord);
    }

    /*@Override
    public int delete(Map<String, Object> params){
        return flowRecordDao.save(flowRecord);
    }*/

    @Override
    public FlowRecord findById(Long id){
        return flowRecordDao.findById(id);
    }

    @Override
    public List<FlowRecord> findList(Map<String, Object> params){
        return flowRecordDao.findList(params);
    }
}

package com.central.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.core.model.page.PageResult;
import com.central.message.api.model.ReliableMessage;
import com.central.message.dao.ReliableMessageDao;
import com.central.message.service.IReliableMessageService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class ReliableMessageServiceImpl extends ServiceImpl<ReliableMessageDao, ReliableMessage> implements IReliableMessageService {

    //@Autowired
    //private ReliableMessageDao reliableMessageDao;


    /*@Override
    public boolean save(ReliableMessage reliableMessage) {
        return this.save(reliableMessage);
    }

    @Override
    public int update(ReliableMessage reliableMessage) {
        return this.update(reliableMessage);
    }

    @Override
    public int delete(Map<String, Object> params){
        return this.delete(params);
    }*/

    @Override
    public ReliableMessage findById(Long id) {
        return this.findById(id);
    }

    /*@Override
    public List<ReliableMessage> findList(Map<String, Object> params) {
        return this.findList(params);
    }*/

    @Override
    public PageResult<ReliableMessage> findList(Map<String, Object> params) {
        Page<ReliableMessage> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<ReliableMessage> list = baseMapper.findList(page, params);
        long total = page.getTotal();
        return PageResult.<ReliableMessage>builder().data(list).code(0).count(total).build();
    }

}

package com.central.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.message.api.model.ReliableMessage;
import com.central.message.dao.ReliableMessageDao;
import com.central.message.service.IReliableMessageService;
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

    @Override
    public List<ReliableMessage> findList(Map<String, Object> params) {
        return this.findList(params);
    }
}

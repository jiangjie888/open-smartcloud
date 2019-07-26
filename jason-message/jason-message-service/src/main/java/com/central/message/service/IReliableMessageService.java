package com.central.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.central.message.api.model.ReliableMessage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IReliableMessageService extends IService<ReliableMessage> {

    /*boolean save(ReliableMessage reliableMessage);

    int update(ReliableMessage reliableMessage);

    int delete(Map<String, Object> params);*/

    ReliableMessage findById(Long id);

    List<ReliableMessage> findList(Map<String, Object> params);
}

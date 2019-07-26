package com.smartcloud.logger.service;

import com.smartcloud.logger.entity.SendingCommonLog;
import com.smartcloud.logger.entity.SendingTCLog;
import com.smartcloud.logger.entity.SendingTraceLog;

/**
 * 发送日志到消息队列的接口类
 */
public interface LogProducerService {

    /**
     * 发送日志
     */
    void sendMsg(SendingCommonLog log);

    /**
     * 发送trace日志
     */
    void sendTraceMsg(SendingTraceLog sendingTraceLog);

    /**
     * 发送接口调用时间日志
     */
    void sendTcMsg(SendingTCLog sendingTCLog);

}

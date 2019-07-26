package com.central.message.api;

import com.central.core.model.page.PageQuery;
import com.central.core.model.page.PageResult;
import com.central.core.model.page.PageResultPlus;
import com.central.message.api.model.ReliableMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 消息服务的接口
 */
@RequestMapping("/api/messageService")
public interface MessageServiceApi {

    /**
     * 预存储消息
     */
    @RequestMapping(value = "/preSaveMessage", method = RequestMethod.POST)
    ReliableMessage preSaveMessage(@RequestBody ReliableMessage reliableMessage);

    /**
     * 确认并发送消息
     */
    @RequestMapping(value="/confirmAndSendMessage", method = RequestMethod.POST)
    void confirmAndSendMessage(@RequestParam("messageId") String messageId);

    /**
     * 存储并发送消息
     */
    @RequestMapping(value = "/saveAndSendMessage", method = RequestMethod.POST)
    void saveAndSendMessage(@RequestBody ReliableMessage reliableMessage);

    /**
     * 直接发送消息
     */
    @RequestMapping(value = "/directSendMessage", method = RequestMethod.POST)
    void directSendMessage(@RequestBody ReliableMessage reliableMessage);

    /**
     * 重发消息
     */
    @RequestMapping(value = "/reSendMessage", method = RequestMethod.POST)
    void reSendMessage(@RequestBody ReliableMessage reliableMessage);

    /**
     * 根据messageId重发某条消息
     */
    @RequestMapping(value = "/reSendMessageByMessageId", method = RequestMethod.POST)
    void reSendMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 将消息标记为死亡消息
     */
    @RequestMapping(value = "/setMessageToAreadlyDead", method = RequestMethod.POST)
    void setMessageToAreadlyDead(@RequestParam("messageId") String messageId);

    /**
     * 根据消息ID获取消息
     */
    @RequestMapping(value = "/getMessageByMessageId", method = RequestMethod.POST)
    ReliableMessage getMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 根据消息ID删除消息
     */
    @RequestMapping(value = "/deleteMessageByMessageId", method = RequestMethod.POST)
    void deleteMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 根据业务id删除消息
     */
    @RequestMapping(value = "/deleteMessageByBizId", method = RequestMethod.POST)
    void deleteMessageByBizId(@RequestParam("bizId") Long bizId);

    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    @RequestMapping(value = "/reSendAllDeadMessageByQueueName", method = RequestMethod.POST)
    void reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName);

    /**
     * 分页获取待发送超时的数据
     */
    @RequestMapping(value = "/listPagetWaitConfimTimeOutMessages", method = RequestMethod.POST)
    PageResult<ReliableMessage> listPagetWaitConfimTimeOutMessages(@RequestBody Map<String, Object> params);

    /**
     * 分页获取发送中超时的数据
     */
    @RequestMapping(value = "/listPageSendingTimeOutMessages", method = RequestMethod.POST)
    PageResult<ReliableMessage> listPageSendingTimeOutMessages(@RequestParam Map<String, Object> params);



    @RequestMapping(value = "/listPagetWaitConfimTimeOutMessagesPlus", method = RequestMethod.POST)
    PageResultPlus<ReliableMessage> listPagetWaitConfimTimeOutMessagesPlus(@RequestBody PageQuery pageParam);

}

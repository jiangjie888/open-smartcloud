package com.central.message.provider;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.model.enums.YesOrNotEnum;
import com.central.core.model.exception.RequestEmptyException;
import com.central.core.model.exception.ServiceException;
import com.central.core.model.page.PageQuery;
import com.central.core.model.page.PageResult;
import com.central.core.model.page.PageResultPlus;
import com.central.core.utils.PageFactory;
import com.central.core.utils.ToolUtil;
import com.central.message.api.MessageServiceApi;
import com.central.message.api.enums.MessageStatusEnum;
import com.central.message.api.exception.MessageExceptionEnum;
import com.central.message.api.model.ReliableMessage;
import com.central.message.config.properteis.MessageProperties;
import com.central.message.core.activemq.MessageSender;
import com.central.message.service.IReliableMessageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息服务提供接口的实现
 */

@Api(tags = "消息模块")
@RestController
@Slf4j
public class MessageServiceImpl implements MessageServiceApi {

    @Autowired
    private IReliableMessageService reliableMessageService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private MessageProperties messageProperties;

    @Override
    public ReliableMessage preSaveMessage(@RequestBody ReliableMessage reliableMessage) {

        //检查消息数据的完整性
        this.checkEmptyMessage(reliableMessage);

        //设置状态为待确认
        reliableMessage.setStatus(MessageStatusEnum.WAIT_VERIFY.name());

        //标记未死亡
        reliableMessage.setAlreadyDead(YesOrNotEnum.N.name());
        reliableMessage.setMessageSendTimes(0);
        reliableMessage.setLastModificationTime(new Date());
        reliableMessageService.save(reliableMessage);
        return reliableMessage;
    }

    @Override
    public void confirmAndSendMessage(@RequestParam("messageId") String messageId) {
        //EntityWrapper<ReliableMessage> wrapper = new EntityWrapper<>();
        //wrapper.eq("message_id", messageId);
        Map<String, Object> params = new HashMap<String, Object>();
        params .put("searchKey","messageId");
        params .put("searchValue",messageId);

        ReliableMessage reliableMessage = reliableMessageService.findList(params).get(0);

        if (reliableMessage == null) {
            throw new ServiceException(MessageExceptionEnum.CANT_FIND_MESSAGE);
        }

        reliableMessage.setStatus(MessageStatusEnum.SENDING.name());
        reliableMessage.setLastModificationTime(new Date());
        reliableMessageService.updateById(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public void saveAndSendMessage(@RequestBody ReliableMessage reliableMessage) {

        //检查消息数据的完整性
        this.checkEmptyMessage(reliableMessage);

        reliableMessage.setStatus(MessageStatusEnum.SENDING.name());
        reliableMessage.setAlreadyDead(YesOrNotEnum.N.name());
        reliableMessage.setMessageSendTimes(0);
        reliableMessage.setLastModificationTime(new Date());
        reliableMessageService.save(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public void directSendMessage(@RequestBody ReliableMessage reliableMessage) {

        //检查消息数据的完整性
        this.checkEmptyMessage(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public void reSendMessage(@RequestBody ReliableMessage reliableMessage) {

        //检查消息数据的完整性
        this.checkEmptyMessage(reliableMessage);

        //更新消息发送次数
        reliableMessage.setMessageSendTimes(reliableMessage.getMessageSendTimes() + 1);
        reliableMessage.setLastModificationTime(new Date());
        reliableMessageService.updateById(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public void reSendMessageByMessageId(@RequestParam("messageId") String messageId) {

        if (ToolUtil.isEmpty(messageId)) {
            throw new RequestEmptyException();
        }

        ReliableMessage reliableMessage = getMessageByMessageId(messageId);
        reliableMessage.setMessageSendTimes(reliableMessage.getMessageSendTimes() + 1);
        reliableMessage.setLastModificationTime(new Date());
        reliableMessageService.updateById(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public void setMessageToAreadlyDead(@RequestParam("messageId") String messageId) {

        if (ToolUtil.isEmpty(messageId)) {
            throw new RequestEmptyException();
        }

        ReliableMessage reliableMessage = this.getMessageByMessageId(messageId);
        reliableMessage.setAlreadyDead(YesOrNotEnum.Y.name());
        reliableMessage.setLastModificationTime(new Date());

        reliableMessage.updateById();
        //reliableMessageService.updateById(reliableMessage);

        //发送消息
        messageSender.sendMessage(reliableMessage);
    }

    @Override
    public ReliableMessage getMessageByMessageId(@RequestParam("messageId") String messageId) {

        if (ToolUtil.isEmpty(messageId)) {
            throw new RequestEmptyException();
        }

        //EntityWrapper<ReliableMessage> wrapper = new EntityWrapper<>();
        //wrapper.eq("message_id", messageId);

        Map<String, Object> params = new HashMap<String, Object>();
        params .put("searchKey","messageId");
        params .put("searchValue",messageId);

        List<ReliableMessage> reliableMessages = this.reliableMessageService.findList(params);
        if (reliableMessages == null || reliableMessages.size() == 0) {
            throw new ServiceException(MessageExceptionEnum.CANT_FIND_MESSAGE);
        } else {
            if (reliableMessages.size() > 1) {
                log.error("消息记录出现错误数据！消息id：" + messageId);
                throw new ServiceException(MessageExceptionEnum.MESSAGE_NUMBER_WRONG);
            } else {
                return reliableMessages.get(0);
            }
        }
    }

    @Override
    public void deleteMessageByMessageId(@RequestParam("messageId") String messageId) {

        if (ToolUtil.isEmpty(messageId)) {
            throw new RequestEmptyException();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params .put("messageId",messageId);
        /*QueryWrapper<ReliableMessage> wrapper = new QueryWrapper<>();
        wrapper.eq("message_id", messageId);*/
        this.reliableMessageService.removeByMap(params);
    }

    @Override
    public void deleteMessageByBizId(@RequestParam("bizId") Long bizId) {
        if (ToolUtil.isEmpty(bizId)) {
            throw new RequestEmptyException();
        }
        //EntityWrapper<ReliableMessage> wrapper = new EntityWrapper<>();
        //wrapper.eq("biz_unique_id", bizId);

        Map<String, Object> params = new HashMap<String, Object>();
        params .put("bizUniqueId", bizId);
        this.reliableMessageService.removeByMap(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName) {

        //默认分页大小为1000
        Integer pageSize = 1000;
        Integer pageNo = 1;
        PageHelper.startPage(pageNo,pageSize,true);

        //存放查询到的所有死亡消息
        Map<String, ReliableMessage> resultMap = new HashMap<>();

        //循环查询所有结构（分页）
        Map<String, Object> params = new HashMap<>();
        params.put("page",pageNo);
        params.put("limit",pageSize);
        List<ReliableMessage> list =  reliableMessageService.findList(params);
        PageInfo<ReliableMessage> pageInfo = new PageInfo(list);
        PageResult<ReliableMessage> pageResult = PageResult.<ReliableMessage>builder().data(pageInfo.getList()).code(200).count(pageInfo.getTotal()).build();

        if (pageResult == null) {
            return;
        }

        List<ReliableMessage> records = pageResult.getData();
        if (records == null || records.isEmpty()) {
            return;
        }

        //把结果放入集合
        for (ReliableMessage record : records) {
            resultMap.put(record.getMessageId(), record);
        }

        //循环查出剩下的还有多少,并且都放入集合
        long pages = 0L;
        if (pageResult.getCount() == 0L) {
            pages = 0L;
        } else {
            pages = pageResult.getCount() / pageSize;
            if (pageResult.getCount() % pageSize != 0L) {
                ++pages;
            }
        }

        for (pageNo = 2; pageNo <= pages; pageNo++) {
            PageHelper.startPage(pageNo,pageSize,true);
            Map<String, Object> secondparams = new HashMap<>();
            params.put("page",pageNo);
            params.put("limit",pageSize);
            List<ReliableMessage> list2 =  reliableMessageService.findList(secondparams);
            PageInfo<ReliableMessage> secondPageInfo = new PageInfo(list2);
            PageResult<ReliableMessage> secondPageResult = PageResult.<ReliableMessage>builder().data(secondPageInfo.getList()).code(200).count(secondPageInfo.getTotal()).build();


            if (secondPageResult == null) {
                break;
            }

            List<ReliableMessage> secondRecords = secondPageResult.getData();
            if (secondRecords == null || secondRecords.isEmpty()) {
                break;
            }

            for (ReliableMessage record : records) {
                resultMap.put(record.getMessageId(), record);
            }
        }

        //重新发送死亡消息
        for (ReliableMessage reliableMessage : resultMap.values()) {
            reliableMessage.setLastModificationTime(new Date());
            reliableMessage.setMessageSendTimes(reliableMessage.getMessageSendTimes() + 1);
            this.reliableMessageService.updateById(reliableMessage);

            this.messageSender.sendMessage(reliableMessage);
        }
    }

    @Override
    public PageResult<ReliableMessage> listPagetWaitConfimTimeOutMessages(@RequestBody Map<String, Object> params) {
        if (MapUtils.getInteger(params, "page")!=null && MapUtils.getInteger(params, "limit")!=null)
            PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);

        params.put("creationTime",ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        params.put("status",MessageStatusEnum.WAIT_VERIFY.name());

        List<ReliableMessage> list =  reliableMessageService.findList(params);
        PageInfo<ReliableMessage> pageInfo = new PageInfo(list);
        return PageResult.<ReliableMessage>builder().data(pageInfo.getList()).code(200).count(pageInfo.getTotal()).build();
    }

    @Override
    public PageResult<ReliableMessage> listPageSendingTimeOutMessages(@RequestParam Map<String, Object> params) {

        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        if (MapUtils.getInteger(params, "page")!=null && MapUtils.getInteger(params, "limit")!=null)
            PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);

        //Map<String, Object> params = new HashMap<String, Object>();
        params.put("creationTime",ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        params.put("status",MessageStatusEnum.SENDING.name());
        params.put("alreadyDead",YesOrNotEnum.N.name());

        List<ReliableMessage> list =  reliableMessageService.findList(params);
        PageInfo<ReliableMessage> pageInfo = new PageInfo(list);
        return PageResult.<ReliableMessage>builder().data(pageInfo.getList()).code(200).count(pageInfo.getTotal()).build();

    }

    @Override
    public PageResultPlus<ReliableMessage> listPagetWaitConfimTimeOutMessagesPlus(@RequestBody PageQuery pageParam) {
        Page<ReliableMessage> page = PageFactory.createPage(pageParam);
        QueryWrapper<ReliableMessage> wrapper = new QueryWrapper<>();
        wrapper.lt("creationTime", ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        wrapper.and(q->q.eq("status", MessageStatusEnum.WAIT_VERIFY.name()));
               // .eq("status", MessageStatusEnum.WAIT_VERIFY.name());
        Page<ReliableMessage> reliableMessagePage = (Page<ReliableMessage>) this.reliableMessageService.page(page,wrapper);
        return new PageResultPlus<>(reliableMessagePage);
    }

    /**
     * 检查消息参数是否为空
     */
    private void checkEmptyMessage(ReliableMessage reliableMessage) {
        if (reliableMessage == null) {
            throw new RequestEmptyException();
        }
        if (ToolUtil.isEmpty(reliableMessage.getMessageId())) {
            throw new ServiceException(MessageExceptionEnum.MESSAGE_ID_CANT_EMPTY);
        }
        if (ToolUtil.isEmpty(reliableMessage.getMessageBody())) {
            throw new ServiceException(MessageExceptionEnum.MESSAGE_BODY_CANT_EMPTY);
        }
        if (ToolUtil.isEmpty(reliableMessage.getConsumerQueue())) {
            throw new ServiceException(MessageExceptionEnum.QUEUE_CANT_EMPTY);
        }
    }

}

package com.central.message.checker.service;

import com.central.core.model.page.PageResult;
import com.central.message.api.model.ReliableMessage;
import com.central.message.checker.consumer.MessageServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象消息校验重发服务
 */

public abstract class AbstractMessageChecker {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageServiceConsumer messageServiceConsumer;

    public void checkMessages() {
        try {
            //存放查询结果
            Map<String, ReliableMessage> messageMap = new HashMap<>();

            int pageSize = 2000;            //每页条数
            int maxHandlePageCount = 3;     //一次最多处理页数
            int currentPage = 1;            //当前处理页

            Map<String, Object> params = new HashMap<>();
            params.put("page",currentPage);
            params.put("limit",pageSize);
            PageResult<ReliableMessage> pageResult = getPageResult(params);

            List<ReliableMessage> rows = pageResult.getData();
            for (ReliableMessage item : rows) {
                messageMap.put(item.getMessageId(), item);
            }

            long totalPage = 0L;
            if (pageResult.getCount() == 0L) {
                totalPage = 0L;
            } else {
                totalPage = pageResult.getCount() / pageSize;
                if (pageResult.getCount() % pageSize != 0L) {
                    ++totalPage;
                }
            }

            if (totalPage > maxHandlePageCount) {
                totalPage = maxHandlePageCount;
            }

            for (currentPage = 2; currentPage <= totalPage; currentPage++) {
                params = new HashMap<>();
                params.put("page",currentPage);
                params.put("limit",pageSize);

                pageResult = getPageResult(params);
                if (pageResult != null && pageResult.getData() != null) {
                    List<ReliableMessage> otherResults = pageResult.getData();
                    for (ReliableMessage rowItem : otherResults) {
                        messageMap.put(rowItem.getMessageId(), rowItem);
                    }
                } else {
                    break;
                }
            }

            //开始处理
            processMessage(messageMap);

        } catch (Exception e) {
            logger.error("处理待发送状态的消息异常！", e);
        }
    }

    protected abstract void processMessage(Map<String, ReliableMessage> messages);

    protected abstract PageResult<ReliableMessage> getPageResult(Map<String, Object> params);
}

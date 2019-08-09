package com.central.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;

/**
 *  注入公共字段自动填充,任选注入方式即可
 */
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    private final static String UPDATE_ID = "lastModifierUserId";
    private final static String UPDATE_TIME = "lastModifyTime";
    private final static String CREATE_ID = "creatorUserId";
    private final static String CREATE_TIME = "creationTime";
    private final static Logger logger = LoggerFactory.getLogger(DefaultMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        //logger.info("新增的时候干点不可描述的事情");
        // 更多查看源码测试用例
        System.out.println("*************************");
        System.out.println("insert fill");
        System.out.println("*************************");

        // 测试下划线
        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        Object createId = getFieldValByName(CREATE_ID, metaObject);
        if (createTime == null || createId==null) {
            Date date = new Date();
            setFieldValByName(CREATE_TIME, date, metaObject);
            setFieldValByName(CREATE_ID, 1L, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //logger.info("更新的时候干点不可描述的事情");
        //更新填充
        System.out.println("*************************");
        System.out.println("update fill");
        System.out.println("*************************");
        setFieldValByName(UPDATE_TIME,  new Timestamp(System.currentTimeMillis()),metaObject);
        setFieldValByName(UPDATE_ID, 2L, metaObject);

    }
}

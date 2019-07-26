package com.central.core.db.listener;

import com.central.core.db.DbInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 初始化 创建字典表
 */
@Slf4j
public class InitTableListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Map<String, DbInitializer> beansOfType = event.getApplicationContext().getBeansOfType(DbInitializer.class);

        for (Map.Entry<String, DbInitializer> entry : beansOfType.entrySet()) {
            DbInitializer value = entry.getValue();
            value.dbInit();
        }

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}

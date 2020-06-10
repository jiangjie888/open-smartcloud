package com.central.db.aop;

import com.central.db.annotation.JDataSource;
import com.central.db.config.util.DataSourceHolder;
import com.central.db.config.util.DataSourceKey;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 切换数据源Advice
 */
@Aspect
@Component
@Order(-1) // 保证该AOP在@Transactional之前执行
public class JDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(JDataSourceAspect.class);

    /*
    * target：类
    * annotation：表示方法
    * */
    //@Before("@annotation(ds)")
    //@Before("@within(ds) && execution(public * *(..))")
    @Before("@within(ds)")
    public void changeDataSource(JoinPoint point, JDataSource ds) throws Throwable {
        String dsId = ds.name();
        try {
            DataSourceKey dataSourceKey = DataSourceKey.valueOf(dsId);
            DataSourceHolder.setDataSourceKey(dataSourceKey);
        } catch (Exception e) {
            logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }
    }

    //@After("@annotation(ds)")
    @After("@within(ds)")
    public void restoreDataSource(JoinPoint point, JDataSource ds) {
        logger.debug("Revert DataSource : {transIdo} > {}", ds.name(), point.getSignature());
        DataSourceHolder.clearDataSourceKey();
    }

}
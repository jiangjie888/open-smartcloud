package com.smartcloud.logger.sql.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

@Slf4j
public class SqlLogFilter implements Log {

    public SqlLogFilter(String clazz) {

    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(s, e);
        SqlHolder.addSqlInfo(s);
    }

    @Override
    public void error(String s) {
        log.error(s);
        SqlHolder.addSqlInfo(s);
    }

    @Override
    public void debug(String s) {
        if (log.isDebugEnabled()) {
            log.debug(s);
        }
        SqlHolder.addSqlInfo(s);
    }

    @Override
    public void trace(String s) {
        if (log.isDebugEnabled()) {
            log.debug(s);
        }
        SqlHolder.addSqlInfo(s);
    }

    @Override
    public void warn(String s) {
        if (log.isDebugEnabled()) {
            log.warn(s);
        }
        SqlHolder.addSqlInfo(s);
    }
}

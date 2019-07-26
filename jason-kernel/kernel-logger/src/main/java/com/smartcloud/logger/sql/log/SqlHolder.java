package com.smartcloud.logger.sql.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行过程sql存放器
 */
public class SqlHolder {

    private static final ThreadLocal<List<String>> SQL_INFO_HOLDER = new ThreadLocal<>();

    public static void init() {
        SQL_INFO_HOLDER.set(new ArrayList<>());
    }

    public static void addSqlInfo(String sql) {
        List<String> strings = SQL_INFO_HOLDER.get();
        if (strings == null) {
            return;
        } else {
            strings.add(sql);
            SQL_INFO_HOLDER.set(strings);
        }
    }

    public static List<String> getSqlInfos() {
        return SQL_INFO_HOLDER.get();
    }

    public static String getSqlInfoStrings() {
        List<String> strings = SQL_INFO_HOLDER.get();
        StringBuilder stringBuffer = new StringBuilder();
        if (strings != null && !strings.isEmpty()) {
            for (String string : strings) {
                stringBuffer.append(string).append("<br/>");
            }
        }
        return stringBuffer.toString();
    }

    public static void cleanTempSqlInfos() {
        SQL_INFO_HOLDER.remove();
    }

}

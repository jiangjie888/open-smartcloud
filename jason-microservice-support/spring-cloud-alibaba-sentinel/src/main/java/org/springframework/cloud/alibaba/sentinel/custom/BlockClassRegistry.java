//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.custom;

import com.alibaba.csp.sentinel.util.StringUtil;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class BlockClassRegistry {
    private static final Map<String, Method> FALLBACK_MAP = new ConcurrentHashMap();
    private static final Map<String, Method> BLOCK_HANDLER_MAP = new ConcurrentHashMap();

    BlockClassRegistry() {
    }

    static Method lookupFallback(Class<?> clazz, String name) {
        return (Method)FALLBACK_MAP.get(getKey(clazz, name));
    }

    static Method lookupBlockHandler(Class<?> clazz, String name) {
        return (Method)BLOCK_HANDLER_MAP.get(getKey(clazz, name));
    }

    static void updateFallbackFor(Class<?> clazz, String name, Method method) {
        if (clazz != null && !StringUtil.isBlank(name)) {
            FALLBACK_MAP.put(getKey(clazz, name), method);
        } else {
            throw new IllegalArgumentException("Bad argument");
        }
    }

    static void updateBlockHandlerFor(Class<?> clazz, String name, Method method) {
        if (clazz != null && !StringUtil.isBlank(name)) {
            BLOCK_HANDLER_MAP.put(getKey(clazz, name), method);
        } else {
            throw new IllegalArgumentException("Bad argument");
        }
    }

    private static String getKey(Class<?> clazz, String name) {
        return String.format("%s:%s", clazz.getCanonicalName(), name);
    }
}

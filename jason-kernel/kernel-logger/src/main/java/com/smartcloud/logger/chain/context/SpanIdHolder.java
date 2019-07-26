package com.smartcloud.logger.chain.context;

/**
 * 基于调用链的服务治理系统的设计（requestNo以及当前节点的spanId和parentSpanId的临时存储器）
 */
public class SpanIdHolder {

    private static final ThreadLocal<String> spanIdContext = new ThreadLocal<>();

    public static void set(String spanId) {
        spanIdContext.set(spanId);
    }

    public static String get() {
        return spanIdContext.get();
    }

    public static void remove() {
        spanIdContext.remove();
    }
}

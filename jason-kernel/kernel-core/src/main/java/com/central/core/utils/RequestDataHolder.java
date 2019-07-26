package com.central.core.utils;


import com.central.core.model.reqres.request.RequestData;

/**
 * 请求数据的临时容器
 */
public class RequestDataHolder {

    private static ThreadLocal<RequestData> holder = new ThreadLocal<>();

    public static void put(RequestData s) {
        holder.set(s);
    }

    public static RequestData get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }
}

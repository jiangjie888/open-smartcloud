package com.central.core.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.utils.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 请求数据统一转化为RequestData
 */
@Slf4j
public class RequestDataMessageConvert extends AbstractGenericHttpMessageConverter<Object> {

    public RequestDataMessageConvert() {
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        if (type instanceof Class) {
            return ((Class) type).isAssignableFrom(RequestData.class);
        } else {
            return false;
        }
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        if (type instanceof Class) {
            return ((Class) type).isAssignableFrom(RequestData.class);
        } else {
            return false;
        }
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Map.class);
    }

    @Override
    public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readMap(inputMessage);
    }

    @Override
    public void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readMap(inputMessage);
    }

    private Object readMap(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        //请求流转化为字符串
        // 把reqeust的body读取到StringBuilder
        InputStreamReader isr=new InputStreamReader(inputMessage.getBody(),"utf8");
        BufferedReader reader=new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int rd;
        while((rd = reader.read(buf)) != -1){
            sb.append(buf, 0, rd);
        }
        String requestBody = sb.toString();
        //String requestBody = IoUtil.read(inputMessage.getBody(), "UTF-8");

        //debug模式可以打出请求的原始数据
        if (log.isDebugEnabled()) {
            log.debug("接收到requestBody: " + requestBody);
        }

        HttpServletRequest request = HttpContext.getRequest();
        return getRequestData(requestBody, request);
    }

    private Object getRequestData(String requestBody, HttpServletRequest request) {
        RequestData requestData = new RequestData();
        if (request != null) {
            requestData.setIp(request.getRemoteHost());
            requestData.setUrl(request.getServletPath());
        }
        requestData.setData(clearWhiteSpace(requestBody));
        return requestData;
    }

    private JSONObject clearWhiteSpace(String requestBody) {
        JSONObject jsonObject = JSON.parseObject(requestBody);
        if (jsonObject != null) {
            Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    value = ((String) value).trim();
                    jsonObject.put(entry.getKey(), value);
                }
            }
            return jsonObject;
        } else {
            return null;
        }
    }

}


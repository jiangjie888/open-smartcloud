package com.central.biz.log.core.serilizer;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Arrays;
import java.util.Map;

/**
 * kafka序列化协议
 */
public class FastjsonKafkaDeserializer implements Deserializer<Object> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            Object result = null;
            if (data != null) {
                result = JSON.parse(data);
            }
            return result;
        } catch (Exception var4) {
            throw new SerializationException("Can't deserialize data [" + Arrays.toString(data) + "] from topic [" + topic + "]", var4);
        }
    }

    @Override
    public void close() {

    }

}

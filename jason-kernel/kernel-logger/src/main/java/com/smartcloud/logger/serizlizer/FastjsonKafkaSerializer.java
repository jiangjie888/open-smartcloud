package com.smartcloud.logger.serizlizer;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * kafka序列化协议
 */
public class FastjsonKafkaSerializer implements Serializer<Object> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            byte[] result = null;
            if (data != null) {
                result = JSON.toJSONBytes(data);
            }
            return result;
        } catch (Exception ex) {
            throw new SerializationException("Can't serialize data [" + data + "] for topic [" + topic + "]", ex);
        }
    }

    @Override
    public void close() {

    }
}

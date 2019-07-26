package com.central.redis.util;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings("unchecked")
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

	private final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public final static ParserConfig defaultRedisConfig = new ParserConfig();

	static MapDeserializer deserializer = new MapDeserializer() {
		public Map<Object, Object> createMap(Type type) {
			return Collections.unmodifiableMap(new HashMap());
		}
	};
	static {
		defaultRedisConfig.setAutoTypeSupport(true);
		defaultRedisConfig.addAccept("com.central.");
		defaultRedisConfig.addAccept("org.springframework.");
		defaultRedisConfig.putDeserializer(Collections.unmodifiableMap(new HashMap()).getClass(), deserializer);
	}



	/*CaseInsensitiveMap<String, Object> root = (CaseInsensitiveMap) JSON.parseObject("{\"val\":{}}", Map.class, defaultRedisConfig, Feature.CustomMapDeserializer);
	CaseInsensitiveMap subMap = (CaseInsensitiveMap) root.get("val");
	assertEquals(0, subMap.size());*/

	private Class<T> clazz;

	public FastJson2JsonRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		//SimplePropertyPreFilter filter = new SimplePropertyPreFilter(OAuth2Request.class, "requestParameters");
		return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
	}

	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		String str = new String(bytes, DEFAULT_CHARSET);

		return (T) JSON.parseObject(str, clazz,defaultRedisConfig);
	}

}

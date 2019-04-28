package com.tomgs.es.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public final class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	public static <T> String toJson(T obj) {
		String json = "";
		json = JSON.toJSONString(obj);
		return json;

	}
	
	/**
	 * json字符串转成对象，并且清除json字符串中字段值的前后空格
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T fromJsonAndTrimVlaue(String json, Class<T> type) {
		ExtraProcessor processor = new ExtraProcessor() {
            public void processExtra(Object object, String key, Object value) {
            	
            	Field[] fields = object.getClass().getDeclaredFields();
            	for (Field field : fields) {
            		field.setAccessible(true);
            		try {
						Object fieldValue = field.get(object);
						if (fieldValue instanceof String) {
							String str = (String)fieldValue;
							field.set(object, str.trim());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						logger.error("json processor has exception.", e);
					}
            	}
            }
        };
		
		T t = null;
		try {
			t = JSON.parseObject(json, type, processor);
		} catch (Exception e) {
			logger.error("change to Object failure", e);
			throw new RuntimeException(e);
		}
		return t;
	}

	public static <T> T fromJson(String json, Class<T> type) {
		T t = null;
		try {
			t = JSON.parseObject(json, type);
		} catch (Exception e) {
			logger.error("change to Object failure", e);
			throw new RuntimeException(e);
		}
		return t;

	}
	
	public static <T> T fromJson(String json, TypeReference<T> type) {
		T t = null;
		try {
			t = JSON.parseObject(json, type);
		} catch (Exception e) {
			logger.error("change to Object failure", e);
			throw new RuntimeException(e);
		}
		return t;
	}

	public static <T> T parseObject(byte[] data, Class<T> type) {
		T object = JSON.parseObject(data, type);
		return object;
	}

	public static <T> List<T> parseFromJson(String json, Class<T> type) {
		try {
			return JSON.parseArray(json, type);
		} catch (Exception e) {
			logger.error("parse to list failure", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * 将json转化成map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> convertJsonStrToMap(String jsonStr) {
		if (StringUtils.isEmpty(jsonStr)) {
			jsonStr = "{}";
		}

		Map<String, Object> map = JSON.parseObject(jsonStr,
				new TypeReference<Map<String, Object>>() {

				});

		return map;
	}

	public static Map<String, Object> convertObjectToMap(Object obj) {
		String jsonStr = "";
		if (obj == null) {
			jsonStr = "{}";
		} else {
			jsonStr = toJson(obj);
		}

		Map<String, Object> map = JSON.parseObject(jsonStr,
				new TypeReference<Map<String, Object>>() {
				});

		return map;
	}

	public static <T> T fromObject(Object obj, Class<T> type) {
		T t = null;
		try {
			String json = toJson(obj);
			t = JSON.parseObject(json, type);

		} catch (Exception e) {
			logger.error("change to Object failure", e);
			throw new RuntimeException(e);
		}
		return t;

	}
}

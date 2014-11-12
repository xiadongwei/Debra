/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

package debra.Framework.Transfer.JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by xiadongwei on 14-2-11.
 * 便于兼容com.alibaba.fastjosn，实际继承自com.fasterxml.jackson.databind.ObjectMapper
 * 实现ObjectMapper的Map封装
 */
public class JSONObject extends ObjectMapper implements Map<String, Object>, Cloneable, Serializable {
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	private final Map<String, Object> map;
	private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();
	;

	public static Object parseObject(String text) {
		JSONObject result = new JSONObject();
		try {
			result = jacksonObjectMapper.readValue(text, JSONObject.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject() {
		this(DEFAULT_INITIAL_CAPACITY, false);
	}

	public JSONObject(Map<String, Object> map) {
		this.map = map;
	}

	public JSONObject(boolean ordered) {
		this(DEFAULT_INITIAL_CAPACITY, ordered);
	}

	public JSONObject(int initialCapacity) {
		this(initialCapacity, false);
	}

	public JSONObject(int initialCapacity, boolean ordered) {
		if (ordered) {
			map = new LinkedHashMap<String, Object>(initialCapacity);
		} else {
			map = new HashMap<String, Object>(initialCapacity);
		}
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public Object clone() {
		return new JSONObject(new HashMap<String, Object>(map));
	}

	public boolean equals(Object obj) {
		return this.map.equals(obj);
	}

	public int hashCode() {
		return this.map.hashCode();
	}

	public String toJSONString() {
		String result = "";
		try {
			result = this.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

}

package com.acer.fms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	private static Util UTIL;
	private static ObjectMapper OBJECT_MAPPER;

	Util() {

	}

	public static Util getInstance() {
		if (UTIL == null) {
			UTIL = new Util();
		}
		return UTIL;
	}

	private static ObjectMapper getObjectMapper() {
		if (OBJECT_MAPPER == null) {
			OBJECT_MAPPER = new ObjectMapper();
		}
		return OBJECT_MAPPER;
	}

	public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) throws Exception {
		return getObjectMapper().readValue(jsonStr, valueTypeRef);
	}

	public String toJSon(Object object) throws JsonProcessingException {
		String json = getObjectMapper().writeValueAsString(object);
		return json;
	}
}

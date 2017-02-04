package fms.common.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	private static Util UTIL;
	private static ObjectMapper OBJECT_MAPPER;
	public static Calendar calendar;

	public Util() {
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

	/**
	 * 本日 00:00:00
	 *
	 * @return
	 */
	public static long getStartTimeOfDay() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println(calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 本日 23:59:59
	 *
	 * @return
	 */
	public static long getEndTimeOfDay() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println(calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * JSON String to Object
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T readValue(String jsonStr, Class<T> clazz) throws Exception {
		return getObjectMapper().readValue(jsonStr, clazz);
	}
    
	/**
	 * Object to JSON String 
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static String toJSon(Object object) throws JsonProcessingException {
		String json = getObjectMapper().writeValueAsString(object);
		return json;
	}
	
	/**
	 * JSON Array String to Collection
	 * @param jsonArrayStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
    public static <T> List<T> readValueArray(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = getObjectMapper().readValue(jsonArrayStr,
				new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(mapToPOJO(map, clazz));
        }
        return result;
    }
    public static <T> T mapToPOJO(Map<String, Object> map, Class<T> clazz) {
        return getObjectMapper().convertValue(map, clazz);
    }

}

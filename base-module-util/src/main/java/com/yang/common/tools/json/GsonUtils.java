package com.yang.common.tools.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Gson工具类
 * @author yanglei 2017年7月13日 上午11:43:20
 */
public class GsonUtils {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private GsonUtils() {
	}

	/**
	 * 转成json
	 * @param object
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:44:42
	 */
	public static String toJson(Object object) {
		String gsonString = null;
		if (gson != null) {
			gsonString = gson.toJson(object);
		}
		return gsonString;
	}

	/**
	 * 转成bean
	 * @param gsonString
	 * @param cls
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:44:25
	 */
	public static <T> T toBean(String gsonString, Class<T> cls) {
		T t = null;
		if (gson != null) {
			t = gson.fromJson(gsonString, cls);
		}
		return t;
	}

	/**
	 * 转成list
	 * @param gsonString
	 * @param cls
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:44:17
	 */
	public static <T> List<T> toList(String gsonString, Class<T> cls) {
		List<T> list = null;
		if (gson != null) {
			list = gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
		}
		return list;
	}

	/**
	 * 转成list（含有map）
	 * @param gsonString
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:43:43
	 */
	public static <T> List<Map<String, T>> toListMaps(String gsonString) {
		List<Map<String, T>> list = null;
		if (gson != null) {
			list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {}.getType());
		}
		return list;
	}

	/**
	 * 转成map
	 * @param gsonString
	 * @return
	 * @author yanglei
	 * 2017年7月14日 上午10:42:56
	 */
	public static <T> Map<String, T> toMaps(String gsonString) {
		Map<String, T> map = null;
		if (gson != null) {
			map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {}.getType());
		}
		return map;
	}

}

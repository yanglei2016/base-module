package com.yang.common.contants;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public enum CommonContants {

	/** 交易状态 0=处理中 */
	TYPE_TRADE_STATUS_0(CommonTypeContants.TYPE_TRADE_STATUS, "处理中", "0"),
	/** 交易状态 1=成功 */
	TYPE_TRADE_STATUS_1(CommonTypeContants.TYPE_TRADE_STATUS, "成功", "1"),
	/** 交易状态 2=失败 */
	TYPE_TRADE_STATUS_2(CommonTypeContants.TYPE_TRADE_STATUS, "失败", "2"),

	
	;

	//================================================================================
	// 成员变量
	private String type;
	
	private String name;

	private String code;

	private CommonContants(String type, String name, String code) {
		this.name = name;
		this.code = code;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getCode() {
		return code;
	}
	
	public static String getName(String type, String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		for (CommonContants c : CommonContants.values()) {
			if (c.type.equals(type) && c.code.equals(code)) {
				return c.name;
			}
		}
		return null;
	}

	public static String getCode(String type, String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		for (CommonContants c : CommonContants.values()) {
			if (c.type.equals(type) && c.name.equals(name)) {
				return c.code;
			}
		}
		return null;
	}

	public static List<String> getCodesByType(String type) {
		List<String> list = new ArrayList<String>();
		for (CommonContants c : CommonContants.values()) {
			if (c.type.equals(type)) {
				list.add(c.code);
			}
		}
		return list;
	}
}

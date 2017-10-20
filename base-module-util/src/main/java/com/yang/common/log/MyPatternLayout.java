package com.yang.common.log;

import ch.qos.logback.classic.PatternLayout;

/**
 * 
 * @author yanglei
 * 2017年7月24日 下午3:18:20
 */
public class MyPatternLayout extends PatternLayout {
	static {
        PatternLayout.defaultConverterMap.put("T", ThreadNumConverter.class.getName());
        PatternLayout.defaultConverterMap.put("threadNum", ThreadNumConverter.class.getName());
    }
}

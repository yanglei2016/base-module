package com.yang.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 
 * @author yanglei
 * 2017年7月24日 下午3:16:24
 */
public class ThreadNumConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent arg0) {
		return String.valueOf(Thread.currentThread().getId());
	}

}

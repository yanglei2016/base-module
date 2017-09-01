package com.yang.common.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * @author yanglei 2017年7月4日 上午9:06:14
 */
public class DateUtils {

	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	
	/** 日期格式 yyyy-MM-dd */
	public static final String DATE_FMT = "yyyy-MM-dd";
	/** 日期格式 yyyy-MM-dd HH:mm:ss */
	public static final String DATE_TIME_FMT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式 yyyyMMddHHmmss */
	public static final String DATETIME_FMT = "yyyyMMddHHmmss";

	private static final ThreadLocal<DateFormat> LOCAL_DATE_FMT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(DATE_FMT);
		}
	};
	private static final ThreadLocal<DateFormat> LOCAL_DATE_TIME_FMT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(DATE_TIME_FMT);
		}
	};
	private static final ThreadLocal<DateFormat> LOCAL_DATETIME_FMT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(DATETIME_FMT);
		}
	};

	private static DateFormat getDateFormat(){
		return LOCAL_DATE_FMT.get();
	}
	
	private static DateFormat getDateTimeFormat(){
		return LOCAL_DATE_TIME_FMT.get();
	}
	
	private static DateFormat getDateFormat(String pattern){
		DateFormat dateFormat = null;
		switch(pattern){
		case DATE_FMT :
			dateFormat = LOCAL_DATE_FMT.get();
			break;
			
		case DATE_TIME_FMT :
			dateFormat = LOCAL_DATE_TIME_FMT.get();
			break;
			
		case DATETIME_FMT :
			dateFormat = LOCAL_DATETIME_FMT.get();
			break;
			
		default :
			dateFormat = LOCAL_DATE_FMT.get();
		}
		return dateFormat;
	}

	/**
	 * yyyy-MM-dd日期字符串
	 * @author yanglei
	 * 2017年7月11日 上午9:27:44
	 */
	public static String getCurrentDate(){
		return getDateFormat().format(new Date());
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss日期字符串
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:28:36
	 */
	public static String getCurrentDateTime(){
		return getDateTimeFormat().format(new Date());
	}
	
	/**
	 * yyyy-MM-dd日期字符串
	 * @author yanglei
	 * 2017年7月11日 上午9:27:44
	 */
	public static String getDateStr(Date date){
		return getDateFormat().format(date);
	}
	
	/**
	 * 根据格式获取日期字符串
	 * @param pattern
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:28:32
	 */
	public static String getDateStr(String pattern){
		return getDateFormat(pattern).format(new Date());
	}
	
	/**
	 * yyyy-MM-dd日期字符串
	 * @author yanglei
	 * 2017年7月11日 上午9:27:44
	 */
	public static String getDateStr(Date date, String pattern){
		return getDateFormat(pattern).format(date);
	}
	
	/**
	 * 转换yyyy-MM-dd字符串为日期
	 * @param dateStr
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:43:47
	 */
	public static Date convertStrToDate(String dateStr){
		Date date = null;
		if(StringUtils.isEmpty(dateStr)){
			return date;
		}
		try {
			date = getDateFormat(DATE_FMT).parse(dateStr);
		} catch (ParseException e) {
			logger.error("DateUtils格式化日期异常", e);
		}
		return date;
	}
	
	/**
	 * 根据格式转换字符串为日期
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:43:54
	 */
	public static Date convertStrToDate(String dateStr, String pattern){
		Date date = null;
		if(StringUtils.isEmpty(dateStr)){
			return date;
		}
		try {
			date = getDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			logger.error("DateUtils格式化日期异常", e);
		}
		return date;
	}
	
	/**
	 * 转换yyyy-MM-dd HH:mm:ss字符串为日期
	 * @param dateStr
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:44:00
	 */
	public static Date convertStrToDateTime(String dateStr){
		Date date = null;
		if(StringUtils.isEmpty(dateStr)){
			return date;
		}
		try {
			date = getDateFormat(DATE_TIME_FMT).parse(dateStr);
		} catch (ParseException e) {
			logger.error("DateUtils格式化日期异常", e);
		}
		return date;
	}
	
	/**
	 * 日期计算
	 * @param date
	 * @param type
	 * @param num
	 * @return
	 * @author yanglei
	 * 2017年7月11日 上午9:54:29
	 */
	private static Date setDate(Date date, int type, int num){
		if (date == null) {
			return null;
		}
		// 初始化日历对象
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		// 根据类型添加
		switch (type) {
		case 1: // 添加年
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + num);
			break;
		case 2: // 添加月
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + num);
			break;
		case 3: // 添加日
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + num);
			break;
		case 4: // 添加时
			cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + num);
			break;
		case 5: // 添加分
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + num);
			break;
		case 6: // 添加秒
			cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + num);
			break;
		}
		return cal.getTime();
	}
	
	/**
	 * 字符串日期天数加减
	 * @param dateStr
	 * @param num
	 * @return yyyy-MM-dd格式日期字符串
	 * @author yanglei
	 * 2017年7月12日 下午3:10:59
	 */
	public static String getDateByNum(String dateStr, int num){
		return getDateStr(setDate(convertStrToDate(dateStr), 3, num));
	}
	
	/**
	 * 日期天数加减
	 * @param date
	 * @param num
	 * @return
	 * @author yanglei
	 * 2017年7月12日 下午3:11:04
	 */
	public static Date getDateByNum(Date date, int num){
		return setDate(date, 3, num);
	}
	
	public static void main(String[] args) throws InterruptedException {
//		System.out.println(getCurrentDate());
//		System.out.println(getCurrentDateTime());
//		
//		Thread.sleep(1000);
//		
//		System.out.println(getDateStr(DateUtils.DATE_FMT));
//		System.out.println(getDateStr(DateUtils.DATE_TIME_FMT));
//		System.out.println(getDateStr(DateUtils.DATETIME_FMT));
//		
//		Thread.sleep(2000);
//		
//		System.out.println(convertStrToDate("2017-07-11"));
//		System.out.println(convertStrToDateTime("2017-07-11 09:26:29"));
		
		
		
		System.out.println(getDateByNum("2017-07-11", 1));
		System.out.println(getDateByNum("2017-07-11", -1));
		
		System.out.println(getDateByNum(new Date(), 1));
		System.out.println(getDateByNum(new Date(), -1));
		
	}
	
}

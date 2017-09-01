package com.yang.common.util;

import com.yang.common.util.impl.FileUtils;
import com.yang.common.util.impl.FtpUtils;
import com.yang.common.util.impl.IOUtils;

/**
 * 
 * @author yanglei
 * 2017年7月27日 下午3:51:09
 */
public class MyTool {

	/**
	 * IO工具类
	 */
	public static final IOUtils IO = IOUtils.getSingleInstance();
	
	/**
	 * FTP工具类
	 */
	public static final FtpUtils FTP = FtpUtils.getSingleInstance();
	
	/**
	 * 文件工具类
	 */
	public static final FileUtils FILE = FileUtils.getSingleInstance();
}

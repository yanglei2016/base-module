package com.yang.common.wheel;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午04:44:57 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public interface ExpirationIntecepter<E> {
	
	public void expired(E e);
	
}

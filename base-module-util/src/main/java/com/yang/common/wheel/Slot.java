package com.yang.common.wheel;

import java.util.HashSet;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午01:39:02 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class Slot<E> {
	
	private int id;
	private HashSet<E> elements = new HashSet<E>(); 
	
	Slot(int id) {
		this.id = id;
	}
	
	public void add(E e) {
		elements.add(e);
	}
	
	public boolean remove(E e) {
		return elements.remove(e);
	}
	
	public HashSet<E> getElementsAndFlush(){
		HashSet<E> oldElements = elements;
		elements = new HashSet<E>();
		return oldElements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

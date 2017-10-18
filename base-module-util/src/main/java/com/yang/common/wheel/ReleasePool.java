package com.yang.common.wheel;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午04:48:44 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class ReleasePool<E> implements Runnable{
	
	private Wheel<E> wheel;
	
	private ArrayBlockingQueue<E> elementsQueue = new ArrayBlockingQueue<E>(100);
	
	ReleasePool(Wheel<E> wheel) {
		this.wheel = wheel;
	}

	@Override
	public void run() {
		System.out.println("release pool start...");
		while(WheelState.SHUTDOWN != wheel.getWheelState()){
			try {
				E element = elementsQueue.poll(1, TimeUnit.SECONDS);

				if(element != null){
					wheel.remove(element);
				}
				
				if(WheelState.CLOSEING == wheel.getWheelState() && !wheel.clockerAlive() && elementsQueue.size()==0)
					wheel.stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		System.err.println("release pool exsit...");
	}
	
	public void add(E e) {
		elementsQueue.offer(e);
	}
	
	public void add(Set<E> es){
		for(Iterator<E> it = es.iterator(); it.hasNext();) {
			elementsQueue.offer(it.next());
		}
	}

}

package com.yang.common.wheel;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午01:38:47 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class Clocker<E> implements Runnable{
	
	final Wheel<E> wheel;
	
	Clocker(Wheel<E> wheel) {
		this.wheel = wheel;
	}

	@Override
	public void run() {
		System.out.println("clocker start...");
		int releaseLoopCount = wheel.getSlotsCount() * 2;
		while (WheelState.SHUTDOWN != wheel.getWheelState() && releaseLoopCount>0) {
			try {
				if(WheelState.RUNNING == wheel.getWheelState())
					Thread.sleep(5000);
				
				if(WheelState.CLOSEING == wheel.getWheelState())
					releaseLoopCount--;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			
			Slot<E> slot = wheel.getExpiredSlot();
			wheel.doExpired(slot);
		}
		
		System.err.println("clocker exsit...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread.currentThread().interrupt();
	}

}

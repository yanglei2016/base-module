package com.yang.common.wheel;


/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午05:37:33 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		final Wheel<String> wheel = Wheel.getInstance(3);
		
		wheel.addExpirationIntecepter(new ExpirationIntecepter<String>() {
			
			@Override
			public void expired(String e) {
				System.err.println("release element : "+e);
				
			}
		});
		
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					wheel.add(String.valueOf(i));
					i++;
				}
			}
		});
		
		
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wheel.stop();
			}
		});
		
		Thread thread3 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("==========="+wheel.getWheelState());
				}
			}
		});
		
		thread1.start();
//		thread2.start();
		thread3.start();
		thread1.join();
//		thread2.join();
		thread3.join();
	}

}

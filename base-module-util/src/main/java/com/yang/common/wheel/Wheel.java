package com.yang.common.wheel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** 
 * =========================================================
 * 北京五八信息技术有限公司架构部
 * @author	chenyang	E-mail: chenyang@58.com
 * @version	Created ：2014-1-17 下午01:37:00 
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public final class Wheel<E> {
	
	private final AtomicInteger currentIndex = new AtomicInteger(0);
	private final Map<E, Slot<E>> elementsMap = new ConcurrentHashMap<E, Slot<E>>();
	private final ArrayList<Slot<E>> slots;
	private WheelState state = WheelState.RUNNING;
	private final AtomicBoolean shutdown = new AtomicBoolean(false);
	
	private static final int MAX_ALIVE_SECONDS = 60 * 60 * 24;	// 最大存活时间为一天，86400秒
	
	private final CopyOnWriteArrayList<ExpirationIntecepter<E>> expirationIntecepters = new CopyOnWriteArrayList<ExpirationIntecepter<E>>();
	private final Thread releasePoolThread;
	private final Thread clockerThread;
	private final ReleasePool<E> releasePool;
	
	public synchronized static <E> Wheel<E> getInstance(int liveSeconds){
		if(liveSeconds < 1 || liveSeconds > MAX_ALIVE_SECONDS)
			throw new IllegalArgumentException("Element live seconds must between 1 to 86400");
		
		Wheel<E> wheel = new Wheel<E>(liveSeconds);
		
		return wheel;
	}
	
	private Wheel(int liveSeconds) {
		// 每秒钟一个槽
		slots = new ArrayList<Slot<E>>(liveSeconds);

		for(int i=0; i<liveSeconds; i++){
			slots.add(new Slot<E>(i));
		}
		
		slots.trimToSize();
		
		clockerThread = new Thread(new Clocker<E>(this));
		clockerThread.start();
		
		releasePool = new ReleasePool<E>(this);
		releasePoolThread = new Thread(releasePool);
		releasePoolThread.start();
	}
	
	private int expiredIndex() {
		int current = currentIndex.get();
		
		if(current == slots.size()-1)
			return 0;
		
		return current + 1;
	}
	
	private void indexIncrement() {
		if(!this.currentIndex.compareAndSet(slots.size()-1, 0))
			this.currentIndex.incrementAndGet();
	}
	
	Slot<E> getExpiredSlot() {
		return slots.get(expiredIndex());
	}
	
	Slot<E> getCurrentSlot() {
		return slots.get(currentIndex.get());
	}
	
	void doExpired(Slot<E> slot) {
		HashSet<E> expiredElements = slot.getElementsAndFlush();
		indexIncrement();
		releasePool.add(expiredElements);
	}
	
	public void add(E e) {
		if(WheelState.RUNNING != this.state)
			throw new IllegalStateException("timing wheel is shuting down");
		
		synchronized (e) {
			removeExsit(e); // 把已有的元素删除掉
			
			Slot<E> slot = getCurrentSlot();
			slot.add(e);
			elementsMap.put(e, slot);
			System.out.println("add element:"+e);
		}
	}
	
	private void removeExsit(E e) {
		Slot<E> slot = elementsMap.get(e);
		if(slot != null) {
			slot.remove(e);
		}
		elementsMap.remove(e);
	}

	void remove(E e) {
		removeExsit(e);
		
		for(ExpirationIntecepter<E> listener : expirationIntecepters) {
			listener.expired(e);
		}
	}
	
	public void stop(){
		if(this.shutdown.compareAndSet(false, true)) {
			this.state = WheelState.CLOSEING;
		}else{
			this.state = WheelState.SHUTDOWN;
		}
	}
	
	public WheelState getWheelState() {
		return this.state;
	}
	
	public boolean clockerAlive() {
		return clockerThread.isAlive();
	}
	
	public int getSlotsCount() {
		return slots.size();
	}
	
	public void addExpirationIntecepter(ExpirationIntecepter<E> intecepter) {
		this.expirationIntecepters.add(intecepter);
	}
	
	public void removeExpirationIntecepter(ExpirationIntecepter<E> intecepter) {
		this.expirationIntecepters.remove(intecepter);
	}

}

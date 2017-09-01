package com.yang.common.tools.redis;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedis;

/**
 * redis锁
 * 
 * @author yanglei 2017年7月13日 下午3:14:40
 */
public class RedisLock {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String LOCAL_FLAG = "true"; // 锁标识
	/** 毫秒与毫微秒的换算单位 1毫秒 = 1000000毫微秒 */
	public static final long MILLI_NANO_CONVERSION = 1000 * 1000L;

	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 获取分布式锁
	 * 
	 * @param lockName
	 *            竞争获取锁key
	 * @param timeOut
	 *            获取锁超时时间(毫秒)
	 * @param expireTime
	 *            锁的超时时间(毫秒)
	 * @return 获取锁标识
	 */
	public boolean tryLock(String lockName, long timeOut, long expireTime) {
		ShardedJedis jedis = null;
		try {
			jedis = redisUtils.getJ();
			String lockKey = "lock:" + lockName;
			long endTime = System.nanoTime() + (timeOut * MILLI_NANO_CONVERSION);
			int lockExpire = (int) (expireTime / 1000);

			while (System.nanoTime() < endTime) {
				if (jedis.setnx(lockKey, LOCAL_FLAG) == 1) {
					jedis.expire(lockKey, lockExpire);
					return true;
				}
				try {
					Thread.sleep(10, new Random().nextInt(500)); // 随机，锁竞争更公平
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch (Exception e) {
			redisUtils.releaseJ(jedis);
			logger.info("获取锁异常：{}", e);
		} finally {
			redisUtils.releaseJ(jedis);
		}
		return false;
	}

	/**
	 * 释放锁
	 * 
	 * @param lockName
	 *            竞争获取锁key
	 * @return
	 */
	public boolean unLock(String lockName) {
		ShardedJedis jedis = null;
		String lockKey = "lock:" + lockName;
		try {
			jedis = redisUtils.getJ();
			String value = jedis.get(lockKey);
			if (StringUtils.isNotBlank(value) && LOCAL_FLAG.equals(value)) {
				jedis.del(lockKey);
				return true;
			}
		} catch (Exception e) {
			redisUtils.releaseJ(jedis);
			logger.info("释放锁异常：{}", e);
		} finally {
			redisUtils.releaseJ(jedis);
		}
		return false;
	}

}

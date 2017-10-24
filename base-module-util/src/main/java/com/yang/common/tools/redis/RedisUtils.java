package com.yang.common.tools.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.tools.json.GsonUtils;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * redis工具类
 * @author yanglei
 * 2017年7月13日 上午8:48:08
 */
public class RedisUtils {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * DEFALT_EXPIRE_TIME:默认过期时间  10days.
	 */
	//private static final int DEFALT_EXPIRE_TIME = 24 * 60 * 60 * 10;
	
	private ShardedJedisPool shardedJedisPool;
	
	/**
	 * jedis init
	 * @param shardingNodes
	 * 			"192.168.0.100:6379,192.0.101.191:6380"
	 * @param jedisPoolConfig
	 * 			jedis pool config
	 * @author yanglei
	 * 2017年7月13日 上午9:15:10
	 */
	public RedisUtils(String shardingNodes, GenericObjectPoolConfig jedisPoolConfig){
		if(StringUtils.isBlank(shardingNodes) || shardingNodes.indexOf(":") == -1){
			throw new RuntimeException("jedis init error, offered shardingNodes str illegal : "+ shardingNodes);
		}
		String[] nodesArr = shardingNodes.split(",");
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		for(String node : nodesArr){
			String[] tmp = node.split(":");
			String hosts = tmp[0];
			int port = Integer.parseInt(tmp[1]);
			JedisShardInfo shard = new JedisShardInfo(hosts,port);
			shards.add(shard);
		}
		shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shards);
		logger.info("jedis init ok, shardingNodes is : {} ", shardingNodes);
	}
	
	/**
	 * 缓存对象
	 * @param key
	 * @param object
	 * @return
	 * @author yanglei
	 * 2017年7月13日 下午2:45:43
	 */
	public boolean set(String key, Object object) {
        ShardedJedis jedis = getJ();
        try {
            jedis.set(key, GsonUtils.toJsonString(object));
            return true;
        } catch (Exception e) {
            logger.error("set error", e);
        } finally {
            releaseJ(jedis);
        }
        return false;
    }
    
	/**
	 * 缓存对象（过期时间）
	 * @param key
	 * @param object
	 * @param seconds
	 * @return
	 * @author yanglei
	 * 2017年7月13日 下午2:45:52
	 */
    public boolean set(String key, Object object, int seconds) {
        ShardedJedis jedis = getJ();
        try {
            jedis.setex(key, seconds, GsonUtils.toJsonString(object));
            return true;
        } catch (Exception e) {
            logger.error("set error", e);
        } finally {
            releaseJ(jedis);
        }
        return false;
    }

    /**
     * 获取缓存对象
     * @param key
     * @param cls
     * @return
     * @author yanglei
     * 2017年7月13日 下午2:46:20
     */
    public <T> T get(String key, Class<T> cls) {
        ShardedJedis jedis = getJ();
        try {
        	String jsonData = jedis.get(key);
        	if(StringUtils.isBlank(jsonData)){
        		return null;
        	}
            return GsonUtils.toBean(jsonData, cls);
        } catch (Exception e) {
            logger.error("get error", e);
        } finally {
            releaseJ(jedis);
        }
        return null;
    }

    /**
     * 缓存字符串
     * @param key
     * @param str
     * @return
     * @author yanglei
     * 2017年7月13日 下午2:47:01
     */
    public boolean setStr(String key, String str) {
        ShardedJedis jedis = getJ();
        try {
            jedis.set(key, str);
            return true;
        } catch (Exception e) {
            logger.error("setStr error", e);
        } finally {
            releaseJ(jedis);
        }
        return false;
    }
    
    /**
     * 缓存字符串（超时时间）
     * @param key
     * @param str
     * @param seconds
     * @return
     * @author yanglei
     * 2017年7月13日 下午2:47:14
     */
    public boolean setStr(String key, String str, int seconds) {
        ShardedJedis jedis = getJ();
        try {
            jedis.setex(key, seconds, str);
            return true;
        } catch (Exception e) {
            logger.error("setStr error", e);
        } finally {
            releaseJ(jedis);
        }
        return false;
    }

    /**
     * 获取缓存字符串
     * @param key
     * @return
     * @author yanglei
     * 2017年7月13日 下午2:47:36
     */
    public String getStr(String key) {
        ShardedJedis jedis = getJ();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("getStr error", e);
        } finally {
            releaseJ(jedis);
        }
        return null;
    }
    
    public boolean setnx(String key, String value, int seconds) {
        ShardedJedis jedis = getJ();
        try {
            if(jedis.setnx(key, value) != 1){
            	
            }
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            logger.error("set error", e);
        } finally {
            releaseJ(jedis);
        }
        return false;
    }
	
	protected ShardedJedis getJ(){
		return shardedJedisPool.getResource();
	}
	
	@SuppressWarnings("deprecation")
	protected void releaseJ(ShardedJedis jedis){
		if (jedis != null){
			shardedJedisPool.returnResourceObject(jedis);
		}
	}
}

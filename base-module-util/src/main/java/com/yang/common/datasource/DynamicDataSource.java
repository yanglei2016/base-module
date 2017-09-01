package com.yang.common.datasource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 继承{@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource} 
 * 配置主从数据源后，根据选择，返回对应的数据源。多个从库的情况下，会平均的分配从库，用于负载均衡。
 * @author yanglei
 * 2017年8月10日 上午10:16:45
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
	
	private DataSource master; //主库，只允许有一个
	private List<DataSource> slaves; // 从库，允许有多个
	private AtomicLong slaveCount = new AtomicLong();
    private int slaveSize = 0;
    
    private Map<Object, Object> dataSources = new HashMap<Object, Object>();
	
    private static final String DEFAULT = "master";
    private static final String SLAVE = "slave";
    
    private static final ThreadLocal<LinkedList<String>> dataSourceHolder = new ThreadLocal<LinkedList<String>>(){
    	protected LinkedList<String> initialValue() {
    		return new LinkedList<String>();
    	};
    };
    
    /**
     * 初始化
     * @author yanglei
     * 2017年8月23日 下午2:29:28
     */
    @Override
    public void afterPropertiesSet() {
    	if(null == master){
    		throw new IllegalArgumentException("Property 'master' is required");
    	}
    	dataSources.put(DEFAULT, master);
    	if(null != slaves && slaves.size() > 0){
    		slaveSize = slaves.size();
    		for(int i = 0; i < slaveSize; i++){
    			dataSources.put(SLAVE + (i + 1), slaves.get(i));
    		}
    	}
    	this.setDefaultTargetDataSource(master);
    	this.setTargetDataSources(dataSources);
    	super.afterPropertiesSet();
    };
    
    /**
     * 使用主库
     * @author yanglei
     * 2017年8月23日 上午11:17:43
     */
    public static void useMaster(){
    	LinkedList<String> datasource = dataSourceHolder.get();
    	if(logger.isDebugEnabled()){
    		logger.debug("use datasource : {}", datasource);
    	}
    	datasource.offerFirst(DEFAULT);
    }
    
    /**
     * 使用从库
     * @author yanglei
     * 2017年8月23日 上午11:17:59
     */
    public static void useSlave(){
    	LinkedList<String> datasource = dataSourceHolder.get();
    	if(logger.isDebugEnabled()){
    		logger.debug("use datasource : {}", datasource);
    	}
    	datasource.offerFirst(SLAVE);
    }
    
    /**
     * 重置当前栈
     * @author yanglei
     * 2017年8月23日 上午11:28:04
     */
    public static void reset(){
    	LinkedList<String> datasource = dataSourceHolder.get();
    	if(logger.isDebugEnabled()){
    		logger.debug("reset datasource : {}", datasource);
    	}
    	if(null != datasource && datasource.size() > 0){
    		datasource.poll();
    	}
    }
    
    /**
     * 如果是选择使用从库，且从库的数量大于1，则通过取模来控制从库的负载, 
     * 计算结果返回AbstractRoutingDataSource 
     * @return
     * @author yanglei
     * 2017年8月23日 下午2:29:58
     */
	@Override
	protected Object determineCurrentLookupKey() {
		LinkedList<String> datasource = dataSourceHolder.get();
		String key = datasource.peekFirst() == null ? DEFAULT : datasource.peekFirst();
		if(logger.isDebugEnabled()){
    		logger.debug("currenty datasource : {}", key);
    	}
		if(null != key){
			if(DEFAULT.equals(key)){
				return key;
			}else if(SLAVE.equals(key)){
				if(slaveSize > 1){
					long c = slaveCount.incrementAndGet();
					c = c % slaveSize;  
                    return SLAVE + (c + 1); 
				}else{
					return SLAVE + "1";
				}
			}
			return null;
		}else{
			return null;
		}
	}

	public DataSource getMaster() {
		return master;
	}

	public void setMaster(DataSource master) {
		this.master = master;
	}

	public List<DataSource> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<DataSource> slaves) {
		this.slaves = slaves;
	}
}

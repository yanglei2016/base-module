package com.yang.common.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.page.PageContext;
import com.yang.common.page.PageModel;
import com.yang.common.tools.json.GsonUtils;

/**
 * 分页拦截器
 * @author HP
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object intercept(Invocation invocation) throws Exception{
		PageModel pageModelThreadLocal = PageContext.getPageModel();
		logger.info("-----------------------------------");
		logger.info("pageModelThreadLocal：{}", GsonUtils.toJson(pageModelThreadLocal));
		try{
			StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
			BoundSql boundSql = statementHandler.getBoundSql();
			
	        PageModel pageModel = this.getPageModel(boundSql);
	        logger.info("pageModel：{}", GsonUtils.toJson(pageModel));
	        logger.info("-----------------------------------");
	        if(pageModel == null){
	        	return invocation.proceed();
	        }
	        
	        String sql = boundSql.getSql();
	        String conutSql = this.createCountSql(sql);
	        String pageSql = this.createPageSql(sql, pageModel);
	        
//	        logger.info("**********conutSql**********："+ conutSql);
//	        logger.info("**********pageSql***********："+ pageSql);
	        
	        Connection conn = (Connection) invocation.getArgs()[0];
	        PreparedStatement preStmt = conn.prepareStatement(conutSql);
	        ResultSet rs = preStmt.executeQuery();
	        if(rs.next()){
	        	pageModel.setCount(rs.getInt(1));
	        }
	        
			MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
			metaObject.setValue("delegate.boundSql.sql", pageSql);
		}catch(Exception e){
			logger.error("分页拦截器异常", e);
			throw new RuntimeException("分页拦截器异常："+ e.getMessage());
		}finally{
			PageContext.remove();
		}
		return invocation.proceed();
	}
	
	/**
	 * 获取分页对象
	 * @author yanglei
	 * 2017年6月30日 上午11:36:31
	 */
	private PageModel getPageModel(BoundSql boundSql){
		PageModel pageModel = null;
		if(boundSql.getParameterObject() instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object> params = (Map<String, Object>)boundSql.getParameterObject();
	        if(MapUtils.isNotEmpty(params)){
	        	if(params.containsKey(PageModel.PAGE_PARAMS)){
	        		pageModel = (PageModel)params.get(PageModel.PAGE_PARAMS);
	        	}
	        }
		}
        return pageModel;
	}
	
	/**
	 * 生成查询总记录sql
	 * @param sql
	 * @return
	 */
	private String createCountSql(String sql){
		String countSql = "select count(*) from ("+ sql +") a";
		return countSql;
	}
	
	/**
	 * 生成分页sql
	 * @param sql
	 * @param PageModel
	 * @return
	 */
	private String createPageSql(String sql, PageModel PageModel){
		String pageSql = sql +" limit "+ (PageModel.getCurrentPage() * PageModel.getPageSize()) +", "+ PageModel.getPageSize();
		return pageSql;
	}
	

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}

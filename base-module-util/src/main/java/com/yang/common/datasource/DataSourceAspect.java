package com.yang.common.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 有{@com.yang.common.datasource.DataSourceType}注解的方法，调用时会切换到指定的数据源
 * @author yanglei
 * 2017年8月23日 下午2:47:15
 */
@Component
@Aspect
public class DataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);
	
	@Pointcut("@annotation(com.yang.common.datasource.DataSourceType)")  
    public void dataSourceTypeAspect(){}
		
	@Around("dataSourceTypeAspect()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint){
		Object rtnVal = null;
        boolean selectedDataSource = false;
        DataSourceType dataSourceType = null;
        try {
        	dataSourceType = this.getMthodAnnotation(proceedingJoinPoint);
            if (null != dataSourceType) {
                selectedDataSource = true;
                if (dataSourceType.slave()) {
                    DynamicDataSource.useSlave();
                } else {
                    DynamicDataSource.useMaster();
                }
            }
            rtnVal = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
        	logger.warn("数据源切换错误", e);
            throw new RuntimeException("数据源切换错误", e);
        } finally {
            if (selectedDataSource) {
                DynamicDataSource.reset();
            }
        }
        return rtnVal;
	}
	
	@SuppressWarnings("rawtypes")
	private DataSourceType getMthodAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException{    
		String targetName = proceedingJoinPoint.getTarget().getClass().getName();
		String methodName = proceedingJoinPoint.getSignature().getName();
		Object[] arguments = proceedingJoinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		DataSourceType dataSourceType = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					dataSourceType = method.getAnnotation(DataSourceType.class);
					break;
				}
			}
		}
		return dataSourceType;
	}
}

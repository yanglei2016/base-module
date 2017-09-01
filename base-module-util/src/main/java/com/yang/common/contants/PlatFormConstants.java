package com.yang.common.contants;
/**
 * 框架常量类
 * @author yanglei
 * 2017年6月14日 下午5:31:34
 */
public class PlatFormConstants {
	
	/**UTF-8**/
	public final static String CHARSET_UTF8 = "UTF-8";
	
	
	/**开始**/
	public final static String MESSAGE_START = "{}..........开始..........";
	/**结束**/
	public final static String MESSAGE_END   = "{}..........结束..........";
	
    /**查询对象**/
	public final static String PARAMETER_MAP = "parameterMap"; 
	/**查询对象**/
	public final static String RESULT_LIST = "resultList"; 
    /**页面类型**/
	public final static String PAGE_TYPE = "pageType"; 
    /**页面类型：新增**/
	public final static String PAGE_TYPE_INSERT = "insert"; 
    /**页面类型：修改**/
	public final static String PAGE_TYPE_UPDATE = "update"; 
    /**页面类型：删除**/
	public final static String PAGE_TYPE_DELETE = "delete"; 
    /**页面类型：详情**/
	public final static String PAGE_TYPE_DETAIL = "detail"; 
    /**页面类型：查询**/
	public final static String PAGE_TYPE_SEARCH = "search";
	
	/**操作结果页**/
	public final static String OPERATE_RESULT = "/layout/operate_result";
	/** 返回成功标识码 **/
    public static final String REDIRECT_URL = "redirectUrl";
	
    /** 返回码标识 **/
    public static final String RESPONSE_CODE = "code";
    /** 返回结果标识 **/
    public static final String RESPONSE_MESSAGE = "message";
    /** 返回码 success=成功**/
    public static final String CODE_SUCC = "success";
    /** 返回码error=失败**/
    public static final String CODE_ERROR = "error";
    /** 返回结果标识 **/
    public static final String MESSAGE_EXCEPTION_DETAIL = "exceptionDetail";
    /** 校验结果不通过(例：越权类) */
    public static final String CODE_NOPASS = "ERROR";
    
    public static final String USER_INFO = "USER_INFO";
}

package com.yang.common.vo;

import com.yang.common.tools.json.GsonUtils;

public class ResponseVo {
	
	public static final String SUCCES_CODE = "0";
	private static final String CODE_404 = "404";
	private static final String CODE_500 = "500";
	
	private static final String DEFAULT_SUCCES_MSG = "操作成功";
	
	private static final String DEFAULT_ERROR_MSG = "操作失败！";
	
	private static final String SERVICE_NOT_FOUND = "服务未找到！";
	
	private String respCode;
	
	private String respMsg;
	
	private Object data = null;
	
	private ResponseVo() {}
	
	private ResponseVo(String respCode, String respMsg, Object data) {
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.data = data;
	}
	
	/**
	 * @return the returnCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * @param respCode the returnCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * @return the returnDesc
	 */
	public String getRespMsg() {
		return respMsg;
	}

	/**
	 * @param respMsg the returnDesc to set
	 */
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	/**
	 * @return the returnData
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the returnData to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
    public static ResponseVo.Builder builder() {
        return new Builder();
    }
    
	public static ResponseVo instance() {
		return new Builder().build();
	}
	
	public static ResponseVo ok() {
		return ResponseVo.ok(ResponseVo.DEFAULT_SUCCES_MSG, null);
	}
	
	public static ResponseVo ok(String respMsg) {
		return ResponseVo.ok(respMsg, null);
	}
	
	public static ResponseVo ok(Object data) {
		return ResponseVo.ok(ResponseVo.DEFAULT_SUCCES_MSG, data);
	}
	
	public static ResponseVo ok(String respMsg, Object data) {
		return new Builder().respCode(SUCCES_CODE).respMsg(respMsg).data(data).build();
	}
	
	public static ResponseVo err() {
		return ResponseVo.err(CODE_500, ResponseVo.DEFAULT_ERROR_MSG, null);
	}
	
	public static ResponseVo err(String respMsg) {
		return ResponseVo.err(CODE_500, respMsg, null);
	}
	
	public static ResponseVo err(String respCode, String respMsg) {
		return ResponseVo.err(respCode, respMsg, null);
	}
	
	public static ResponseVo err(String respMsg, Object data) {
		return ResponseVo.err(CODE_500, respMsg, data);
	}
	
	public static ResponseVo err(String respCode, String respMsg, Object data) {
		return new Builder().respCode(respCode).respMsg(respMsg).data(data).build();
	}
	
	public static ResponseVo notFound(){
		return new Builder().respCode(CODE_404).respMsg(ResponseVo.SERVICE_NOT_FOUND).build();
	}
	
	public static class Builder {
		
		private String respCode;
		
		private String respMsg;
		
		private Object data;
		
		Builder() {
		}
		
		public Builder respCode(String respCode) {
			this.respCode = respCode;
			return this;
		}
		public Builder respMsg(String respMsg) {
			this.respMsg = respMsg;
			return this;
		}
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		public ResponseVo build() {
			return new ResponseVo(this.respCode, this.respMsg, this.data);
		}
	}
	
	@Override
	public String toString() {
		return GsonUtils.toJsonString(this);
	}
}

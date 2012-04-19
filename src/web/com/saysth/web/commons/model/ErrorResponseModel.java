package com.saysth.web.commons.model;

import java.io.Serializable;

/**
 * @author RamosLi
 * 错误返回的对象
 */
public class ErrorResponseModel extends BaseModel implements Serializable {
	private static final long serialVersionUID = 3121416529256889938L;
	
	private String error_msg;
	public ErrorResponseModel() {
		super();
	}
	public ErrorResponseModel(int errorCode, String errorMessage) {
		this.result = errorCode;
		this.error_msg = errorMessage;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String errorMessage) {
		this.error_msg = errorMessage;
	}
}

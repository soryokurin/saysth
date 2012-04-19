package com.saysth.web.commons.model;

/**
 * @author RamosLi
 * API服务返回的最基本的model，其他model需要继承此对象
 */
public class BaseModel {
	public static final BaseModel SUCCESS_MODEL = new BaseModel();
	// 默认值为1，表示成功
	protected int result = 1;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}

package com.saysth.web.commons.model;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RamosLi
 * 参数上下文
 */
public class ParamContext {
	// 当前用户的ID
	private long userId;
	// 当前用户的User对象，可能为空
	private User user;
	
	private String clientIp;
	
	private HttpServletRequest request;
	
	public ParamContext(HttpServletRequest request) {
		this.request = request;
	}
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public User getUser() {
//		if (this.user == null && this.userId > 0) {
//			this.user = UserHome.getInstance().get(userId);
//		}
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	// IP做一个缓存，避免每次都去读取
	public String getClientIp() {
		if (this.clientIp == null) {
//			this.clientIp = IpAddressUtils.getClientIp(request);
		}
		return clientIp;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}

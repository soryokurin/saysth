package com.saysth.web.commons.model;

import java.io.Serializable;

/**
 * @author RamosLi 
 * 通行证的对象
 */
public class PassportModel extends BaseModel implements Serializable {
	private static final long serialVersionUID = -7448918584660786466L;
	
	private long user_id;
	private String access_token;
	private int expire_in;
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(int expire_in) {
		this.expire_in = expire_in;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	
}

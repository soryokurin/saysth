package com.saysth.core.exceptions;

public class ServiceException extends Exception{
	private static final long serialVersionUID = 8804481786221399009L;
	
	private int code;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public ServiceException(){
		super();
	}

	public ServiceException(String msg){
		super(msg);
	}
	
	public ServiceException(int code){
		super();
		this.code = code;
	}
	
	public ServiceException(int code,String name){
		super();
		this.code = code;
		this.name = name;
	}

}

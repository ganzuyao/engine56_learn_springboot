package com.engine56.glue.common.response;

public enum ResponseEnum {
	
	SUCCESS(0,"success"),ERROR(1,"error");
	
	private int status;
	private String message;
	
	private ResponseEnum(int status, String message) {
		this.status = status;
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

package com.ud.iot.exceptions;

public abstract class UserException extends DashboardException {

	private static final long serialVersionUID = 1L;

	public UserException(String message) {
		super(message);
	}
	
	@Override
	public String getTitle() {
		return "User error";
	}
}

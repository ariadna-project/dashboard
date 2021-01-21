package com.ud.iot.exceptions;

public abstract class DashboardException extends Exception {

	private static final long serialVersionUID = 1L;

	public DashboardException(String message) {
		super(message);
	}
	
	public String getTitle() {
		return "Dashboard exception";
	}
}

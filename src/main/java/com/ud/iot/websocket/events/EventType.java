package com.ud.iot.websocket.events;

public enum EventType {

	UPDATE_DEVICES_DATA("UPDATE_DEVICES_DATA");
	
	private String value;
	
	private EventType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
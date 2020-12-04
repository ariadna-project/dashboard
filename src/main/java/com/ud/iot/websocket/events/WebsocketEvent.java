package com.ud.iot.websocket.events;

import java.io.Serializable;

public class WebsocketEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private EventType type;
	private Object event;
	
	public WebsocketEvent() {
	}
	
	public WebsocketEvent(EventType type, Object event) {
		super();
		this.type = type;
		this.event = event;
	}

	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Object getEvent() {
		return event;
	}
	public void setEvent(Object event) {
		this.event = event;
	}
}

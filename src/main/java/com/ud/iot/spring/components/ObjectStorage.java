package com.ud.iot.spring.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

@Component
public class ObjectStorage {

	@Value("${desktop.storage:true}") private boolean desktopStorage;
	
	public void pushObject(String name, Object o) {
		if(desktopStorage) {
			Executions.getCurrent().getDesktop().setAttribute(name, o);
		} else {
			Sessions.getCurrent().setAttribute(name, o);
		}
	}
	
	public Object getObject(String name) {
		if(desktopStorage) {
			return Executions.getCurrent().getDesktop().getAttribute(name);
		} else {
			return Sessions.getCurrent().getAttribute(name);
		}
	}
	
	public Object removeObject(String name) {
		if(desktopStorage) {
			return Executions.getCurrent().getDesktop().removeAttribute(name);
		} else {
			return Sessions.getCurrent().removeAttribute(name);
		}
	}
}

package com.ud.iot.util;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;

public class ClientUtils {
	public static void sendEventToDesktop(Desktop desktop, String eventName, Object data) {
		Executions.schedule(desktop, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				EventQueues.lookup(eventName).publish(event);
			}
		}, new Event(eventName, null, data));
	}
}

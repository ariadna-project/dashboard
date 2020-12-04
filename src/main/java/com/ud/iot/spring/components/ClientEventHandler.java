package com.ud.iot.spring.components;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.iot.model.DataDevice;
import com.ud.iot.websocket.events.EventType;
import com.ud.iot.websocket.events.UpdateDataDeviceEvent;

@Component
public class ClientEventHandler {
	
	public void sendEventToClient(Set<Desktop> desktops, EventType type, String eventText) {
		//Transformar el mensaje
		Map<String, Object> eventsQueue = new Hashtable<>();

		ObjectMapper om = new ObjectMapper();
		try {
			switch (type) {
			case UPDATE_DEVICES_DATA:
				UpdateDataDeviceEvent event = om.readValue(eventText, UpdateDataDeviceEvent.class);
				for(DataDevice dd : event.getDataDevices()) {
					eventsQueue.put("updateData_"+dd.getDeviceId() + "_" + dd.getDataId(), dd);
				}
				break;
			default:
				break;
			}
		} catch(Exception e) {
			System.out.println("Error al procesar el evento");
		}
		
		for(Desktop desktop : desktops) {
			for(Entry<String, Object> e : eventsQueue.entrySet()) {
				String queue = e.getKey();
				Object event = e.getValue();
				System.out.println("Enviando evento a  " + queue);
				Executions.schedule(desktop, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						EventQueues.lookup(queue).publish(event);
					}
				}, new Event(queue, null, event));
			}
		}		
	}
}

package com.ud.iot.spring.components;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.zkoss.zk.ui.Desktop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.iot.websocket.events.EventType;

import reactor.core.publisher.Mono;

@Component
public class WebsocketSessionHandler implements WebSocketHandler {
	
	@Autowired private ClientEventHandler clientEventHandler;
	
	Set<Desktop> desktops = new HashSet<>();

	public void addDesktop(Desktop desktop) {
		this.desktops.add(desktop);
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {

		return session.receive().map(WebSocketMessage::getPayloadAsText)
                .doOnNext(txt -> sendData(txt))
                .doOnSubscribe(subscriber -> System.out.println("subscribeeeee----------------------->"))
                .doFinally(signalType -> System.out.println(".CLOSE---------------------------->"))
                .then();
	}
	
	private void sendData(String msg) {
		desktops.removeIf(d -> !d.isAlive());
		if(desktops.isEmpty()) {
			return;
		}
		//Transformar el evento
		EventType type = null;
		String msgEvent = null;
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jn = om.readTree(msg);
			type = EventType.valueOf(jn.get("type").asText());
			msgEvent = jn.get("event").toString();
		} catch(Exception e) {
			System.out.println("Excepción al transformar el mensaje recibido");
			return;
		}
		this.clientEventHandler.sendEventToClient(desktops, type, msgEvent);
	}
}

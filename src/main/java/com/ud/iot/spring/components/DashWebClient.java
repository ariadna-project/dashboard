package com.ud.iot.spring.components;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.zkoss.zk.ui.Desktop;

import com.ud.iot.model.Application;
import com.ud.iot.model.Data;
import com.ud.iot.model.Device;
import com.ud.iot.model.Position;
import com.ud.iot.spring.security.DashboardTokenProvider;
import com.ud.iot.util.AuthenticationUtils;
import com.ud.iot.util.ClientUtils;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class DashWebClient {

	private boolean initialized;
	private WebClient webClient;
	private String token;

	@Value("${dashboard.api.url}") private String apiUrl;
	@Value("${dashboard.api.ws.url}") private String wsUrl;
	
	@Autowired private DashboardTokenProvider tokenProvider;
	@Autowired private WebsocketSessionHandler websocketSessionHandler;

	public void init() {
		if(!this.initialized) {
			token = this.tokenProvider.generateToken(AuthenticationUtils.getUsername(), AuthenticationUtils.getUserRoles());
			
			webClient = WebClient.create(this.apiUrl);
			new ReactorNettyWebSocketClient().execute(URI.create(wsUrl), websocketSessionHandler).subscribe();
		}
	}
	

	//Application
	public void loadApplications(Desktop desktop, String eventName) {
		try {
			List<Application> data = new ArrayList<>();
			Flux<Application> flux = webClient.get().uri("/app").headers(h -> h.setBearerAuth(token)).retrieve().bodyToFlux(Application.class);
			flux.subscribe(data::add, error -> System.out.println("Error al obtener las aplicaciones - " + error),
					() -> ClientUtils.sendEventToDesktop(desktop, eventName, data));
		} catch (Exception e) {
			System.out.println("ERROR obteniendo aplicaciones");
		}
	}

	//Devices
	public void loadDevicesByApplication(Desktop desktop, String eventName, long idApp) {

		try {
			List<Device> data = new ArrayList<>();
			Flux<Device> flux = webClient.get().uri("/app/" + idApp + "/devices").accept(MediaType.APPLICATION_NDJSON).headers(h -> h.setBearerAuth(token)).retrieve().bodyToFlux(Device.class);
			flux.subscribe(data::add, error -> System.out.println("Error al obtener los dispositivos de la aplicación " + idApp + " - " + error),
					() -> ClientUtils.sendEventToDesktop(desktop, eventName, data));
		} catch (Exception e) {
			System.out.println("Error al obtener los dispositivos para la aplicación " + idApp);
		}
	}

	//Datas
	public long getTotalDataByDevice(long idDevice, long idData) {
		return webClient.get().uri("/device/" + idDevice + "/data/" + idData + "/count").headers(h -> h.setBearerAuth(token)).retrieve().bodyToMono(Long.class).block();
	}

	public void loadDeviceData(Desktop desktop, String eventName, long idDevice, long idData, List<Long> pages, long pageSize) {

		List<Data> data = new ArrayList<>();
		
		List<Flux<Data>> fluxes = new ArrayList<>();
		pages.forEach(page -> fluxes.add(this.getPagedData(idDevice, idData, page, pageSize).publishOn(Schedulers.boundedElastic())));
		Flux.merge(fluxes).subscribe(
				data::add,
				error -> System.out.println("ERROR al obtener los datos " + error),
				() -> {
					ClientUtils.sendEventToDesktop(desktop, eventName, data);
				});
	}
	
	private Flux<Data> getPagedData(long idDevice, long idData, long page, long pageSize) {
		return webClient.get().uri("/device/" + idDevice + "/data/" +  idData + "/" + page + "/" + pageSize).headers(h -> h.setBearerAuth(token)).retrieve().bodyToFlux(Data.class);
	}
	
	//Positions
	public long getTotalPositionsByDevice(long idDevice) {
		return webClient.get().uri("/device/" + idDevice + "/positions/count").headers(h -> h.setBearerAuth(token)).retrieve().bodyToMono(Long.class).block();
	}
	
	public void loadDevicePositions(Desktop desktop, String eventName, long idDevice, List<Long> pages, long pageSize) {

		List<Position> data = new ArrayList<>();
		
		List<Flux<Position>> fluxes = new ArrayList<>();
		pages.forEach(page -> fluxes.add(this.getPagedPositions(idDevice, page, pageSize).publishOn(Schedulers.boundedElastic())));
		Flux.merge(fluxes).subscribe(
				data::add,
				error -> System.out.println("ERROR al obtener los datos " + error),
				() -> {
					ClientUtils.sendEventToDesktop(desktop, eventName, data);
				});
	}
	
	private Flux<Position> getPagedPositions(long idDevice, long page, long pageSize) {
		return webClient.get().uri("/device/" + idDevice + "/positions/" +  page + "/" + pageSize).headers(h -> h.setBearerAuth(token)).retrieve().bodyToFlux(Position.class);
	}
}
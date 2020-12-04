package com.ud.iot.model;

import java.io.Serializable;
import java.util.Date;

public class Position implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long deviceId;
	private long serviceId;
	private Double latitude;
	private Double longitude;
	private Long altitude;
	private Double accuracy;
	private Double hdop;
	private String algorithmType;
	private Long numberOfGatewaysReceived;
	private Long numberOfGatewaysUsed;
	private Date fechaCrea;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public long getServiceId() {
		return serviceId;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Long getAltitude() {
		return altitude;
	}
	public void setAltitude(Long altitude) {
		this.altitude = altitude;
	}
	public Double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
	public Double getHdop() {
		return hdop;
	}
	public void setHdop(Double hdop) {
		this.hdop = hdop;
	}
	public String getAlgorithmType() {
		return algorithmType;
	}
	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}
	public Long getNumberOfGatewaysReceived() {
		return numberOfGatewaysReceived;
	}
	public void setNumberOfGatewaysReceived(Long numberOfGatewaysReceived) {
		this.numberOfGatewaysReceived = numberOfGatewaysReceived;
	}
	public Long getNumberOfGatewaysUsed() {
		return numberOfGatewaysUsed;
	}
	public void setNumberOfGatewaysUsed(Long numberOfGatewaysUsed) {
		this.numberOfGatewaysUsed = numberOfGatewaysUsed;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
}
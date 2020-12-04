package com.ud.iot.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lorenzo
 *
 */
public class Data implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long deviceId;
	private long dataId;
	private String value;
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
	public long getDataId() {
		return dataId;
	}
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Data [");
		builder.append("deviceId=");
		builder.append(deviceId);
		builder.append(", dataId=");
		builder.append(dataId);
		builder.append(", ");
		if (value != null) {
			builder.append("value=");
			builder.append(value);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
}
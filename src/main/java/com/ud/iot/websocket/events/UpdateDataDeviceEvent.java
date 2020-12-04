package com.ud.iot.websocket.events;

import java.io.Serializable;
import java.util.List;

import com.ud.iot.model.DataDevice;

public class UpdateDataDeviceEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<DataDevice> dataDevices;

	public List<DataDevice> getDataDevices() {
		return dataDevices;
	}

	public void setDataDevices(List<DataDevice> dataDevices) {
		this.dataDevices = dataDevices;
	}
	
}

package com.ud.iot.charts;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;

public class TempChart extends AbstractDataChart{

	private static final long serialVersionUID = 1L;
	private static final String ON_DATA_LOADED = "onDataLoadedTemp";
	
    @Override
    protected long getIdData() {
    	return 1;
    }
	
	@Override
	@Subscribe(ON_DATA_LOADED)
    public void onDataLoaded(Event event) {
		super.onDataLoaded(event);
    }
    
	@Override
	protected String getEventName() {
		return ON_DATA_LOADED;
	}

	@Override
	protected String getDataName() {
		return "Temperatura";
	}

	@Override
	protected String getYAxisTitle() {
		return "ºC";
	}
	
	@Override
	protected String getUnit() {
		return "ºC";
	}
}

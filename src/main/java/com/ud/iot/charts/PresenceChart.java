package com.ud.iot.charts;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;

public class PresenceChart extends AbstractDataChart{

	private static final long serialVersionUID = 1L;
	private static final String ON_DATA_LOADED = "onDataLoadedPresence";
	
    @Override
    protected long getIdData() {
    	return 2;
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
		return "Presencia";
	}

	@Override
	protected String getYAxisTitle() {
		return "Presencia";
	}
	
	@Override
	protected String getUnit() {
		return "";
	}
}

package com.ud.iot.charts;

import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.ListModelList;

import com.ud.iot.model.Position;

public class PositionsList extends AbstractDataList<Position>{

	private static final long serialVersionUID = 1L;
	private static final String ON_DATA_LOADED = "onPositionsLoaded";
	
	@Override
	@Subscribe(ON_DATA_LOADED)
	public void onDataLoaded(Event event) {
		super.onDataLoaded(event);
	}

	@Override
	protected ListModelList<Position> createListModelList() {
		return new ListModelList<>();
	}

	@Override
	protected String getDataName() {
		return "Posiciones";
	}
	@Override
	protected String getEventName() {
		return ON_DATA_LOADED;
	}

	@Override
	protected long getTotalElementFromService() {
		return this.dashWebClient.getTotalPositionsByDevice(idDevice);
	}

	@Override
	protected void loadElementFromService(List<Long> pages) {
		dashWebClient.loadDevicePositions(desktop, getEventName(), idDevice, pages, getPageSize());
	}

	@Override
	protected Comparator<Position> getElementComparator() {
		return new Comparator<Position>() {
    		@Override
    		public int compare(Position p0, Position p1) {
    			return p0.getFechaCrea().compareTo(p1.getFechaCrea());
    		}
		};
	}
}

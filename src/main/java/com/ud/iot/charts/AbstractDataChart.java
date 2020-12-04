package com.ud.iot.charts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.StringUtils;
import org.zkoss.chart.Charts;
import org.zkoss.chart.model.DefaultXYModel;
import org.zkoss.chart.model.XYModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import com.ud.iot.model.Data;
import com.ud.iot.model.DataDevice;
import com.ud.iot.spring.components.DashWebClient;
import com.ud.iot.spring.components.ObjectStorage;
import com.ud.iot.spring.components.WebsocketSessionHandler;
import com.ud.iot.util.ClientAttrs;
import com.ud.iot.util.ClientUtils;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class AbstractDataChart extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@WireVariable private ObjectStorage objectStorage;
	@WireVariable private DashWebClient dashWebClient;
	@WireVariable private WebsocketSessionHandler websocketSessionHandler;
	
	@Wire Charts charts;
	@Wire Button btnLoadMore;
	@Wire Label lblValue;
	@Wire Image imgUnit;
	
	private Desktop desktop;
	
	protected long idDevice;
	protected long totalPages;
	protected long lastPageLoaded;
	private List<Data> loadedData;

	protected abstract String getDataName();
	protected abstract String getUnit();
	protected abstract long getIdData();
	protected abstract String getYAxisTitle();
	protected abstract String getEventName();

	protected long getPageSize() {
		return 300;
	}
	
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }

        
        this.loadedData = new ArrayList<>();
        this.idDevice = Long.valueOf((String)objectStorage.getObject(ClientAttrs.PARAM_ID_DEVICE));
        websocketSessionHandler.addDesktop(desktop);
        
        String queue = "updateData_" + this.idDevice + "_" + this.getIdData();
        System.out.println("suscribiendo a " + queue);
        EventQueues.lookup(queue, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
        	
        	@Override
         	public void onEvent(Event event) throws Exception {
        		setCurrentData(((DataDevice)event.getData()).getValue());
        	}
        });

        new Thread(() -> {
        	//En la primera carga, obtener dos páginas
        	long totalRegs = dashWebClient.getTotalDataByDevice(idDevice, getIdData());
        	if(totalRegs == 0) {
        		ClientUtils.sendEventToDesktop(desktop, getEventName(), new ArrayList<>());
        	} else {
        		totalPages = totalRegs / getPageSize();
        		if(totalPages > 0 &&
        				totalRegs % totalPages > 0) {
        			totalPages++;
        		}
        		List<Long> pages = new ArrayList<>();
        		pages.add(totalPages == 0? totalPages : totalPages-1);
        		loadData(pages, getPageSize());
        	}
        }).start();
        
        //Eje X
        charts.getXAxis().setType("datetime");
        charts.getXAxis().getDateTimeLabelFormats().setDay("<b>%e. %b</b><br/>");
        charts.getXAxis().getDateTimeLabelFormats().setMinute("%H:%M");
        charts.getXAxis().setTitle("Time");
         
        //Eje Y
        charts.getYAxis().setTitle(this.getYAxisTitle());
        
        //Tooltip
        charts.getTooltip().setHeaderFormat("<b>"+ getDataName() +"</b><br/>");
        charts.getTooltip().setPointFormat("{point.x:%e. %b. %H:%M:%S} -> {point.y} " + this.getYAxisTitle());
        charts.getPlotOptions().getSpline().getMarker().setEnabled(true);

        Clients.showBusy(charts, "Obteniendo datos");
    }
    
    private void loadData(List<Long> pages, long pageSize) {
    	dashWebClient.loadDeviceData(desktop, getEventName(), idDevice, getIdData(), pages, getPageSize());
    	lastPageLoaded = pages.get(pages.size()-1);
    }
    
    public void onDataLoaded(Event event) {
    	Clients.clearBusy(charts);
    	
    	List<Data> data = new ArrayList<>((List<Data>) event.getData());
    	if(data.size() == 0) {
    		return;
    	}
    	System.out.println("Total datos..." + getIdData() + " - " + data.size());
    	Collections.sort(data, new Comparator<Data>() {
    		@Override
    		public int compare(Data d0, Data d1) {
    			return d0.getFechaCrea().compareTo(d1.getFechaCrea());
    		}
    	});
    	loadedData.addAll(0, data);
    	
    	XYModel model = new DefaultXYModel();
    	for (Data d : loadedData) {
    		model.addValue(this.getDataName(), d.getFechaCrea().getTime(), Float.parseFloat(d.getValue()));
    	}
    	if(!StringUtils.hasText(this.lblValue.getValue())) {
    		this.setCurrentData(this.loadedData.get(loadedData.size()-1).getValue());
    	}
    	charts.setModel(model);
    	this.showBtnLoadMore();
    }
    
    private void showBtnLoadMore() {
    	if(lastPageLoaded > 0) {
    		this.btnLoadMore.setVisible(true);
    	} else {
    		this.btnLoadMore.setVisible(false);
    	}
    }
    
    @Listen("onClick=#btnLoadMore")
    public void onClickBtnLoadMore(Event event) {
    	loadData(Arrays.asList(lastPageLoaded-1), getPageSize());
    }
    
    private void setCurrentData(String value) {
    	lblValue.setValue(value + " " + getUnit());
    	imgUnit.setVisible(true);
    }
}

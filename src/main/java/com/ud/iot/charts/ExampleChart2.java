package com.ud.iot.charts;

import java.util.Collections;
import java.util.List;

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
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;

import com.ud.iot.model.Data;
import com.ud.iot.spring.components.ObjectStorage;
import com.ud.iot.util.ClientAttrs;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExampleChart2 extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	private static final String ON_DATA_LOADED = "onDataLoaded";

	@WireVariable private ObjectStorage objectStorage;
	
	@Wire Charts chart;
	
	private long idDevice;
 
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }

        this.idDevice = Long.valueOf((String)objectStorage.getObject(ClientAttrs.PARAM_ID_DEVICE));

        chart.getXAxis().setType("datetime");
        chart.getXAxis().getDateTimeLabelFormats().setMonth("%e. %b");
        chart.getXAxis().getDateTimeLabelFormats().setYear("%b");
        chart.getXAxis().setTitle("Time");
        chart.getYAxis().setTitle("mA");
        chart.getYAxis().setMin(0);
        
        chart.getTooltip().setHeaderFormat("<b>{series.name}</b><br/>");
        chart.getTooltip().setPointFormat("{point.x:%e. %b}: {point.y} m");
        
        chart.getPlotOptions().getSpline().getMarker().setEnabled(true);
        Clients.showBusy(chart, "Obteniendo datos");
        new Thread(() -> {
        	List<Data> data = Collections.EMPTY_LIST;//RestClient.getDeviceData(this.idDevice);
        	try {
        		String id = chart.getParent().getParent().getParent().getId();
        		long milis = Long.parseLong("" + id.charAt(id.length() -1));
        		Thread.sleep(milis * 1000);
        	} catch(Exception e) { 
        	}
        	Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					EventQueues.lookup(ON_DATA_LOADED).publish(event);
				}
			}, new Event(ON_DATA_LOADED, null, data));
        }).start();
    }

    @Subscribe(ON_DATA_LOADED)
    public void onDataLoaded(Event event) {
    	Clients.clearBusy(chart);
    	
    	List<Data> data = (List<Data>) event.getData();
    	
    	XYModel model = new DefaultXYModel();
    	for (Data d : data) {
    		//model.addValue("Batería", d.getFechaCrea().getTime(), Float.parseFloat(d.getDatas().get("Batería")));
    	}
    	chart.setModel(model);
    }
}

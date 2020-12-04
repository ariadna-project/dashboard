package com.ud.iot.charts;

import java.util.Collections;
import java.util.List;

import org.zkoss.chart.Charts;
import org.zkoss.chart.model.DefaultXYModel;
import org.zkoss.chart.model.XYModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import com.ud.iot.model.Data;

public class ExampleChart1 extends GenericForwardComposer<Component>{

	private static final long serialVersionUID = 1L;

	@Wire Charts chart;
 
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
        chart.getXAxis().setType("datetime");
     
        new Thread(() -> {
        	List<Data> data = Collections.EMPTY_LIST;//RestClient.getDeviceData(1);
        	Executions.schedule(desktop, ExampleChart1.this, new Event("onData", null, data));
        }).start();
    }
    
    public void onData(Event event) {
    	List<Data> data = (List<Data>) event.getData();

    	XYModel model = new DefaultXYModel();
//    	data.forEach(d -> model.addValue("Altitud", d.getFechaCrea().getTime(), Float.parseFloat(d.getDatas().get("Batería"))));
    	chart.setModel(model);
    }
}
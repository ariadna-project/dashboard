package com.ud.iot.mvc.controllers;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zuti.zul.ForEach;

import com.ud.iot.model.Device;
import com.ud.iot.spring.components.DashWebClient;

/**
 * Este componente no puede extender a GenericForwardComposer por el uso del forEach
 * 
 * @author Lorenzo
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NavApplicationController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	private static final String ON_DEVICES_LOADED = "onDevicesLoaded";
	
	@WireVariable private DashWebClient dashWebClient;

	private Nav navApplication;
	@Wire("::shadow#feDevices") private ForEach feDevices;
	
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
        
        navApplication = (Nav)comp;
        boolean async = false;
        if(async) {
        	new Thread(() -> {
        		dashWebClient.loadDevicesByApplication(desktop, ON_DEVICES_LOADED, Long.valueOf(navApplication.getId()));
        	}).start();
        } else {
        	dashWebClient.loadDevicesByApplication(desktop, ON_DEVICES_LOADED, Long.valueOf(navApplication.getId()));
        }
    }
    
    @Subscribe(ON_DEVICES_LOADED)
    public void onData(Event event) {
    	List<Device> data = (List<Device>) event.getData();
    	feDevices.setItems(data);
    	feDevices.recreate();
    	navApplication.setBadgeText(data.size() + " disp");
    }
}
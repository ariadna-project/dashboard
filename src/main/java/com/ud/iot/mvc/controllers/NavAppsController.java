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

import com.ud.iot.model.Application;
import com.ud.iot.spring.components.DashWebClient;

/**
 * Este componente no puede extender a GenericForwardComposer por el uso del forEach
 * 
 * @author Lorenzo
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NavAppsController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	private static final String ON_APPS_LOADED = "onAppsLoaded";
	
	@WireVariable private DashWebClient dashWebClient;
	
	@Wire private Nav navApps;
	@Wire("::shadow#feApps") private ForEach feApps;
	
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
        
        boolean async = false;
        if(async) {
        	new Thread(() -> {
        		dashWebClient.loadApplications(desktop, ON_APPS_LOADED);
        	}).start();
        } else {
        	dashWebClient.loadApplications(desktop, ON_APPS_LOADED);
        }
    }
    
    @Subscribe(ON_APPS_LOADED)
    public void onData(Event event) {
    	List<Application> data = (List<Application>) event.getData();
    	feApps.setItems(data);
    	feApps.recreate();
    	navApps.setBadgeText(String.valueOf(data.size()));
    }
}
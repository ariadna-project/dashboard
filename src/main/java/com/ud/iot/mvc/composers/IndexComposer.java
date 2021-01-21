package com.ud.iot.mvc.composers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Include;

import com.ud.iot.util.ClientAttrs;

public class IndexComposer extends BaseComposer{

	private static final long serialVersionUID = 1L;
	
	@Wire private Include incPanel;
	@Wire Navitem navAdmin;
	@Wire Navitem navApps;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		this.getDashWebClient().init();
	}

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
    }
    
    /**
     * Método lanzado desde el zul para actualizar el 
     * contenido del dash con el dispositivo
     * 
     * @param target
     */
    public void onDeviceClicked(Component target) {
    	String[] targetIdSplit = target.getId().split("_");
    	this.showDash(targetIdSplit[3], targetIdSplit[1]);
    }
    
    /**
     * Método lanzado desde el zul para actualizar el 
     * contenido del dash con la aplicación
     * 
     * @param target
     */
    public void onAppOpen(OpenEvent event) {
    	
//    	if(event.isOpen()) {
//    		this.showDash(event.getTarget().getId(), null);
//    	}
    }
    
    private void showDash(String appId, String devId) {
    	this.incPanel.setSrc(null);
    	this.getObjectStorage().pushObject(ClientAttrs.PARAM_ID_APP, appId);
    	this.getObjectStorage().pushObject(ClientAttrs.PARAM_ID_DEVICE, devId);
    	this.incPanel.setSrc(ClientAttrs.URL_DASH);
    }
}
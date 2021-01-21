package com.ud.iot.mvc.composers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

public class DashPanelComposer extends BaseComposer{

	private static final long serialVersionUID = 1L;
	private String idApp;
	private String idDevice;
	
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if(!desktop.isServerPushEnabled()) {
        	desktop.enableServerPush(true);
        }
    }
}
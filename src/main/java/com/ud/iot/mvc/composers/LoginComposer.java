package com.ud.iot.mvc.composers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Video;
import org.zkoss.zul.Label;

public class LoginComposer extends BaseComposer{

	private static final long serialVersionUID = 1L;
	
	@Wire private Label lblError;
	@Wire private Video video;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.video.setSrc("http://demo.unified-streaming.com/video/tears-of-steel/tears-of-steel.ism");
//        this.video.setSrc("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-webm-file.webm");
//        this.video.setSrc("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
        try {
        	Object error = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        	if(error != null) {
        		this.lblError.setVisible(true);
        		String textError = "Error desconocido al iniciar sesión";
        		try {
        			AuthenticationException authEx = (AuthenticationException)error;
        			textError = authEx.getMessage();
        		} finally {
        			this.lblError.setValue(textError);
        		}
        	}
        	
        } catch(Exception e) {
        }
    }
}
package com.ud.iot.mvc.composers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.ud.iot.exceptions.UserException;
import com.ud.iot.spring.components.AuthBiz;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SignUpComposer extends BaseComposer{

	private static final long serialVersionUID = 1L;
	
	@WireVariable private AuthBiz authBiz;
	
	@Wire private Label lblError;
	@Wire private Textbox txtUsername;
	@Wire private Textbox txtPassword;
	@Wire private Textbox txtRole;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }
    
    @Listen("onClick=#btnSignUp")
    public void onClickBtnSignUp(Event event) {
    	try {
			this.authBiz.createUser(txtUsername.getValue(), txtPassword.getValue(), txtRole.getValue());
		} catch (UserException e) {
			lblError.setValue(e.getMessage());
			lblError.setVisible(true);
		}
    }
}
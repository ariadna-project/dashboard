package com.ud.iot.mvc.composers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.ud.iot.spring.components.DashWebClient;
import com.ud.iot.spring.components.ObjectStorage;

/**
 * Clase base para todos los componentes del dashboard
 * @author Lorenzo
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BaseComposer extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;

	@WireVariable private DashWebClient dashWebClient;
	@WireVariable private ObjectStorage objectStorage;
	
	protected DashWebClient getDashWebClient() {
		return dashWebClient;
	}

	protected ObjectStorage getObjectStorage() {
		return objectStorage;
	}
}
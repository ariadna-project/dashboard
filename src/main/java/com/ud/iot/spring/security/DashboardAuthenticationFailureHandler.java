package com.ud.iot.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class DashboardAuthenticationFailureHandler extends ForwardAuthenticationFailureHandler {

	@Autowired
	public DashboardAuthenticationFailureHandler(String forwardFailureUrl) {
		super(forwardFailureUrl);
	}
}
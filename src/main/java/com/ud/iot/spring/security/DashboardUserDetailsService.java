package com.ud.iot.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ud.iot.documents.DashboardUser;
import com.ud.iot.spring.components.AuthBiz;

@Component
public class DashboardUserDetailsService implements UserDetailsService {

	@Autowired private AuthBiz authBiz;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DashboardUser dashUser = this.authBiz.getUserByName(username);
		if(dashUser != null) {
			return User.builder()
	            	.username(dashUser.getName())
	            	.password(dashUser.getPass())
//	            	.disabled(dashUser.isDisabled())
//	            	.accountExpired(dashUser.isAccountExpired())
//	            	.accountLocked(dashUser.isAccountLocked())
//	            	.credentialsExpired(dashUser.isCredentialsExpired())
	            	.roles(dashUser.getRole())
	            	.build();
        } 
		throw new UsernameNotFoundException("User " + username + " doesn't exist");
	}
	
	public boolean matches(String decPass, String encPass) {
		return this.authBiz.matches(decPass, encPass);
	}
}
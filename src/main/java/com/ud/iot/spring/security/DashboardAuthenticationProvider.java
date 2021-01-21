package com.ud.iot.spring.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class DashboardAuthenticationProvider implements AuthenticationProvider {

	@Autowired private DashboardUserDetailsService dashboardUserDetailsService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        UserDetails systemUser = this.dashboardUserDetailsService.loadUserByUsername(name);
        
        if (this.dashboardUserDetailsService.matches(password, systemUser.getPassword())) {
            final List<GrantedAuthority> grantedAuths = new ArrayList<>(systemUser.getAuthorities());
            final Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        }
        throw new BadCredentialsException("Wrong credentials to user " + name);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
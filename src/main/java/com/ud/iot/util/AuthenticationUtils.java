package com.ud.iot.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils{

	public static String getUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	public static List<String> getUserRoles() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
		return roles;
	}
}
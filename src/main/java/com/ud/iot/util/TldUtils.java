package com.ud.iot.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TldUtils {

	public static boolean roles(String compRoles) {
		try {
			List<String> userRoles = AuthenticationUtils.getUserRoles();
			if(userRoles == null) {
				return false;
			}
			List<String> compRolesSplit = Arrays.asList(compRoles.split(",")).stream().map(r -> r.trim()).collect(Collectors.toList());
			userRoles.retainAll(compRolesSplit);
			return !userRoles.isEmpty();
		} catch(Exception e) {
		}
		return false;
	}
}

package com.ud.iot.spring.components;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ud.iot.documents.DashboardUser;
import com.ud.iot.exceptions.DuplicateUserException;
import com.ud.iot.exceptions.UserException;
import com.ud.iot.spring.repositories.UserRepository;
import com.ud.iot.spring.sequences.SequenceGeneratorService;

@Component
public class AuthBiz {
	
	@Value("${dashboard.admin.user}") String initUserName;
	@Value("${dashboard.admin.pass}") String initUserPass;
	@Value("${dashboard.admin.role}") String initUserRole;
	
	@Autowired private UserRepository userRepository;
	@Autowired private SequenceGeneratorService sequenceGeneratorService;

	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		try {
			this.createUser(this.initUserName, this.initUserPass, this.initUserRole);
		} catch (UserException e) {
			System.out.println("No se creó el usuario inicial (" + this.initUserName + "): " + e.getMessage());
		}
	}
	
	public boolean matches(String decPass, String encPass) {
		return this.passwordEncoder.matches(decPass, encPass);
	}
	
	public void createUser(String username, String pass, String role) throws UserException {
		DashboardUser oldUser = this.getUserByName(username);
		if(oldUser != null) {
			throw new DuplicateUserException("Usuario " + username + " ya existe");
		}
		DashboardUser dashboardUser = new DashboardUser();
		dashboardUser.setId(this.sequenceGeneratorService.generateSequence(DashboardUser.SEQUENCE_NAME));
		dashboardUser.setName(username);
		dashboardUser.setPass(this.passwordEncoder.encode(pass));
		dashboardUser.setRole(role);
		this.userRepository.save(dashboardUser);
	}
	
	public DashboardUser getUserByName(String username) {
		return this.userRepository.findByName(username).orElse(null);
	}
}
package com.ud.iot.documents;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("tusuario")
public class DashboardUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient public static final String SEQUENCE_NAME = "tusuario_sequence";

	@Field("id") private Long id;
	@Field("name") private String name;
	@Field("pass") private String pass;
	@Field("role") private String role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}

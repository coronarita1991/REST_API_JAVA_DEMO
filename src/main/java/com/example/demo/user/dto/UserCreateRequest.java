package com.example.demo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserCreateRequest {
	@NotBlank(message = "name은 필수입니다.")
	private String name;

	@Email(message = "email 형식이 올바르지 않습니다.")
	@NotBlank(message = "email은 필수입니다.")
	private String email;

	@NotBlank(message = "password는 필수입니다.")
	private String password;

	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }

	public void setName(String name) { this.name = name; }
	public void setEmail(String email) { this.email = email; }
	public void setPassword(String password) { this.password = password; }
}

package com.example.lottery.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class WebUser {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}

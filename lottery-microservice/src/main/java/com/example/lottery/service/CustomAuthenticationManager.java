package com.example.lottery.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
	private final UserDetailsManager userDetailsManager;

	public CustomAuthenticationManager(UserDetailsManager userDetailsManager) {
		this.userDetailsManager = userDetailsManager;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String pw = authentication.getCredentials().toString();
		var userDetails = userDetailsManager.loadUserByUsername(username);
		if (userDetails.getUsername().equals(username) && userDetails.getPassword().equals(pw))
		   return new UsernamePasswordAuthenticationToken(username, null,userDetails.getAuthorities());
		throw new BadCredentialsException("Wrong username or password.");
	}
}

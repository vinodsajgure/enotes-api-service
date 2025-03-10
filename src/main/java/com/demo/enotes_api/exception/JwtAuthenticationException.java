package com.demo.enotes_api.exception;

public class JwtAuthenticationException extends RuntimeException {

	public JwtAuthenticationException(String message) {
		super(message);
	}

}

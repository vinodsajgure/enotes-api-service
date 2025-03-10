package com.demo.enotes_api.exception;

public class JwtTokenExpiredException extends RuntimeException {

	public JwtTokenExpiredException(String message) {
		super(message);
	}

}

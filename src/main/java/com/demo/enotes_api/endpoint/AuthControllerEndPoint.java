package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.LoginRequest;
import com.demo.enotes_api.dto.UserRequest;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/auth")
public interface AuthControllerEndPoint {
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto,HttpServletRequest request) throws Exception;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);

}

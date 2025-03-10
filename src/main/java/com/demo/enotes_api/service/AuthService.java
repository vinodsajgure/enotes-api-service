package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.LoginRequest;
import com.demo.enotes_api.dto.LoginResponse;
import com.demo.enotes_api.dto.UserRequest;

public interface AuthService {

	public Boolean registerUser(UserRequest userDto, String url) throws Exception;
	
	public LoginResponse login(LoginRequest loginRequest);
}

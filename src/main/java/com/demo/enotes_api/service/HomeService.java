package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.UserRequest;

public interface HomeService {

	public Boolean verifyUserEmailAccount(Integer userId, String verificationCode);
	
}

package com.demo.enotes_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.dto.LoginRequest;
import com.demo.enotes_api.dto.LoginResponse;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.endpoint.AuthControllerEndPoint;
import com.demo.enotes_api.service.AuthService;
import com.demo.enotes_api.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController implements AuthControllerEndPoint {

	@Autowired
	private AuthService authService;
	

	@Override
	public ResponseEntity<?> registerUser(UserRequest userDto,HttpServletRequest request) throws Exception {
		log.info("AuthController : registerUser () : Execution Start");
		String url = CommonUtil.getUrl(request);
		Boolean registerdUser = authService.registerUser(userDto,url);
		if (!registerdUser) {
			log.info("error : {}",  "Register user failed");
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,"Somthing went wrong, user not saved.");
		}
		log.info("AuthController : registerUser () : Execution End");
		return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "User Saved Successfully.");
	}

	

	@Override
	public ResponseEntity<?> login(LoginRequest loginRequest){
		LoginResponse loginResponse = authService.login(loginRequest);
		if(ObjectUtils.isEmpty(loginResponse)){
			CommonUtil.createBuildErrorResponseMessage(HttpStatus.BAD_REQUEST, "Invalid Credentials");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, loginResponse);
	}
	

}

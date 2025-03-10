package com.demo.enotes_api.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.dto.LoginRequest;
import com.demo.enotes_api.dto.LoginResponse;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.dto.UserResponse;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.service.AuthService;
import com.demo.enotes_api.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping()
	public ResponseEntity<?> saveUser(@RequestBody UserRequest userDto,HttpServletRequest request) throws Exception {
		String url = CommonUtil.getUrl(request);
		Boolean registerdUser = authService.registerUser(userDto,url);
		if (registerdUser) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "User Saved Successfully.");
		}

		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,"Somthing went wrong, user not saved.");
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		LoginResponse loginResponse = authService.login(loginRequest);
		if(ObjectUtils.isEmpty(loginResponse)){
			CommonUtil.createBuildErrorResponseMessage(HttpStatus.BAD_REQUEST, "Invalid Credentials");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, loginResponse);
	}
	

}

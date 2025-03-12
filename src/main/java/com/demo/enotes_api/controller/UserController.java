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

import com.demo.enotes_api.dto.ChangePasswordRequest;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.dto.UserResponse;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.service.UserService;
import com.demo.enotes_api.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
		log.info("UserController : getUserById () : Start");
		UserRequest userById = userService.getUserById(userId);
		if (!ObjectUtils.isEmpty(userById)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, userById);
		} else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NOT_FOUND, "User Not Found.");
		}
	}

	@GetMapping("/userList")
	public ResponseEntity<?> getAllUsers() {
		List<UserRequest> userDtoList = userService.getAllUsers();
		if (!CollectionUtils.isEmpty(userDtoList)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, userDtoList);
		} else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NO_CONTENT, "Users Not Available.");
		}
	}

	@GetMapping("/loggedInUser")
	public ResponseEntity<?> getLoggedInUser(){
		User loggedInUser = CommonUtil.getLoggedInUser();
		UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
		return CommonUtil.createBuildResponse(HttpStatus.OK, userResponse);
	}
	
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest passwordRequest){
		userService.changeUserPassword(passwordRequest);
		return CommonUtil.createBuildResponse(HttpStatus.OK, "Passoword changed Successfully.");
	}
}

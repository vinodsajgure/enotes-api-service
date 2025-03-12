package com.demo.enotes_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.dto.ResetPasswordRequest;
import com.demo.enotes_api.endpoint.HomeControllerEndPoint;
import com.demo.enotes_api.service.HomeService;
import com.demo.enotes_api.service.UserService;
import com.demo.enotes_api.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController implements HomeControllerEndPoint {
	
	Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
	
	//@GetMapping("/verify")
	@Override
	public ResponseEntity<?> verifyUserEmailAccount(@RequestParam Integer uid, @RequestParam String code) {
		log.info("HomeController : VerifyUserEmailAccount() : Execution Start");
		Boolean verifyUserEmailAccount = homeService.verifyUserEmailAccount(uid, code);

		if (verifyUserEmailAccount) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "user account verified successfully.");
		}
		log.info("HomeController : VerifyUserEmailAccount() : Execution End");
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.BAD_REQUEST, "Invalid Verfication Link.");
	}

	@Override
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email,HttpServletRequest request) throws Exception{
		userService.sendEmailForPasswordReset(email,request);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Please check the Email sent to your registered email id for resetting the Password. ");
	}
	
	@Override
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String token){
		userService.verifyPasswordResetTokenLink(uid,token);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Verified Successfully.");
	}
	
	@Override
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
		userService.resetPassword(resetPasswordRequest);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Password has been reset Successfully.");
	}
	
}

package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.enotes_api.dto.ResetPasswordRequest;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/home")
public interface HomeControllerEndPoint {

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserEmailAccount(@RequestParam Integer uid, @RequestParam String code);
	
	@GetMapping("/send-email-for-pswd-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email,HttpServletRequest request)throws Exception;
	
	@GetMapping("/verify-pswd-reset-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String token);
	
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest);
}

package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.enotes_api.dto.ResetPasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="Home", description="All Home Page Operations APIs")
@RequestMapping("/api/v1/home")
public interface HomeControllerEndPoint {

	@Operation(summary = "Verify User EmailId Account Endpoint",description = "User account verification after registration")
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserEmailAccount(@RequestParam Integer uid, @RequestParam String code);
	
	@Operation(summary = "Send Email For Password Reset Endpoint",description = "User can send email for account password reset")
	@GetMapping("/send-email-for-pswd-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email,HttpServletRequest request)throws Exception;
	
	@Operation(summary = "User Verification Password Reset Link Endpoint",description = "User Verification Password Link")
	@GetMapping("/verify-pswd-reset-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String token);
	
	@Operation(summary = "Reset User Password Endpoint",description = "User can reset password")
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest);
}

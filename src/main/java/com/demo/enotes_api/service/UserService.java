package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.ChangePasswordRequest;
import com.demo.enotes_api.dto.ResetPasswordRequest;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.dto.UserResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public UserRequest getUserById(Integer userId);
	
	public List<UserResponse> getAllUsers();
	
	public void changeUserPassword(ChangePasswordRequest passwordRequest);

	public void sendEmailForPasswordReset(String email,HttpServletRequest request) throws Exception;

	public void verifyPasswordResetTokenLink(Integer uid, String token);

	public void resetPassword(ResetPasswordRequest resetPasswordRequest);
}

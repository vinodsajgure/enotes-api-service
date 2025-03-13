package com.demo.enotes_api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.demo.enotes_api.dto.ChangePasswordRequest;
import com.demo.enotes_api.dto.EmailRequest;
import com.demo.enotes_api.dto.ResetPasswordRequest;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.dto.UserResponse;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.UserRepository;
import com.demo.enotes_api.service.UserService;
import com.demo.enotes_api.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public UserRequest getUserById(Integer userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found, Invalid user id."));
		UserRequest userDto = mapper.map(user, UserRequest.class);
		return userDto;
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> userList = userRepository.findAll();
		List<UserResponse> useDtolist = userList.stream().map(user -> mapper.map(userList, UserResponse.class)).toList();
		return useDtolist;
	}
	
	@Override
	public void changeUserPassword(ChangePasswordRequest passwordRequest) {
		User loggedInUser = CommonUtil.getLoggedInUser();
		
		if(!passwordEncoder.matches(passwordRequest.getOldPassword(),loggedInUser.getPassword())) {
			throw new IllegalArgumentException("Current password is incorrect !!");
		}
		
		String encodedNewPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
		loggedInUser.setPassword(encodedNewPassword);
		
		userRepository.save(loggedInUser);
	}

	@Override
	public void sendEmailForPasswordReset(String email, HttpServletRequest request) throws Exception {
		User user = userRepository.findByEmail(email);
		if(ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException("Invalid Email Id.");
		}
//		Generate Unique Password Reset Token
		String passwordResetToken = UUID.randomUUID().toString();
		user.getStatus().setPasswordResetToken(passwordResetToken);
		User updatedUser = userRepository.save(user);
		
		String url = CommonUtil.getUrl(request);
		
		sendEmailRequestForPasswordReset(updatedUser,url);
		
	}

	private void sendEmailRequestForPasswordReset(User user,String url) throws Exception {

		String message = "Hi, <b>[[username]]</b>"
				+ "<br><p> You have requested to reset your password.</P>"
				+ "<p> To Reset your Password, Please Click the Below Link.<p>"
				+ "<a href='[[url]]'> Change My Password </a> "
				+ "<p> Ignore this email if you remember the password,<p>"
				+ "<p>or you have not made the request.<p>"
				+ "<br><br>" + "Thanks <br>Enotes.com";

		message = message.replace("[[username]]", user.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-reset-link?uid=" + user.getId() + "&&token="
				+ user.getStatus().getPasswordResetToken());

		EmailRequest emailRequest = EmailRequest.builder().to(user.getEmail())
				.title("Reset Password").subject("Password Reset Link").message(message).build();

		emailService.sendEmail(emailRequest);
		
	}

	@Override
	public void verifyPasswordResetTokenLink(Integer uid, String token) {
	
		User user = userRepository.findById(uid).orElseThrow(()-> new ResourceNotFoundException("Invalid User"));
		verifyPasswordResetToken(user.getStatus().getPasswordResetToken(),token);
	}

	private void verifyPasswordResetToken(String passwordResetToken, String token) {
		if(StringUtils.hasText(token)) {
			
			if(!StringUtils.hasText(passwordResetToken)) {
				throw new IllegalArgumentException("Password Already Reset");
			}
			
			if(!passwordResetToken.equals(token)) {
				throw new IllegalArgumentException("Invalid url");
			}
			
		}else {
			throw new IllegalArgumentException("Invalid Token");
		}
		
	}

	@Override
	public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
		User user = userRepository.findById(resetPasswordRequest.getUid()).orElseThrow(()-> new ResourceNotFoundException("Invalid User"));
		String encodedNewPassword = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
		user.setPassword(encodedNewPassword);
		user.getStatus().setPasswordResetToken(null);
		userRepository.save(user);
	}

}

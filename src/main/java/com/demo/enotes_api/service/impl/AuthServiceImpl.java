package com.demo.enotes_api.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.demo.enotes_api.config.security.CustomUserDetails;
import com.demo.enotes_api.dto.EmailRequest;
import com.demo.enotes_api.dto.LoginRequest;
import com.demo.enotes_api.dto.LoginResponse;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.dto.UserResponse;
import com.demo.enotes_api.entity.AccountStatus;
import com.demo.enotes_api.entity.Role;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.repository.RoleRepository;
import com.demo.enotes_api.repository.UserRepository;
import com.demo.enotes_api.service.AuthService;
import com.demo.enotes_api.service.JwtService;
import com.demo.enotes_api.util.Validations;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private Validations validations;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;

	@Override
	public Boolean registerUser(UserRequest userDto, String url) throws Exception {
		// user validations
		validations.userValidations(userDto);

		User user = mapper.map(userDto, User.class);

		setRole(userDto, user);

		AccountStatus accountStatus = AccountStatus.builder().isActive(false)
				.verificationCode(UUID.randomUUID().toString()).build();

		user.setStatus(accountStatus);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(user);
		if (!ObjectUtils.isEmpty(savedUser)) {
			// Sending Email to user for successful Registration.
			sendEmailToUserAfterSuccessfulRegister(savedUser, url);
			return true;
		} else {
			return false;
		}
	}

	private void sendEmailToUserAfterSuccessfulRegister(User savedUser, String url) throws Exception {

		String message = "Hi,<b>[[username]]</b>" + "<br> Your Account has been created with E-notes Successfully.<br>"
				+ "<br> To Verify the Account, Please Click the Below Link.<br>"
				+ "<a href='[[url]]'> Click Here </a> <br><br>" + "Thanks <br>Enotes.com";

		message = message.replace("[[username]]", savedUser.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + savedUser.getId() + "&&code="
				+ savedUser.getStatus().getVerificationCode());

		EmailRequest emailRequest = EmailRequest.builder().to(savedUser.getEmail())
				.title("Account Created Successfully.").subject("Account With Enotes").message(message).build();

		emailService.sendEmail(emailRequest);

	}

	private void setRole(UserRequest userDto, User user) {
		List<Integer> reqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleIds);
		user.setRoles(roles);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		if(authenticate.isAuthenticated()) {
			CustomUserDetails userDetails = (CustomUserDetails)authenticate.getPrincipal();
			
			String token = jwtService.generateToken(userDetails.getUser());
			
			LoginResponse loginResponse = LoginResponse.builder()
					.user(mapper.map(userDetails.getUser(), UserResponse.class))
					.token(token)
					.build();
			
			return loginResponse;
		}
		
		return null;
	}

}

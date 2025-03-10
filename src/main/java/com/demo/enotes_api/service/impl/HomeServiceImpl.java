package com.demo.enotes_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.enotes_api.entity.AccountStatus;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.exception.SuccessException;
import com.demo.enotes_api.repository.UserRepository;
import com.demo.enotes_api.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyUserEmailAccount(Integer userId, String verificationCode) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Invalid User"));
		
		if(user.getStatus().getVerificationCode()==null)
		{
			throw new SuccessException("Account alreday verified");
		}
		
		if(user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
	
			userRepository.save(user);
			return true;
		}
		return false;
	}

}

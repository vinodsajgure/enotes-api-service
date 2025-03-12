package com.demo.enotes_api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.enotes_api.entity.AccountStatus;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.exception.SuccessException;
import com.demo.enotes_api.repository.UserRepository;
import com.demo.enotes_api.service.HomeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyUserEmailAccount(Integer userId, String verificationCode) {
		log.info("HomeServiceImpl : VerifyUserEmailAccount () : Start");
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Invalid User"));
		
		if(user.getStatus().getVerificationCode()==null)
		{
			log.info("message : Account alreday verified");
			throw new SuccessException("Account alreday verified");
		}
		
		if(user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			userRepository.save(user);
			log.info("message : Account verification success");
			return true;
		}
		log.info("HomeServiceImpl : VerifyUserEmailAccount () : Ends");
		return false;
	}

}

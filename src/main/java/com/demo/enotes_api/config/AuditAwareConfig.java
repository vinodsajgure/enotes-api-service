package com.demo.enotes_api.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		User loggedInUser = CommonUtil.getLoggedInUser();
		return Optional.of(loggedInUser.getId());
	}

}

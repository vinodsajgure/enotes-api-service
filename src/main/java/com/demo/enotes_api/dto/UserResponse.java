package com.demo.enotes_api.dto;

import java.util.List;

import com.demo.enotes_api.dto.UserRequest.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNo;
	private StatusDto status;
	private List<RoleDto> roles;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class RoleDto{
		private Integer id;
		private String name;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class StatusDto{
		private Integer id;
		private Boolean isActive;
	}
}

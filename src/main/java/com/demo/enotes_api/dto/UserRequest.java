package com.demo.enotes_api.dto;

import java.util.Date;
import java.util.List;

import com.demo.enotes_api.entity.Role;
import com.demo.enotes_api.entity.User;

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
public class UserRequest {

	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNo;
	private String password;
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
}

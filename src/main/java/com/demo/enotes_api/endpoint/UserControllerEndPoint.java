package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.ChangePasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="User", description="All User Operations APIs")
@RequestMapping("/api/v1/user")
public interface UserControllerEndPoint {

	@Operation(summary = "Get User By Id Endpoint",tags="User",description = "Get User By Id")
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Integer userId);
	
	
	@Operation(summary = "Get All Users Endpoint",tags="User",description = "Get All User List")
	@GetMapping("/userList")
	public ResponseEntity<?> getAllUsers();
	
	@Operation(summary = "Get Logged In User Endpoint",tags="User",description = "Get Logged In User")
	@GetMapping("/loggedInUser")
	public ResponseEntity<?> getLoggedInUser();
	
	@Operation(summary = "Change User Password Endpoint",tags="User",description = "Change User Password")
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest passwordRequest);
}

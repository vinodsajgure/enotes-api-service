package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.ChangePasswordRequest;

@RequestMapping("/api/v1/user")
public interface UserControllerEndPoint {

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Integer userId);
	
	@GetMapping("/userList")
	public ResponseEntity<?> getAllUsers();
	
	@GetMapping("/loggedInUser")
	public ResponseEntity<?> getLoggedInUser();
	
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest passwordRequest);
}

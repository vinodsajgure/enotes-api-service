package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.ToDoDto;

@RequestMapping("/api/v1/toDo")
public interface ToDoControllerEndPoint {

	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveToDo(@RequestBody ToDoDto toDoDto);
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getToDoById(@PathVariable Integer id);
	
	@GetMapping("/toDoList")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllTodosByUser();
}

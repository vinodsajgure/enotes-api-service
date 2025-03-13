package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.ToDoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="To-Do", description="All To Do Operations APIs")
@RequestMapping("/api/v1/toDo")
public interface ToDoControllerEndPoint {

	@Operation(summary = "Save To Do Endpoint",tags="To-Do",description = "User Save To Do")
	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveToDo(@RequestBody ToDoDto toDoDto);
	
	@Operation(summary = "Get To Do By Id Endpoint",tags="To-Do",description = "User Can Get To Do By Id")
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getToDoById(@PathVariable Integer id);
	
	@Operation(summary = "Get All To Dos Endpoint",tags="To-Do",description = "User Can Get All To Dos")
	@GetMapping("/toDoList")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllTodosByUser();
}

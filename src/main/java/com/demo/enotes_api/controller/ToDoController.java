package com.demo.enotes_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.dto.ToDoDto;
import com.demo.enotes_api.endpoint.ToDoControllerEndPoint;
import com.demo.enotes_api.service.ToDoService;
import com.demo.enotes_api.util.CommonUtil;

@RestController
public class ToDoController implements ToDoControllerEndPoint {

	@Autowired
	private ToDoService toDoService;
	

	@Override
	public ResponseEntity<?> saveToDo(ToDoDto toDoDto){
		Boolean saveToDo = toDoService.saveToDo(toDoDto);
		if(saveToDo) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "To Do saved successfully.");
		}else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Somthing went wrong, To Do not saved.");
		}
	}
	

	@Override
	public ResponseEntity<?> getToDoById(Integer id){
		ToDoDto toDoById = toDoService.getToDoById(id);
		if(!ObjectUtils.isEmpty(toDoById)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, toDoById);
		}else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NOT_FOUND, "To Do Not Found. Invalid Id.");
		}
	}
	

	@Override
	public ResponseEntity<?> getAllTodosByUser(){
		List<ToDoDto> toDoDtoList = toDoService.getToDosByUser();
		if(!CollectionUtils.isEmpty(toDoDtoList)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, toDoDtoList);
		}else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NO_CONTENT, "To Do not found");
		}
	}
}

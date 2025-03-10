package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.ToDoDto;

public interface ToDoService {
	
	public Boolean saveToDo(ToDoDto toDoDto);
	
	public ToDoDto getToDoById(Integer id);
	
	public List<ToDoDto> getToDosByUser();

}

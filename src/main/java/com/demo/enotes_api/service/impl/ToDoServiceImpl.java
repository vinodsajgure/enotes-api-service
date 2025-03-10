package com.demo.enotes_api.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.demo.enotes_api.dto.ToDoDto;
import com.demo.enotes_api.dto.ToDoDto.StatusDto;
import com.demo.enotes_api.entity.ToDo;
import com.demo.enotes_api.enums.ToDoStatus;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.ToDoRepository;
import com.demo.enotes_api.service.ToDoService;
import com.demo.enotes_api.util.CommonUtil;
import com.demo.enotes_api.util.Validations;

@Service
public class ToDoServiceImpl implements ToDoService {

	@Autowired
	private ToDoRepository toDoRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validations validations;


	@Override
	public Boolean saveToDo(ToDoDto toDoDto) {
		//Validate ToDo Status
		validations.toDoValidations(toDoDto);
		
		ToDo toDo = mapper.map(toDoDto, ToDo.class);
		toDo.setStatusId(toDoDto.getStatus().getId());
		ToDo savedToDo = toDoRepository.save(toDo);
		if (!ObjectUtils.isEmpty(savedToDo)) {
			return true;
		} else
			return false;

	}

	@Override
	public ToDoDto getToDoById(Integer id) {
		ToDo toDo = toDoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("To Do Not Found!! Invalid id."));
		ToDoDto toDoDto = mapper.map(toDo, ToDoDto.class);
		setStatus(toDoDto,toDo);
		return toDoDto;
	}

	private void setStatus(ToDoDto toDoDto, ToDo toDo) {
		for(ToDoStatus st : ToDoStatus.values()) {
			if(st.getId().equals(toDo.getStatusId())) {
				StatusDto statusDto = StatusDto.builder()
						.id(st.getId())
						.name(st.getName())
						.build(); 
				
				toDoDto.setStatus(statusDto);
			}
		}
	}

	@Override
	public List<ToDoDto> getToDosByUser() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<ToDo> toDoList =  toDoRepository.findByCreatedBy(userId);
		List<ToDoDto> toDoDtolist = toDoList.stream().map(td -> mapper.map(td, ToDoDto.class)).toList();
		return toDoDtolist;
	}

}

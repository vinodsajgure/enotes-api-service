package com.demo.enotes_api.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.dto.NotesDto;
import com.demo.enotes_api.dto.ToDoDto;
import com.demo.enotes_api.dto.ToDoDto.StatusDto;
import com.demo.enotes_api.dto.UserRequest;
import com.demo.enotes_api.enums.ToDoStatus;
import com.demo.enotes_api.exception.CategoryDtoValidationException;
import com.demo.enotes_api.exception.DataAlreadyExistsException;
import com.demo.enotes_api.exception.NotesDtoValidationException;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.RoleRepository;
import com.demo.enotes_api.repository.UserRepository;

@Component
public class Validations {
	
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	public void categoryValidations(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object/JSON should not be null or empty");
		} else {
			// Name field validations.
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name : ", "Name should not be null or empty");
			} else {
				if (categoryDto.getName().length() < 3) {
					error.put("name", "name length should be minimum 3 chars");
				}
				if (categoryDto.getName().length() > 30) {
					error.put("name", "name length should be maximum 30 chars");
				}
			}
			// Description field validations.
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description : ", "Description should not be null or empty");
			} else {
				if (categoryDto.getDescription().length() < 5) {
					error.put("description", "description length should be minimum 5 chars");
				}
				if (categoryDto.getDescription().length() > 100) {
					error.put("description", "description length should be maximum 100 chars");
				}
			}
			// isActive field validations
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive : ", "IsActive should not be null or empty");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
						&& categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
					error.put("isActive : ", "Invalid value");
				}
			}
		}

		if (!error.isEmpty()) {
			throw new CategoryDtoValidationException(error);
		}
	}

	public void notesValidations(NotesDto notesDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(notesDto)) {
			throw new IllegalArgumentException("Notes Object/JSON should not be null or empty");
		} else {
			// Notes title field validations.
			if (ObjectUtils.isEmpty(notesDto.getTitle())) {
				error.put("title : ", "Title should not be null or empty");
			} else {
				if (notesDto.getTitle().length() < 3) {
					error.put("title", "title length should be minimum 3 chars");
				}
				if (notesDto.getTitle().length() > 30) {
					error.put("title", "title length should be maximum 30 chars");
				}
			}

			// Notes Description field validations.
			if (ObjectUtils.isEmpty(notesDto.getDescription())) {
				error.put("description : ", "Description should not be null or empty");
			} else {
				if (notesDto.getDescription().length() < 5) {
					error.put("description", "description length should be minimum 5 chars");
				}
				if (notesDto.getDescription().length() > 30) {
					error.put("description", "description length should be maximum 100 chars");
				}
			}

		}

		if (!error.isEmpty()) {
			throw new NotesDtoValidationException(error);
		}

	}

	public void toDoValidations(ToDoDto toDoDto) {
		StatusDto requestStatus = toDoDto.getStatus();
		ToDoStatus[] toDoStatusValues = ToDoStatus.values();
		
		Boolean statusFound = false;
		
		for(ToDoStatus st: toDoStatusValues) {
			if(st.getId().equals(requestStatus.getId())) {
				statusFound = true;
			}
		}
		
		if(!statusFound) {
			throw new ResourceNotFoundException("Invalid Status");
		}

	}
	
	
	public void userValidations(UserRequest userDto) {
		if(!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("Invalid First Name");
		}
		if(!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Invalid Last Name");
		}
		if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Invalid Email Id");
		}else {
			//validate email already exists or not
			Boolean emailExistsOrNot = userRepository.existsByEmail(userDto.getEmail());
			if(emailExistsOrNot) {
				throw new DataAlreadyExistsException("Email Already Exists !");
			}
		}
		
		if(!StringUtils.hasText(userDto.getMobileNo())|| !userDto.getMobileNo().matches(Constants.MOBILE_REGEX)) {
			throw new IllegalArgumentException("Invalid Mobile Number");
		}
		
		
		if(CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Invalid User Role");
		}else {
			List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getId()).toList();
			
			List<Integer> requestRoleIds = userDto.getRoles().stream().map(r -> r.getId()).filter(roleId -> !roleIds.contains(roleId)).toList();
			if(!CollectionUtils.isEmpty(requestRoleIds)) {
				throw new IllegalArgumentException("Invalid Role Id");
			}
		}
		
		
	}

}
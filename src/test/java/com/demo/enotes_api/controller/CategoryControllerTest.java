package com.demo.enotes_api.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.entity.Category;
import com.demo.enotes_api.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
	
	@Mock
	private CategoryService categoryService;
	
	@InjectMocks
	private CategoryController categoryController;
	
	private CategoryDto categoryDto = null;
	private Category category = null;
	private List<CategoryDto> categoriesDtoList = new ArrayList<>();
	private List<Category> categoriesList = new ArrayList<>();
	
	
	@BeforeEach
	public void initilize() {
		categoryDto = CategoryDto.builder()
					.id(null)
					.name("Java Programming")
					.description("Java notes for Students")
					.isActive(true)
					.build();
		
		category = Category.builder()
					.id(null)
					.name("Java Programming")
					.description("Java notes for Students")
					.isActive(true)
					.isDeleted(false)
					.build();
		
		categoriesDtoList.add(categoryDto);
		categoriesList.add(category);
	}
	
	@Test
	public void testSaveCategory() {
		when(categoryService.saveCategory(categoryDto)).thenReturn(true);
		ResponseEntity<?> response = categoryController.saveCategory(categoryDto);
		Object body = response.getBody();
		
		Map<String,String> json = (Map<String,String>)body;
		
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		Assertions.assertEquals(json.get("status"), "success");
		
	}
	
	@Test
	public void testCategoryNotSaved() {
		when(categoryService.saveCategory(categoryDto)).thenReturn(false);
		ResponseEntity<?> response = categoryController.saveCategory(categoryDto);
		Object body = response.getBody();
		
		Map<String,String> json = (Map<String,String>)body;
		
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
		Assertions.assertEquals(json.get("status"), "failed");
		
	}
}

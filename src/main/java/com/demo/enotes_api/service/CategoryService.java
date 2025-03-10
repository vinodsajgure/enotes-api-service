package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.dto.CategoryResponse;
import com.demo.enotes_api.exception.DataAlreadyExistsException;
import com.demo.enotes_api.exception.ResourceNotFoundException;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);
	
	public List<CategoryDto> getAllCategories();
	
	public List<CategoryResponse> getAllActiveCategories();

	public CategoryDto getCategoryById(Integer id) throws Exception;

	public Boolean deleteCategoryById(Integer id);
}

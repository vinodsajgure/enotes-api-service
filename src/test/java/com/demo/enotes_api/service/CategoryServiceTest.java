package com.demo.enotes_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.entity.Category;
import com.demo.enotes_api.exception.DataAlreadyExistsException;
import com.demo.enotes_api.repository.CategoryRepository;
import com.demo.enotes_api.service.impl.CategoryServiceImpl;
import com.demo.enotes_api.util.Validations;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	private CategoryDto categoryDto = null;
	private Category category = null;
	private List<CategoryDto> categoriesDtoList = new ArrayList<>();
	private List<Category> categoriesList = new ArrayList<>();
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private Validations validations;
	
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
		//arrange
		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
		when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);
		
		//act
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		
		//assert
		assertTrue(saveCategory);
		
		//verify
		verify(validations).categoryValidations(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(categoryRepository).save(category);
	}
	
	@Test
	public void testCategoryExists() {
		
		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(true);
		DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, ()->{
			categoryService.saveCategory(categoryDto);
		});
		
		assertEquals("Category already exists.", exception.getMessage());
		verify(validations).categoryValidations(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(categoryRepository,never()).save(category);
	}
	
	@Test
	public void testUpdateCategory() {
		categoryDto.setId(1);
		category.setId(1);
		
		//arrange
		when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
		when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
		when(categoryRepository.save(category)).thenReturn(category);
		
		//act
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		
		//assert
		assertTrue(saveCategory);
		
		//verify
		verify(validations).categoryValidations(categoryDto);
		verify(categoryRepository).existsByName(categoryDto.getName());
		verify(categoryRepository).save(category);
	}
	
	@Test
	public void testAllCategories() {
		
		when(categoryRepository.findByIsDeletedFalse()).thenReturn(categoriesList);
		List<CategoryDto> allCategories = categoryService.getAllCategories();
		
		assertEquals(allCategories.size(), categoriesList.size());
		verify(categoryRepository).findByIsDeletedFalse();
	}
}

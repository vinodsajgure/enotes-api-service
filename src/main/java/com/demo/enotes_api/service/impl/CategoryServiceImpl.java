 package com.demo.enotes_api.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.dto.CategoryResponse;
import com.demo.enotes_api.entity.Category;
import com.demo.enotes_api.exception.DataAlreadyExistsException;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.CategoryRepository;
import com.demo.enotes_api.service.CacheManagerService;
import com.demo.enotes_api.service.CategoryService;
import com.demo.enotes_api.util.Validations;


@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validations validations;
	
	@Autowired
	private CacheManagerService cacheManagerService;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
//      Validations checking
		
		validations.categoryValidations(categoryDto);
		
//		checking if category already exists or not
		
		Boolean categoryExistsOrNot = categoryRepository.existsByName(categoryDto.getName().trim());
		
		if(categoryExistsOrNot) {
			
			throw new DataAlreadyExistsException("Category already exists.");
		}
		

		Category category = mapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
//			category.setCreatedBy(1);
//			category.setCreatedDate(new Date());
		} else {

			updateCategory(category);
		}

		Category saveCategory = categoryRepository.save(category);
		if (!ObjectUtils.isEmpty(saveCategory)) {
			return true;
		}
		return false;
	}

	private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepository.findById(category.getId());
		if (findById.isPresent()) {
			Category existingCategory = findById.get();
			category.setCreatedBy(existingCategory.getCreatedBy());
			category.setCreatedDate(existingCategory.getCreatedDate());
			category.setIsDeleted(existingCategory.getIsDeleted());

//			category.setUpdatedBy(1);
//			category.setUpdatedOn(new Date());
		}

	}

	@Override
	@Cacheable("allCategory")
	public List<CategoryDto> getAllCategories() {
//		String st = null;
//		st.toUpperCase();
		List<Category> categories = categoryRepository.findByIsDeletedFalse();
		List<CategoryDto> categoriesDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class))
				.toList();
		return categoriesDtoList;
	}

	@Override
	@Cacheable("activeCategories")
	public List<CategoryResponse> getAllActiveCategories() {
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> categoriesDtoList = categories.stream()
				.map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return categoriesDtoList;
	}

	@Override
	@Cacheable(value="getCategoryById", key="#id" )
	public CategoryDto getCategoryById(Integer id) throws Exception {
		Category categoryById = categoryRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));
		if (!ObjectUtils.isEmpty(categoryById)) {
			return mapper.map(categoryById, CategoryDto.class);
		}
		return null;
	}

	@Override
	@CacheEvict(value="getCategoryById", key="#id" )
	public Boolean deleteCategoryById(Integer id) {
		Optional<Category> categoryById = categoryRepository.findById(id);
		if (categoryById.isPresent()) {
			Category category = categoryById.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			
			//remove from cache
			cacheManagerService.removeCacheByName(Arrays.asList("allCategory","activeCategories"));
			
			return true;
		}
		return false;
	}

}

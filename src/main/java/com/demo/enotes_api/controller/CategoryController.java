package com.demo.enotes_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.enotes_api.dto.CategoryDto;
import com.demo.enotes_api.dto.CategoryResponse;
import com.demo.enotes_api.endpoint.CategoryControllerEndPoint;
import com.demo.enotes_api.exception.DataAlreadyExistsException;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.service.CategoryService;
import com.demo.enotes_api.util.CommonUtil;

import jakarta.validation.Valid;

@RestController
public class CategoryController implements CategoryControllerEndPoint {

	@Autowired
	private CategoryService categoryService;


	@Override
	public ResponseEntity<?> saveCategory(CategoryDto categoryDto) {
		Boolean saveCategory = categoryService.saveCategory(categoryDto);

		if (saveCategory) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "saved successfully.");
//			return new ResponseEntity<>("saved successfully.", HttpStatus.CREATED);
		} else {
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "somthing went wrong"); 
//			return new ResponseEntity<>("somthing went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	@Override
	public ResponseEntity<?> getAllCategories() {
		List<CategoryDto> allCategories = categoryService.getAllCategories();
		if (!CollectionUtils.isEmpty(allCategories)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allCategories);
//			return new ResponseEntity<>(allCategories, HttpStatus.OK);
		} else {
			
			return ResponseEntity.noContent().build();
		}
	}


	@Override
	public ResponseEntity<?> getAllActiveCategories() {
		List<CategoryResponse> allCategories = categoryService.getAllActiveCategories();
		if (!CollectionUtils.isEmpty(allCategories)) {
//			return new ResponseEntity<>(allCategories, HttpStatus.OK);
			return CommonUtil.createBuildResponse(HttpStatus.OK, allCategories);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


	@Override
	public ResponseEntity<?> getCategoryDetailsById(Integer id) throws Exception {

		CategoryDto category = categoryService.getCategoryById(id);
		if (!ObjectUtils.isEmpty(category)) {
//			return new ResponseEntity<>(category, HttpStatus.OK);
			return CommonUtil.createBuildResponse(HttpStatus.OK, category);
		} else {
//			return new ResponseEntity<>("Category with id " + id + " not found", HttpStatus.NOT_FOUND);
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NOT_FOUND, "Category with id " + id + " not found");

		}

	}


	@Override
	public ResponseEntity<?> deleteCategoryById(Integer id) {
		Boolean deleted = categoryService.deleteCategoryById(id);
		if (deleted) {
//			return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
			return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Category deleted successfully.");
		} else {
//			return new ResponseEntity<>("Category with id " + id + " not deleted.", HttpStatus.INTERNAL_SERVER_ERROR);
			return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Category with id " + id + " not deleted.");
		}
	}
}

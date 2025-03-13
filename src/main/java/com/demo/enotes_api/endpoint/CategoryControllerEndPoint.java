package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.enotes_api.dto.CategoryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Category", description="All Category Operations APIs")
@RequestMapping("/api/v1/category")
public interface CategoryControllerEndPoint {

	@Operation(summary = "Save Category Endpoint",description = "Admin Save Category")
	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);
	
	@Operation(summary = "Get All Categories Endpoint",description = "Admin Get all Category")
	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllCategories();
	
	@Operation(summary = "Get All Active Categories Endpoint",description = "Admin, User Get all active Categories")
	@GetMapping("/ActiveCategories")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getAllActiveCategories();
	
	@Operation(summary = "Get Category By Id Endpoint",description = "Admin Get Category Details By Id")
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Delete Category By Id Endpoint",description = "Admin Delete Categor Detailsy By Id")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
}

package com.demo.enotes_api.exception;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demo.enotes_api.util.CommonUtil;
import com.demo.enotes_api.util.Validations;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
//		log.error("GlobalExceptionHandler :: handleException ::", e.getMessage());
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.FORBIDDEN,e.getMessage());
	}
	
	@ExceptionHandler(SuccessException.class)
	public ResponseEntity<?> handleSuccessException(SuccessException e) {
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK,e.getMessage());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		
		Map<String,Object> error = new LinkedHashMap<>();
		allErrors.stream().forEach(err ->{
			String msg = err.getDefaultMessage();
			String field = ((FieldError)(err)).getField();
			error.put(field, msg);
		});
		
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST,error);
	}
	
	@ExceptionHandler(CategoryDtoValidationException.class)
	public ResponseEntity<?> handleCategoryDtoValidationException(CategoryDtoValidationException e) {
//		return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getErrors());

	}
	
	
	@ExceptionHandler(DataAlreadyExistsException.class)
	public ResponseEntity<?> handleCategoryAlreadyExistsException(DataAlreadyExistsException e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.CONFLICT, e.getMessage());

	}
	
	@ExceptionHandler(NotesDtoValidationException.class)
	public ResponseEntity<?> handleNotesDtoValidationException(NotesDtoValidationException e) {
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());

	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
		return CommonUtil.createBuildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());

	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());

	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleUsernameNotFoundException(BadCredentialsException e) {
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());

	}

}

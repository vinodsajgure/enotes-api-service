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

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		log.error("GlobalExceptionHandler : handleException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		log.error("GlobalExceptionHandler : handleAccessDeniedException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.FORBIDDEN,e.getMessage());
	}
	
	@ExceptionHandler(SuccessException.class)
	public ResponseEntity<?> handleSuccessException(SuccessException e) {
		log.error("GlobalExceptionHandler : handleSuccessException() : {}", e.getMessage());
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK,e.getMessage());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(Exception e) {
		log.error("GlobalExceptionHandler : handleNullPointerException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception e) {
		log.error("GlobalExceptionHandler : handleResourceNotFoundException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("GlobalExceptionHandler : handleMethodArgumentNotValidException() : {}", e.getMessage());
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		
		Map<String,Object> error = new LinkedHashMap<>();
		allErrors.stream().forEach(err ->{
			String msg = err.getDefaultMessage();
			String field = ((FieldError)(err)).getField();
			error.put(field, msg);
		});
		
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST,error);
	}
	
	@ExceptionHandler(CategoryDtoValidationException.class)
	public ResponseEntity<?> handleCategoryDtoValidationException(CategoryDtoValidationException e) {
		log.error("GlobalExceptionHandler : handleCategoryDtoValidationException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getErrors());

	}
	
	
	@ExceptionHandler(DataAlreadyExistsException.class)
	public ResponseEntity<?> handleCategoryAlreadyExistsException(DataAlreadyExistsException e) {
		log.error("GlobalExceptionHandler : handleCategoryAlreadyExistsException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.CONFLICT, e.getMessage());

	}
	
	@ExceptionHandler(NotesDtoValidationException.class)
	public ResponseEntity<?> handleNotesDtoValidationException(NotesDtoValidationException e) {
		log.error("GlobalExceptionHandler : handleNotesDtoValidationException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());

	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
		log.error("GlobalExceptionHandler : handleFileNotFoundException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());

	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("GlobalExceptionHandler : handleIllegalArgumentException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());

	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleUsernameNotFoundException(BadCredentialsException e) {
		log.error("GlobalExceptionHandler : handleUsernameNotFoundException() : {}", e.getMessage());
		return CommonUtil.createBuildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());

	}

}

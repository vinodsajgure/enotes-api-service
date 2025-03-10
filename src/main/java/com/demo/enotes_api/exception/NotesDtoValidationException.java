package com.demo.enotes_api.exception;

import java.util.Map;

public class NotesDtoValidationException extends RuntimeException {
	
	private Map<String, Object> error;

	public NotesDtoValidationException(Map<String, Object> error) {
		super("Validations Failed");
		this.error = error;
	}
	
	public Map<String, Object> getErrors(){
		return error;
	}
}

package com.demo.enotes_api.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import com.demo.enotes_api.config.security.CustomUserDetails;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.response_handler.GenericResponse;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {
	
		public static ResponseEntity<?> createBuildResponse(HttpStatus status, Object data){
		
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("success")
				.message("success")
				.data(data)
				.build();
		return response.createResponse();
				
	}
	
		public static ResponseEntity<?> createBuildResponseMessage(HttpStatus status,String message){
		
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("success")
				.message(message)
				.build();
		return response.createResponse();
				
	}
	
    	public static ResponseEntity<?> createBuildErrorResponse(HttpStatus status, Object data){
		
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("failed")
				.message("failed")
				.data(data)
				.build();
		return response.createResponse();
				
	}
    		
    	public static ResponseEntity<?> createBuildErrorResponseMessage(HttpStatus status,String message){
    		
    		GenericResponse response = GenericResponse.builder()
    				.responseStatus(status)
    				.status("failed")
    				.message(message)
    				.build();
    		return response.createResponse();
    				
    	}

		public static String getContentType(String originalFileName) {
			String extension = FilenameUtils.getExtension(originalFileName); // java_programing.pdf

			switch (extension) {
			case "pdf":
				return "application/pdf";
			case "xlsx":
				return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
			case "txt":
				return "text/plan";
			case "png":
				return "image/png";
			case "jpeg":
				return "image/jpeg";
			default:
				return "application/octet-stream";
			}
		}

		public static String getUrl(HttpServletRequest request) {
			String apiUrl = request.getRequestURL().toString();   //http://localhost:8085/api/v1/user
			apiUrl = apiUrl.replace(request.getServletPath(), ""); //http://localhost:8085
			return apiUrl;
		}
		
		
		public static User getLoggedInUser() {
			try {
				CustomUserDetails loggedInUser =(CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				return loggedInUser.getUser();
			} catch (Exception e) {
				throw e;
			}
		}

}

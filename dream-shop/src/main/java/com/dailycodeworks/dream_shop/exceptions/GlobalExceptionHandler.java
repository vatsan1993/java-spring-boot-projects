package com.dailycodeworks.dream_shop.exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// handles forbidden operation
	// AccessDeniedException is from java.nio
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handResponseEntity(AccessDeniedException ex){
		String message = "You do not have permission to this action";
		return new ResponseEntity(message, HttpStatus.FORBIDDEN);
		
	}
}

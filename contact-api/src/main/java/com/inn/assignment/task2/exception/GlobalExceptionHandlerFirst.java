package com.inn.assignment.task2.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandlerFirst {

	@Data
	@AllArgsConstructor
	private static class ErrorResponse {
		private int status;
		private String message;
		private LocalDateTime timestamp;
		private List<String> errors;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "validation error", LocalDateTime.now(),
				errors);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BuisnessException.class)
	public ResponseEntity<Object> handleFException(BuisnessException ex) {
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now(),
				Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<Object> handleGeneralExceptions(Exception ex) {
		ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
				LocalDateTime.now(), Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<Object> handleRuntimeExceptions(RuntimeException e) {
		ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),
				LocalDateTime.now(), Collections.singletonList(e.getMessage()));
		return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
		ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now(),
				Collections.singletonList(e.getMessage()));
		return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
	}

}

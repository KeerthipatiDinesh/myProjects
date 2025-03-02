package com.QuizManagementSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle validation exceptions
	@ExceptionHandler(org.springframework.validation.BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidationException(BindException ex, Model model) {
		model.addAttribute("error", "Validation failed. Please check your inputs.");
		model.addAttribute("details", ex.getAllErrors());
		return "validation-error"; // Custom error page
	}

	// Handle generic exceptions
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGenericException(Exception ex, Model model) {
		model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
		return "generic-error"; // Custom error page
	}
}
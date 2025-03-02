package com.QuizManagementSystem.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.service.ResultService;

@Controller
@RequestMapping("/results")
public class ResultController {

	@Autowired
	private ResultService resultService;

	// Fetch all results for the authenticated user
	@GetMapping("/user")
	public ResponseEntity<List<Result>> getUserResults(Principal principal) {
		String username = principal.getName();
		List<Result> results = resultService.getUserResults(username);
		return ResponseEntity.ok(results);
	}

	// Fetch all results (Admin only)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Result>> getAllResults() {
		List<Result> results = resultService.getAllResults();
		return ResponseEntity.ok(results);
	}
}

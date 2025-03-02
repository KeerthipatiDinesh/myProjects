package com.QuizManagementSystem.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.service.PdfReportService;
import com.QuizManagementSystem.service.ResultService;


@Controller
public class ReportController {

	@Autowired
	private PdfReportService pdfReportService;

	@Autowired
	private ResultService resultService;

	

	@GetMapping("/results/pdf")
	public ResponseEntity<byte[]> downloadResultPdf() {
		String username = getLoggedInUsername(); // Get the logged-in username
		List<Result> results = resultService.getResultsForLoggedInUser(username);

		if (results.isEmpty()) {
			// Handle case where no results are found
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results found.".getBytes());
		}



		ByteArrayInputStream pdfStream = pdfReportService.generateResultPdf(results); // Generate the PDF for this
																						// result

		byte[] pdfBytes = pdfStream.readAllBytes();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=quiz_result.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

	// Method to get the logged-in user's username
	private String getLoggedInUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName(); // Returns the logged-in user's username
	}

	@GetMapping("/admin/all-results/pdf")
	public ResponseEntity<byte[]> downloadAllResultsPdf() {
		// Fetch all results from the database
		List<Result> results = resultService.getAllResults();

		if (results.isEmpty()) {
			// Handle the case where no results are found
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results available.".getBytes());
		}

		// Generate PDF for all results
		ByteArrayInputStream pdfStream = pdfReportService.generateAllResultsPdf(results);

		byte[] pdfBytes = pdfStream.readAllBytes();

		// Set the response headers to download the PDF
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=all_quiz_results.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}
}



 package com.QuizManagementSystem.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.QuizManagementSystem.model.Result;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfReportService {
//for user
	public ByteArrayInputStream generateResultPdf(List<Result> results) {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// Add a title to the PDF
			document.add(new Paragraph("Quiz Results", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));

			for (Result result : results) {
				// Handle null values for username and quiz title gracefully
				String username = (result.getUser() != null && result.getUser().getUsername() != null)
						? result.getUser().getUsername()
						: "Unknown User";

				String quizTitle = (result.getQuiz() != null && result.getQuiz().getTitle() != null)
						? result.getQuiz().getTitle()
						: "Unknown Quiz";

				Integer score = (result.getScore() != null) ? result.getScore() : 0;

				// Add the result details to the PDF
				document.add(new Paragraph(String.format("Username: %s", username)));
				document.add(new Paragraph(String.format("Quiz: %s", quizTitle)));
				document.add(new Paragraph(String.format("Score: %d", score)));
				document.add(new Paragraph(" ")); // Add some spacing between entries
			}

			// Close the document
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}



	public ByteArrayInputStream generateAllResultsPdf(List<Result> results) {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// Add a title to the PDF
			document.add(new Paragraph("All Quiz Results Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
			document.add(new Paragraph(" ")); // Add some spacing

			// Table to display results in tabular format
			PdfPTable table = new PdfPTable(3); // 3 columns: User, Quiz, Score
			table.setWidthPercentage(100);

			// Add table header
			Stream.of("User", "Quiz", "Score").forEach(headerTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setPhrase(new Phrase(headerTitle));
				table.addCell(header);
			});

			// Loop through all results and add them to the table
			for (Result result : results) {
				String username = (result.getUser() != null && result.getUser().getUsername() != null)
						? result.getUser().getUsername()
						: "Unknown User";

				String quizTitle = (result.getQuiz() != null && result.getQuiz().getTitle() != null)
						? result.getQuiz().getTitle()
						: "Unknown Quiz";

				String score = (result.getScore() != null) ? result.getScore().toString() : "0";

//				System.out.println("User: " + result.getUser());
//				System.out.println("Quiz: " + result.getQuiz());
//				System.out.println("Score: " + result.getScore());

				table.addCell(username);
				table.addCell(quizTitle);
				table.addCell(score);
			}

			// Add table to the document
			document.add(table);

			// Close the document
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}

////	For admin
//	public ByteArrayInputStream generateAllResultsPdf(List<Result> results) {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Add a title to the PDF
//			document.add(new Paragraph("All Quiz Results Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//
//			// Loop through all results and add them to the document
//			for (Result result : results) {
//				String username = (result.getUser() != null && result.getUser().getUsername() != null)
//						? result.getUser().getUsername()
//						: "Unknown User";
//
//				String quizTitle = (result.getQuiz() != null && result.getQuiz().getTitle() != null)
//						? result.getQuiz().getTitle()
//						: "Unknown Quiz";
//
//				Integer score = (result.getScore() != null) ? result.getScore() : 0;
//
//				// Add each result to the PDF
//				document.add(
//						new Paragraph(String.format("User: %s | Quiz: %s | Score: %d", username, quizTitle, score)));
//			}
//
//			// Close the document
//			document.close();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//	}

//	public ByteArrayInputStream generateResultPdf(Result result) {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Add a title to the PDF
//			document.add(new Paragraph("Quiz Results", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//
//			// Get the user details and quiz details from the result
//			String username = result.getUser() != null ? result.getUser().getUsername() : "Unknown User";
//			String quizTitle = result.getQuiz() != null ? result.getQuiz().getTitle() : "Unknown Quiz";
//			Integer score = result.getScore() != null ? result.getScore() : 0;
//
//			// Add the result details to the PDF
//			document.add(new Paragraph(String.format("Username: %s", username)));
//			document.add(new Paragraph(String.format("Quiz: %s", quizTitle)));
//			document.add(new Paragraph(String.format("Score: %d", score)));
//
//			// Close the document
//			document.close();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//	}

//	public ByteArrayInputStream generateResultPdf(List<Result> results) {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Add a title to the PDF
//			document.add(new Paragraph("Results Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//
//			for (Result result : results) {
//				// Safely retrieve user and quiz title, defaulting to "Unknown" if null
//				String username = (result.getUser() != null && result.getUser().getUsername() != null)
//						? result.getUser().getUsername()
//						: "Unknown User";
//				String quizTitle = (result.getQuiz() != null && result.getQuiz().getTitle() != null)
//						? result.getQuiz().getTitle()
//						: "Unknown Quiz";
//
//				// Ensure score is not null; default to 0 if it is
//				int score = (result.getScore() != null) ? result.getScore() : 0;
//
//				 System.out.println("Result Details: ");
//		            System.out.println("Username: " + username);
//		            System.out.println("Quiz Title: " + quizTitle);
//		            System.out.println("Score: " + score);
//				// Add the result to the PDF
//				document.add(
//						new Paragraph(String.format("User: %s | Quiz: %s | Score: %d", username, quizTitle, score)));
//			}
//
//			document.close();
//		} catch (DocumentException e) {
//			e.printStackTrace(); // Log exception or handle it as needed
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//	}

//	public ByteArrayInputStream generateResultPdf(List<Result> results) {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Add a title to the PDF
//			document.add(new Paragraph("Results Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//
//			for (Result result : results) {
//				String username = result.getUser() != null ? result.getUser().getUsername() : "Unknown User";
//				String quizTitle = result.getQuiz() != null ? result.getQuiz().getTitle() : "Unknown Quiz";
//
//				document.add(new Paragraph(
//						String.format("User: %s | Quiz: %s | Score: %d", username, quizTitle, result.getScore())));
//			}
//
//			document.close();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//	}

//	public ByteArrayInputStream generateResultPdf(List<Result> results) {
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// Add Title
//			Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
//			Paragraph title = new Paragraph("User Results Report", titleFont);
//			title.setAlignment(Element.ALIGN_CENTER);
//			document.add(title);
//			document.add(Chunk.NEWLINE);
//
//			// Add Table
//			PdfPTable table = new PdfPTable(4);
//			table.setWidthPercentage(100);
//			table.setWidths(new int[] { 4, 4, 2, 2 });
//
//			// Add Table Header
//			Stream.of("Username", "Quiz Title", "Score", "Total Questions").forEach(header -> {
//				PdfPCell cell = new PdfPCell();
//				cell.setPhrase(new Phrase(header));
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cell);
//			});
//
//			// Add Table Rows
//			for (Result result : results) {
//				table.addCell(result.getUser().getUsername());
//				table.addCell(result.getQuiz().getTitle());
//				table.addCell(String.valueOf(result.getScore()));
//				table.addCell(String.valueOf(result.getQuiz().getQuestions()));
//			}
//
//			document.add(table);
//
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} finally {
//			document.close();
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//	}
	
////for user
//public ByteArrayInputStream generateResultPdf(Result result) {
//	Document document = new Document();
//	ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//	try {
//		PdfWriter.getInstance(document, out);
//		document.open();
//
//		// Add a title to the PDF
//		document.add(new Paragraph("Quiz Results", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//
//		// Get the user details and quiz details from the result
//		String username = result.getUser().getUsername() != null ? result.getUser().getUsername() : "Unknown User";
//
//		// Correctly fetch the quiz name associated with the result
//		String quizTitle = result.getQuiz().getTitle() != null ? result.getQuiz().getTitle() : "Unknown Quiz";
//
//		// Get the score (if not present, set it to 0)
//		Integer score = result.getScore() != null ? result.getScore() : 0;
//
//		System.out.println("Quiz Title: " + result.getQuiz().getTitle()); // Log the quiz title to check
//		System.out.println("Quiz Score: " + result.getScore()); // Log the quiz title to check
//
//		// Add the result details to the PDF
//		document.add(new Paragraph(String.format("Username: %s", username)));
//		document.add(new Paragraph(String.format("Quiz: %s", quizTitle))); // Correct quiz title
//		document.add(new Paragraph(String.format("Score: %d", score)));
//
//		// Close the document
//		document.close();
//	} catch (DocumentException e) {
//		e.printStackTrace();
//	}
//
//	return new ByteArrayInputStream(out.toByteArray());
//}


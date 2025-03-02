package com.QuizManagementSystem.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.service.QuestionService;
import com.QuizManagementSystem.service.QuizService;

@Controller

public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuizService quizService;

	@GetMapping("/addQuestion/{quizId}")
	public String addQuestionForm(@PathVariable Long quizId, Model model) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
		if (quizOptional.isPresent()) {
			Question question = new Question();
			question.setQuiz(quizOptional.get());
			model.addAttribute("quiz", quizOptional.get());
			model.addAttribute("question", question);
			return "admin/add-question";
		} else {
			model.addAttribute("error", "Quiz not found for ID: " + quizId);
			return "generic-error";
		}
	}

	@PostMapping("/addQuestion")
	public String addQuestion(@ModelAttribute Question question, Model model) {
		// Validate that the correct answer matches one of the provided options
		String correctAnswer = question.getCorrectAnswer();
		if (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C")
				&& !correctAnswer.equals("D")) {
			model.addAttribute("error", "Invalid correct answer selection. Please select a valid option.");
			return "admin/add-question"; 
		}

		// Save the question to the database with the correct answer as the option key
		// (e.g., A, B, C, D)
		question.setCorrectAnswer(correctAnswer);

		// Save question to the database
		questionService.addQuestion(question);
		return "redirect:/list-quizzes";
	}

	@GetMapping("/show-questions/{quizId}")
	public String showQuestions(@PathVariable Long quizId, Model model) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
		if (quizOptional.isPresent()) {
			Quiz quiz = quizOptional.get();
			model.addAttribute("quiz", quiz);
			model.addAttribute("questions", quiz.getQuestions()); // Assuming `getQuestions()` returns a list of
																	// questions
			return "admin/show-questions"; 
		} else {
			model.addAttribute("error", "Quiz not found for ID: " + quizId);
			return "generic-error";
		}
	}

	@GetMapping("/edit-question/{quizId}/{questionId}")
	public String editQuestion(@PathVariable Long quizId, @PathVariable Long questionId, Model model) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
		Optional<Question> questionOptional = questionService.getQuestionById(questionId);

		if (quizOptional.isPresent() && questionOptional.isPresent()) {
			model.addAttribute("quizId", quizId);
			model.addAttribute("question", questionOptional.get());
			return "admin/edit-question";
		} else {
			model.addAttribute("error", "Quiz or Question not found.");
			return "generic-error";
		}
	}

	@PostMapping("/edit-question/{quizId}/{questionId}")
	public String saveEditedQuestion(@PathVariable Long questionId, @ModelAttribute Question updatedQuestion,
			Model model, @PathVariable Long quizId) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
		Optional<Question> existingQuestionOptional = questionService.getQuestionById(questionId);
		if (existingQuestionOptional.isPresent()) {
			Question existingQuestion = existingQuestionOptional.get();

			// Validate that the correct answer is one of the options (A, B, C, or D)
			if (!"A".equalsIgnoreCase(updatedQuestion.getCorrectAnswer())
					&& !"B".equalsIgnoreCase(updatedQuestion.getCorrectAnswer())
					&& !"C".equalsIgnoreCase(updatedQuestion.getCorrectAnswer())
					&& !"D".equalsIgnoreCase(updatedQuestion.getCorrectAnswer())) {
				model.addAttribute("error", "Correct answer must be one of the options (A, B, C, or D).");
				return "admin/edit-question";
			}

			// Update the question details
			existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
			existingQuestion.setOptionA(updatedQuestion.getOptionA());
			existingQuestion.setOptionB(updatedQuestion.getOptionB());
			existingQuestion.setOptionC(updatedQuestion.getOptionC());
			existingQuestion.setOptionD(updatedQuestion.getOptionD());
			existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());

			// Save the updated question to the database
			questionService.addQuestion(existingQuestion);

			model.addAttribute("message", "Question updated successfully");
			return "redirect:/show-questions/" + quizId;
//			
		} else {
			model.addAttribute("error", "Question not found for ID: " + questionId);
			return "generic-error";
		}
	}

	@PostMapping("/delete-question/{questionId}")
	public String deleteQuestion(@PathVariable Long questionId, @RequestParam Long quizId) {
		questionService.deleteQuestionById(questionId);
		return "redirect:/admin/show-questions/" + quizId;
	}
}



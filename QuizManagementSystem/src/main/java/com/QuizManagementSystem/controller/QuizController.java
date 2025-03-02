package com.QuizManagementSystem.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.model.User;
import com.QuizManagementSystem.repository.ResultRepository;
import com.QuizManagementSystem.service.QuizService;
import com.QuizManagementSystem.service.UserService;

import jakarta.validation.Valid;

@Controller

public class QuizController {

	@Autowired
	private UserService userService;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private QuizService quizService;

	@GetMapping("/create-quiz")
	public String showQuizForm(Model model) {
		model.addAttribute("quiz", new Quiz());
		return "admin/create-quiz"; 
	}

	@PostMapping("/create-quiz")
	public String createQuiz(@Valid @ModelAttribute Quiz quiz, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "admin/create-quiz";
		}
		quizService.createQuiz(quiz);
		model.addAttribute("message", "Quiz created successfully!");
		return "redirect:/list-quizzes";
	}

	@GetMapping("/list-quizzes")
	public String listQuizzes(Model model) {
		model.addAttribute("quizzes", quizService.getAllQuizzes());
		return "admin/quiz-list"; 
	}

	@GetMapping("/list")
	public String listQuizze(Model model) {
		List<Quiz> quizzes = quizService.getAllQuizzes();
		model.addAttribute("quizzes", quizzes);
		return "user/quiz-list"; 
	}

	// Start quiz
	@GetMapping("/quiz/start/{quizId}")
	public String startQuiz(@PathVariable Long quizId, Model model) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);

		// If the quiz is not found
		if (quizOptional.isEmpty()) {
			model.addAttribute("error", "Quiz not found");
			return "generic-error"; 
		}

		Quiz quiz = quizOptional.get(); // Get the quiz object from Optional

		List<Question> questions = quizService.getQuestionsByQuizId(quizId);
		// If there are no questions for the quiz
		if (questions.isEmpty()) {
			model.addAttribute("error", "No questions available for this quiz");
			return "generic-error"; 
		}

		model.addAttribute("quiz", quiz);
		model.addAttribute("questions", questions);
		return "user/quiz-start";
	}

	@PostMapping("/quiz/submit")
	public String submitQuiz(@RequestParam Long quizId, @RequestParam Map<String, String> answers, Principal principal,
			Model model) {

		// Fetch the quiz by ID
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);

		if (quizOptional.isEmpty()) {
			model.addAttribute("error", "Quiz not found.");
			return "generic-error";
		}

		Quiz quiz = quizOptional.get();
		List<Question> questions = quiz.getQuestions();

		int score = 0;
		int unansweredCount = 0;
		int incorrectCount = 0;

		for (Question question : questions) {
			// Use the updated key format: "answers_" + question ID
			String userAnswer = answers.get("answers_" + question.getId());
			System.out.println("Validating Question ID: " + question.getId() + ", User Answer: " + userAnswer
					+ ", Correct Answer: " + question.getCorrectAnswer());


			if (userAnswer == null || userAnswer.trim().isEmpty()) {
				unansweredCount++;
			} else if (userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
				score++;
			} else {
				incorrectCount++;
			}
		}
		if (principal == null || principal.getName() == null) {
			model.addAttribute("error", "User authentication failed.");
			return "generic-error";
		}

		// Save the result to the database
		User currentUser = userService.findByUsername(principal.getName());
		if (currentUser == null) {
			model.addAttribute("error", "User not found.");
			return "generic-error";
		}
		Result result = new Result();
		result.setQuiz(quiz);
		result.setUser(currentUser);
		result.setScore(score);

		resultRepository.save(result);

		// Add quiz title, username, score, and total questions to the model
		model.addAttribute("quizTitle", quiz.getTitle()); // Assuming the Quiz entity has a `getTitle()` method
		model.addAttribute("username", currentUser.getUsername()); // Assuming User entity has a `getUsername()` method
		model.addAttribute("score", score);
		model.addAttribute("totalQuestions", questions.size());
		model.addAttribute("unansweredCount", unansweredCount);
		model.addAttribute("incorrectCount", incorrectCount);
		model.addAttribute("answeredCount", questions.size() - unansweredCount);
		return "user/result";
	}

	@GetMapping("/edit-quiz/{quizId}")
	public String editQuiz(@PathVariable Long quizId, Model model) {
		Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
		if (quizOptional.isPresent()) {
			model.addAttribute("quiz", quizOptional.get());
			return "admin/edit-quiz"; // Refers to the Thymeleaf template `edit-quiz.html`
		} else {
			model.addAttribute("error", "Quiz not found for ID: " + quizId);
			return "generic-error";
		}
	}

	@PostMapping("/edit-quiz")
	public String updateQuiz(@ModelAttribute Quiz quiz) {
		quizService.updateQuiz(quiz);
		return "redirect:/list-quizzes";
	}

	// Delete a Quiz
	@GetMapping("/delete-quiz/{quizId}")
	public String deleteQuiz(@PathVariable Long quizId) {
		quizService.deleteQuizById(quizId);
		return "redirect:/list-quizzes";
	}
}





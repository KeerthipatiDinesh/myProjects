package com.QuizManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.service.QuizService;

@Controller
//@RequestMapping("/user")
public class UserController {

	@Autowired
	private QuizService quizService;

	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user/user-dashboard";
	}

	@GetMapping("user/quizzes/list")
	public String listQuizzesForUser(Model model) {
		model.addAttribute("quizzes", quizService.getAllQuizzes());
		return "quiz-list";
	}

	@GetMapping("user/quizzes/{id}")
	public String attemptQuiz(@PathVariable Long id, Model model) {
		Quiz quiz = quizService.getQuizById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
		model.addAttribute("quiz", quiz);
		return "quiz-attempt";
	}
}

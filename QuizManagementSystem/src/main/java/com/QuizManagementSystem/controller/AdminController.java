package com.QuizManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.QuizManagementSystem.service.QuizService;

@Controller
//@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private QuizService quizService;
	
	 @GetMapping("/admin/dashboard")
	    public String adminDashboard() {
	        return "admin/admin-dashboard";
	    }

	    @GetMapping("admin/quizzes/create")
	    public String createQuizPage() {
	        return "quiz-create";
	    }

	    @GetMapping("/admin/quizzes/list")
	    public String listQuizzes(Model model) {
	        model.addAttribute("quizzes", quizService.getAllQuizzes());
	        return "quiz-list";
	    }
}

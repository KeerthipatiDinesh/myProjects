package com.QuizManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.QuizManagementSystem.model.User;
import com.QuizManagementSystem.service.UserService;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public String saveUser( User user) {
		 userService.register(user);
		 return "redirect:/login";
	}
	
	@GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
    
    
}

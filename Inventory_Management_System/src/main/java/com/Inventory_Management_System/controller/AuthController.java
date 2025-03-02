package com.Inventory_Management_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory_Management_System.model.Users;
import com.Inventory_Management_System.service.SessionService;
import com.Inventory_Management_System.service.UsersService;

import jakarta.servlet.http.HttpSession;

@RestController
public class AuthController {

	@Autowired
	private SessionService sessionService;
	@Autowired
	private UsersService usersService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Users requestUser, HttpSession session) {
		if (usersService.validateCredentials(requestUser.getUsername(), requestUser.getPassword())) {
			if (sessionService.isUserLoggedIn(requestUser.getUsername())) {
				return ResponseEntity.badRequest().body("User already logged in");
			}

			sessionService.handleLogin(requestUser.getUsername(), session);
			return ResponseEntity.ok().header("X-Auth-Token", session.getId()).body("Login successful");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		sessionService.handleLogout(session);
		return ResponseEntity.ok("Logged out successfully");
	}
}

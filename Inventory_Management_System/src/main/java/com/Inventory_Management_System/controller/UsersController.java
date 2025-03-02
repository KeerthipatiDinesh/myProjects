package com.Inventory_Management_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory_Management_System.model.Users;
import com.Inventory_Management_System.service.UsersService;

@RestController
@RequestMapping("/user")
public class UsersController {

	 @Autowired
	    private UsersService usersService;

	 @PostMapping("/adduser")
	    public ResponseEntity<String> registerUser(@RequestBody Users user) {
	        usersService.registerUser(user);
	        return ResponseEntity.ok("User registered successfully");
	    }

	    @GetMapping("/{username}")
	    public ResponseEntity<Users> getUser(@PathVariable String username) {
	        return ResponseEntity.ok(usersService.getUserByUsername(username));
	    }
}

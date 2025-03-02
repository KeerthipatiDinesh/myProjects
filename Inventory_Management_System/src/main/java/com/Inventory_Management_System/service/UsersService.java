package com.Inventory_Management_System.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Inventory_Management_System.exception.UserAlreadyExistsException;
import com.Inventory_Management_System.model.Users;
import com.Inventory_Management_System.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@CachePut(cacheNames = "users", key = "#user.username")
	public void registerUser(Users user) {
		if (usersRepository.findByUsername(user.getUsername()) != null) {
			throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
	}

	@Cacheable(cacheNames = "users", key = "#username")
	public Users getUserByUsername(String username) {
		Users user = usersRepository.findByUsername(username);
		if (user == null) {
			throw new RuntimeException("User not found: " + username);
		}
		return user;
	}

	public boolean validateCredentials(String username, String password) {
		Users user = usersRepository.findByUsername(username);
		if (user == null) {
			return false;
		}
		return passwordEncoder.matches(password, user.getPassword());
	}
}

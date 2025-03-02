package com.QuizManagementSystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.model.User;
import com.QuizManagementSystem.repository.ResultRepository;
import com.QuizManagementSystem.repository.UserRepository;

@Service
public class ResultService {

	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
    private UserRepository userRepository;

	


	public List<Result> getUserResults(String username) {
	    return resultRepository.findByUserUsername(username);
	}
	
	public List<Result> getResultsForLoggedInUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);  // Fetch the logged-in user
        if (user != null) {
            return resultRepository.findByUser(user);  // Get results for this user
        }
        return new ArrayList<>();
    }


    
    public List<Result> getAllResults() {
        return resultRepository.findAllWithUserAndQuiz();
    }

    
    
}

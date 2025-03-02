package com.QuizManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	public void addQuestion(Question question) {
		questionRepository.save(question);
	}

	public void deleteQuestionById(Long questionId) {
		questionRepository.deleteById(questionId);
	}

	public Optional<Question> getQuestionById(Long questionId) {
		return questionRepository.findById(questionId);
	}
	
	 public List<Question> getQuestionsByQuizId(Long quizId) {
	        return questionRepository.findByQuizId(quizId);
	    }
}

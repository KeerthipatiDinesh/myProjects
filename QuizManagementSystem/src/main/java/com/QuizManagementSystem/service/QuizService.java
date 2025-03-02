package com.QuizManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.repository.QuestionRepository;
import com.QuizManagementSystem.repository.QuizRepository;

@Service
public class QuizService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuizRepository quizRepository;

	public Quiz createQuiz(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public Optional<Quiz> getQuizById(Long id) {
		return quizRepository.findById(id);
	}

	public List<Question> getQuestionsByQuizId(Long quizId) {
		return questionRepository.findByQuizId(quizId);
	}

	public Quiz updateQuiz(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	public void deleteQuizById(Long id) {
		quizRepository.deleteById(id);
	}
}

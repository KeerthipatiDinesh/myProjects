package com.QuizManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.repository.QuestionRepository;

@SpringBootTest
public class QuestionServiceTest {

	@Autowired
	private QuestionService questionService;

	@MockBean
	private QuestionRepository questionRepository;

	private Question question;

	@BeforeEach
	public void setUp() {
		question = new Question();
		question.setId(1L);
		question.setQuestionText("What is 2 + 2?");
		question.setOptionA("3");
		question.setOptionB("4");
		question.setOptionC("5");
		question.setOptionD("6");
		question.setCorrectAnswer("B");
	}

	@Test
	public void testAddQuestion() {
		// Mock the repository behavior
		when(questionRepository.save(any(Question.class))).thenReturn(question);

		// Call the service method
		questionService.addQuestion(question);

		// Verify that the save method was called
		verify(questionRepository, times(1)).save(question);
	}

	@Test
	public void testDeleteQuestionById() {
		// Mock the delete behavior
		doNothing().when(questionRepository).deleteById(anyLong());

		// Call the service method
		questionService.deleteQuestionById(1L);

		// Verify that the delete method was called
		verify(questionRepository, times(1)).deleteById(1L);
	}

	@Test
	public void testGetQuestionById() {
		// Mock the repository behavior
		when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

		// Call the service method
		Optional<Question> result = questionService.getQuestionById(1L);

		// Verify that the correct question is returned
		assertTrue(result.isPresent());
		assertEquals("What is 2 + 2?", result.get().getQuestionText());
	}

	@Test
	public void testGetQuestionByIdNotFound() {
		// Mock the repository behavior to return an empty Optional
		when(questionRepository.findById(1L)).thenReturn(Optional.empty());

		// Call the service method
		Optional<Question> result = questionService.getQuestionById(1L);

		// Verify that the result is empty
		assertFalse(result.isPresent());
	}

	@Test
	public void testGetQuestionsByQuizId() {
		// Mock the repository behavior
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		when(questionRepository.findByQuizId(1L)).thenReturn(questions);

		// Call the service method
		List<Question> result = questionService.getQuestionsByQuizId(1L);

		// Verify that the questions list is returned
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("What is 2 + 2?", result.get(0).getQuestionText());
	}

	@Test
	public void testGetQuestionsByQuizIdEmpty() {
		// Mock the repository behavior to return an empty list
		when(questionRepository.findByQuizId(1L)).thenReturn(Collections.emptyList());

		// Call the service method
		List<Question> result = questionService.getQuestionsByQuizId(1L);

		// Verify that the list is empty
		assertTrue(result.isEmpty());
	}
}

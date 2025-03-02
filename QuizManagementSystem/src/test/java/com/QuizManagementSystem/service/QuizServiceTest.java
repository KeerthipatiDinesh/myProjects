package com.QuizManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.repository.QuestionRepository;
import com.QuizManagementSystem.repository.QuizRepository;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService quizService;

	@MockBean
	private QuizRepository quizRepository;

	@MockBean
	private QuestionRepository questionRepository;

	private Quiz quiz;
	private Question question;

	 
	@BeforeEach
	public void setUp() {
		quiz = new Quiz();
		quiz.setId(1L);
		quiz.setTitle("General Knowledge Quiz");

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
	public void testCreateQuiz() {
		// Mock the repository behavior
		when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

		// Call the service method
		Quiz result = quizService.createQuiz(quiz);

		// Verify the quiz was saved and returned
		assertNotNull(result);
		assertEquals("General Knowledge Quiz", result.getTitle());
		verify(quizRepository, times(1)).save(quiz);
	}

	@Test
	public void testGetAllQuizzes() {
		// Mock the repository behavior
		List<Quiz> quizzes = new ArrayList<>();
		quizzes.add(quiz);
		when(quizRepository.findAll()).thenReturn(quizzes);

		// Call the service method
		List<Quiz> result = quizService.getAllQuizzes();

		// Verify the quizzes list is returned
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("General Knowledge Quiz", result.get(0).getTitle());
	}

	@Test
	public void testGetQuizById() {
		// Mock the repository behavior
		when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

		// Call the service method
		Optional<Quiz> result = quizService.getQuizById(1L);

		// Verify the correct quiz is returned
		assertTrue(result.isPresent());
		assertEquals("General Knowledge Quiz", result.get().getTitle());
	}

	@Test
	public void testGetQuizByIdNotFound() {
		// Mock the repository behavior to return an empty Optional
		when(quizRepository.findById(1L)).thenReturn(Optional.empty());

		// Call the service method
		Optional<Quiz> result = quizService.getQuizById(1L);

		// Verify the result is empty
		assertFalse(result.isPresent());
	}

//	@Test
//	public void testGetQuestionsByQuizId() {
//		// Mock the repository behavior
//		List<Question> questions = new ArrayList<>();
//		questions.add(question);
//		when(questionRepository.findByQuizId(1L)).thenReturn(questions);
//
//		// Call the service method
//		List<Question> result = quizService.getQuestionsByQuizId(1L);
//
//		// Verify the questions list is returned
//		assertNotNull(result);
//		assertEquals(1, result.size());
//		assertEquals("What is 2 + 2?", result.get(0).getQuestionText());
//	}
	@Test
	public void testGetQuestionsByQuizId() {
		Long quizId = 1L; // Test with a sample quiz ID

		// Prepare a list of mocked questions
		Question question1 = mock(Question.class);
		Question question2 = mock(Question.class);
		List<Question> questionList = Arrays.asList(question1, question2);

		// Mock the method that retrieves questions by quiz ID
		when(questionRepository.findByQuizId(quizId)).thenReturn(questionList);

		// Call the method
		List<Question> result = quizService.getQuestionsByQuizId(quizId);

		// Assertions to ensure the result is as expected
		assertNotNull(result);
		assertEquals(2, result.size()); // The list should contain 2 questions
		verify(questionRepository, times(1)).findByQuizId(quizId); // Ensure the method is called once
	}

	@Test
	public void testGetQuestionsByQuizIdEmpty() {
		// Mock the repository behavior to return an empty list
		when(questionRepository.findByQuizId(1L)).thenReturn(Collections.emptyList());

		// Call the service method
		List<Question> result = quizService.getQuestionsByQuizId(1L);

		// Verify the list is empty
		assertTrue(result.isEmpty());
	}

	@Test
	public void testUpdateQuiz() {
		// Mock the repository behavior
		when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

		// Call the service method
		quiz.setTitle("Updated Quiz Title");
		Quiz result = quizService.updateQuiz(quiz);

		// Verify the quiz was updated and saved
		assertNotNull(result);
		assertEquals("Updated Quiz Title", result.getTitle());
		verify(quizRepository, times(1)).save(quiz);
	}

	@Test
	public void testDeleteQuizById() {
		// Mock the delete behavior
		doNothing().when(quizRepository).deleteById(anyLong());

		// Call the service method
		quizService.deleteQuizById(1L);

		// Verify that the delete method was called
		verify(quizRepository, times(1)).deleteById(1L);
	}
}

package com.QuizManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.model.User;
import com.QuizManagementSystem.repository.ResultRepository;
import com.QuizManagementSystem.service.QuizService;
import com.QuizManagementSystem.service.UserService;

@WebMvcTest(QuizController.class)
public class QuizControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QuizService quizService;

	@MockBean
	private UserService userService;

	@MockBean
	private ResultRepository resultRepository;

	private Quiz quiz;

	@BeforeEach
	public void setUp() {
		quiz = new Quiz();
		quiz.setId(1L);
		quiz.setTitle("Sample Quiz");

		Question question = new Question();
		question.setId(1L);
		question.setQuestionText("What is 2 + 2?");
		question.setOptionA("3");
		question.setOptionB("4");
		question.setOptionC("5");
		question.setOptionD("6");
		question.setCorrectAnswer("B");

		quiz.setQuestions(new ArrayList<>(Collections.singletonList(question)));
	}

	@Test
	public void testShowQuizForm() throws Exception {
		mockMvc.perform(get("/create-quiz").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andExpect(view().name("admin/create-quiz"));
	}

	@Test
	public void testCreateQuiz() throws Exception {
		when(quizService.createQuiz(any(Quiz.class))).thenReturn(quiz);

		mockMvc.perform(post("/create-quiz").with(user("admin").password("password").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).param("title", "New Quiz")
				.param("questions[0].questionText", "What is 2 + 2?").param("questions[0].optionA", "3")
				.param("questions[0].optionB", "4").param("questions[0].optionC", "5")
				.param("questions[0].optionD", "6").param("questions[0].correctAnswer", "B"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list-quizzes"));
	}

	@Test
	public void testListQuizzes() throws Exception {
		List<Quiz> quizzes = Collections.singletonList(quiz);
		when(quizService.getAllQuizzes()).thenReturn(quizzes);

		mockMvc.perform(get("/list-quizzes").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andExpect(view().name("admin/quiz-list"))
				.andExpect(model().attribute("quizzes", quizzes));
	}

	@Test
	public void testStartQuiz() throws Exception {
		when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));
		when(quizService.getQuestionsByQuizId(1L)).thenReturn(quiz.getQuestions());

		mockMvc.perform(get("/quiz/start/1").with(user("user").password("password").roles("USER")))
				.andExpect(status().isOk()).andExpect(view().name("user/quiz-start"))
				.andExpect(model().attribute("quiz", quiz))
				.andExpect(model().attribute("questions", quiz.getQuestions()));
	}

	@Test
	public void testSubmitQuiz() throws Exception {
		Map<String, String> answers = new HashMap<>();
		answers.put("answers_1", "B");

		User mockUser = mock(User.class);
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("testuser");
		when(userService.findByUsername("testuser")).thenReturn(mockUser);
		when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));
		when(quiz.getQuestions()).thenReturn(quiz.getQuestions());

		mockMvc.perform(post("/quiz/submit").with(user("testuser").password("password").roles("USER"))
				.param("quizId", "1").param("answers_1", "B")).andExpect(status().isOk())
				.andExpect(view().name("user/quiz-result")).andExpect(model().attribute("score", 1))
				.andExpect(model().attribute("totalQuestions", 1));

		// Verify that resultRepository.save was called
		verify(resultRepository, times(1)).save(any(Result.class));
	}

	@Test
	public void testEditQuiz() throws Exception {
		when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));

		mockMvc.perform(get("/edit-quiz/1").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andExpect(view().name("admin/edit-quiz"))
				.andExpect(model().attribute("quiz", quiz));
	}

	@Test
	public void testUpdateQuiz() throws Exception {
		mockMvc.perform(post("/edit-quiz").with(user("admin").password("password").roles("ADMIN")).param("id", "1")
				.param("title", "Updated Quiz")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/list-quizzes"));

		verify(quizService, times(1)).updateQuiz(any(Quiz.class));
	}

	@Test
	public void testDeleteQuiz() throws Exception {
		mockMvc.perform(get("/delete-quiz/1").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list-quizzes"));

		verify(quizService, times(1)).deleteQuizById(1L);
	}
}

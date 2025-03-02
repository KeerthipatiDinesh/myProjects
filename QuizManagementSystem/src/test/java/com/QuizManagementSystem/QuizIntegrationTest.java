package com.QuizManagementSystem;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.service.QuizService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class QuizIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private QuizService quizService;

	private Quiz quiz;

	@BeforeEach
	public void setUp() {
		// Create a new quiz to be used in tests
		quiz = new Quiz();
		quiz.setTitle("Test Quiz");
		quiz.setDescription("This is a test quiz.");
		quizService.createQuiz(quiz);
	}

//	@Test
//	public void testCreateQuiz() throws Exception {
//		// Test creating a new quiz
//		mockMvc.perform(
//				post("/create-quiz").param("title", "New Quiz").param("description", "Description of the new quiz"))
//				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list-quizzes"))
//				.andExpect(flash().attribute("message", "Quiz created successfully!"));
//	}
	@Test
	public void testCreateQuiz() throws Exception {
		mockMvc.perform(
				post("/create-quiz").param("title", "New Quiz").param("description", "Description of the new quiz")
						.with(user("Dinesh").password("dinesh1234").roles("ADMIN"))) // Simulate admin login
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list-quizzes"));
	}

	@Test
	public void testListQuizzes() throws Exception {
		// Test listing all quizzes
		mockMvc.perform(get("/list-quizzes").with(user("Dinesh").password("dinesh1234").roles("ADMIN"))) // Simulate
																											// admin
																											// login
				.andExpect(status().isOk()).andExpect(model().attributeExists("quizzes"))
				.andExpect(view().name("admin/quiz-list"));
	}

	@Test
	public void testStartQuiz() throws Exception {
		// Test starting a quiz
		mockMvc.perform(
				get("/quiz/start/{quizId}", quiz.getId()).with(user("Dinesh").password("dinesh1234").roles("ADMIN"))) // Simulate
																														// admin
																														// login
																														// (if
																														// admins
																														// can
																														// start
																														// quizzes)
				.andExpect(status().isOk()).andExpect(view().name("user/quiz-start"))
				.andExpect(model().attribute("quiz", quiz));
	}

	@Test
	public void testSubmitQuiz() throws Exception {
		// Test submitting a quiz
		mockMvc.perform(post("/quiz/submit").param("quizId", quiz.getId().toString()).param("answers_1", "Option A")
				.with(user("Dinesh").password("dinesh1234").roles("ADMIN"))) // Simulate admin login (if needed)
				.andExpect(status().isOk()).andExpect(view().name("user/quiz-result"))
				.andExpect(model().attribute("score", 1)); // Assuming 1 correct answer
	}

	@Test
	public void testDeleteQuiz() throws Exception {
		// Test deleting a quiz
		mockMvc.perform(
				get("/delete-quiz/{quizId}", quiz.getId()).with(user("Dinesh").password("dinesh1234").roles("ADMIN"))) // Simulate
																														// admin
																														// login
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/list-quizzes"));
	}

//	@Test
//	public void testListQuizzes() throws Exception {
//		// Test listing all quizzes
//		mockMvc.perform(get("/list-quizzes")).andExpect(status().isOk()).andExpect(model().attributeExists("quizzes"))
//				.andExpect(view().name("admin/quiz-list"));
//	}
//
//	@Test
//	public void testStartQuiz() throws Exception {
//		// Test starting a quiz
//		mockMvc.perform(get("/quiz/start/{quizId}", quiz.getId())).andExpect(status().isOk())
//				.andExpect(view().name("user/quiz-start")).andExpect(model().attribute("quiz", quiz));
//	}
//
//	@Test
//	public void testSubmitQuiz() throws Exception {
//		// Test submitting a quiz
//		mockMvc.perform(post("/quiz/submit").param("quizId", quiz.getId().toString()).param("answers_1", "Option A")) 
//				.andExpect(status().isOk()).andExpect(view().name("user/quiz-result"))
//				.andExpect(model().attribute("score", 1)); // Assuming 1 correct answer
//	}
//
//	@Test
//	public void testDeleteQuiz() throws Exception {
//		// Test deleting a quiz
//		mockMvc.perform(get("/delete-quiz/{quizId}", quiz.getId())).andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/list-quizzes"));
//	}
}

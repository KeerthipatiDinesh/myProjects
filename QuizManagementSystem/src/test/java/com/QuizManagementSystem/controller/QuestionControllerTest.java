package com.QuizManagementSystem.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.QuizManagementSystem.model.Question;
import com.QuizManagementSystem.model.Quiz;
import com.QuizManagementSystem.service.QuestionService;
import com.QuizManagementSystem.service.QuizService;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuizService quizService;

    @Test
    public void testAddQuestionForm_QuizFound() throws Exception {
        Quiz quiz = new Quiz("Sample Quiz");
        quiz.setId(1L);
        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));

        mockMvc.perform(get("/addQuestion/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("admin/add-question"))
               .andExpect(model().attribute("quiz", quiz));
    }

    @Test
    public void testAddQuestionForm_QuizNotFound() throws Exception {
        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/addQuestion/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("error"))
               .andExpect(model().attribute("error", "Quiz not found for ID: 1"));
    }
    
    @Test
    public void testAddQuestion_ValidAnswer() throws Exception {
        Question question = new Question();
        question.setCorrectAnswer("A");

        mockMvc.perform(post("/addQuestion")
                .flashAttr("question", question)
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/list-quizzes"));
    }

    @Test
    public void testAddQuestion_InvalidAnswer() throws Exception {
        Question question = new Question();
        question.setCorrectAnswer("E"); // Invalid answer

        mockMvc.perform(post("/addQuestion")
                .flashAttr("question", question)
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-question"))
                .andExpect(model().attribute("error", "Invalid correct answer selection. Please select a valid option."));
    }

    @Test
    public void testShowQuestions_QuizFound() throws Exception {
        Quiz quiz = new Quiz("Sample Quiz");
        quiz.setId(1L);
        Question question = new Question("Sample question", "A", "B", "C", "D", "A");
        quiz.addQuestion(question);

        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));

        mockMvc.perform(get("/show-questions/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("admin/show-questions"))
               .andExpect(model().attribute("quiz", quiz))
               .andExpect(model().attribute("questions", quiz.getQuestions()));
    }

    @Test
    public void testShowQuestions_QuizNotFound() throws Exception {
        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/show-questions/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("error"))
               .andExpect(model().attribute("error", "Quiz not found for ID: 1"));
    }

    @Test
    public void testEditQuestion_QuizAndQuestionFound() throws Exception {
        Quiz quiz = new Quiz("Sample Quiz");
        quiz.setId(1L);
        Question question = new Question("Sample question", "A", "B", "C", "D", "A");
        question.setId(1L);

        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.of(quiz));
        Mockito.when(questionService.getQuestionById(1L)).thenReturn(Optional.of(question));

        mockMvc.perform(get("/edit-question/1/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("admin/edit-question"))
               .andExpect(model().attribute("quizId", 1L))
               .andExpect(model().attribute("question", question));
    }

    @Test
    public void testEditQuestion_QuizOrQuestionNotFound() throws Exception {
        Mockito.when(quizService.getQuizById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/edit-question/1/1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("error"))
               .andExpect(model().attribute("error", "Quiz or Question not found."));
    }

    @Test
    public void testSaveEditedQuestion_ValidAnswer() throws Exception {
        Question existingQuestion = new Question("Old question", "A", "B", "C", "D", "A");
        existingQuestion.setId(1L);

        Question updatedQuestion = new Question("Updated question", "A", "B", "C", "D", "A");

        Mockito.when(questionService.getQuestionById(1L)).thenReturn(Optional.of(existingQuestion));

        mockMvc.perform(post("/edit-question/1/1")
                .flashAttr("question", updatedQuestion)
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/show-questions/1"))
               .andExpect(model().attribute("message", "Question updated successfully"));
    }

    @Test
    public void testSaveEditedQuestion_InvalidAnswer() throws Exception {
        Question existingQuestion = new Question("Old question", "A", "B", "C", "D", "A");
        existingQuestion.setId(1L);

        Question updatedQuestion = new Question("Updated question", "E", "B", "C", "D", "E"); // Invalid answer

        Mockito.when(questionService.getQuestionById(1L)).thenReturn(Optional.of(existingQuestion));

        mockMvc.perform(post("/edit-question/1/1")
                .flashAttr("question", updatedQuestion)
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().isOk())
               .andExpect(view().name("admin/edit-question"))
               .andExpect(model().attribute("error", "Correct answer must be one of the options (A, B, C, or D)."));
    }

    @Test
    public void testDeleteQuestion() throws Exception {
        mockMvc.perform(post("/delete-question/1")
                .param("quizId", "1")
                .with(user("admin").password("password").roles("ADMIN")))  // Simulate admin login
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/admin/show-questions/1"));
    }
}

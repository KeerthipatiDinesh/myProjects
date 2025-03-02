package com.QuizManagementSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	@NotBlank(message = "Question text is required")
	@Size(max = 300, message = "Question text must not exceed 300 characters")
	private String questionText;

	@NotBlank(message = "Option A is required")
	@Size(max = 100, message = "Option A must not exceed 100 characters")
	private String optionA;
	@NotBlank(message = "Option B is required")
	@Size(max = 100, message = "Option B must not exceed 100 characters")
	private String optionB;
	@NotBlank(message = "Option C is required")
	@Size(max = 100, message = "Option C must not exceed 100 characters")
	private String optionC;
	@NotBlank(message = "Option D is required")
	@Size(max = 100, message = "Option D must not exceed 100 characters")
	private String optionD;

	@Column(nullable = false)
	@NotBlank(message = "Correct answer is required")
	@Pattern(regexp = "A|B|C|D", message = "Correct answer must be one of: A, B, C, or D")
	private String correctAnswer;
	@ManyToOne
	@JoinColumn(name = "quiz_id", nullable = false)
	private Quiz quiz;

	public Question(String string, String string2, String string3, String string4, String string5, String string6) {
		// TODO Auto-generated constructor stub
	}

	public Question() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

}

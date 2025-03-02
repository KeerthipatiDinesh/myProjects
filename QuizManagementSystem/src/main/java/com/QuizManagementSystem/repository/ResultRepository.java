package com.QuizManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuizManagementSystem.model.Result;
import com.QuizManagementSystem.model.User;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

	List<Result> findByUserUsername(String username);

	@Query("SELECT r FROM Result r JOIN FETCH r.user JOIN FETCH r.quiz WHERE r.quiz.id = :quizId")
	List<Result> findResultsByQuizId(@Param("quizId") Long quizId);

	List<Result> findByUser(Optional<User> user);

	@Query("SELECT r FROM Result r JOIN FETCH r.user u JOIN FETCH r.quiz q")
	List<Result> findAllWithUserAndQuiz();

}

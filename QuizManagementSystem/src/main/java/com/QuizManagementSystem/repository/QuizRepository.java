package com.QuizManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuizManagementSystem.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

}

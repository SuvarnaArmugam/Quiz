package com.example.quizapp.repository;

import com.example.quizapp.entity.QuizAttempt;
import com.example.quizapp.entity.AttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

	Optional<QuizAttempt> findFirstByQuiz_IdAndStatusOrderByStartedAtDesc(Long quizId, AttemptStatus status);
}

package com.example.aiinterview_v2.interview.repository;

import com.example.aiinterview_v2.interview.model.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, String> {
    List<InterviewAnswer> findByQuestionSessionSessionId(String sessionId);
}
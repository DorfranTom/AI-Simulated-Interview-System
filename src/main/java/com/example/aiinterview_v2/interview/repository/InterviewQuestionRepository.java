package com.example.aiinterview_v2.interview.repository;

import com.example.aiinterview_v2.interview.model.InterviewQuestion;
import com.example.aiinterview_v2.interview.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, String> {
    List<InterviewQuestion> findBySessionSessionId(String sessionId);
}
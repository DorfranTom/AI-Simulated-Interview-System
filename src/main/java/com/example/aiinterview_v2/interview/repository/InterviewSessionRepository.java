package com.example.aiinterview_v2.interview.repository;

import com.example.aiinterview_v2.interview.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, String> {
}
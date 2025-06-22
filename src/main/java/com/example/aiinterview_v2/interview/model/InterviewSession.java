package com.example.aiinterview_v2.interview.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_session")
public class InterviewSession {
    @Id
    private String sessionId;
    private String positionType;
    private String interviewTitle;
    private LocalDateTime startTime;
    private String candidateName;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewQuestion> questions = new ArrayList<>();

    public InterviewSession() {
    }

    public InterviewSession(String sessionId, String candidateName, LocalDateTime startTime) {
        this.sessionId = sessionId;
        this.startTime = startTime;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public List<InterviewQuestion> getQuestions() {
        return questions;
    }

    public void addQuestion(InterviewQuestion question) {
        questions.add(question);
        question.setSession(this);
    }

    public void removeQuestion(InterviewQuestion question) {
        questions.remove(question);
        question.setSession(null);
    }

    public String getInterviewTitle() {
        return interviewTitle;
    }

    public void setInterviewTitle(String interviewTitle) {
        this.interviewTitle = interviewTitle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public InterviewQuestion getLastQuestion() {
        if (questions.isEmpty()) {
            return null;
        }
        return questions.get(questions.size() - 1);
    }
}
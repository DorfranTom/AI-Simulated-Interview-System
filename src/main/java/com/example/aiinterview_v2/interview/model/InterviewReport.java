package com.example.aiinterview_v2.interview.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterviewReport {
    private String sessionId;
    private String positionType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double overallScore;
    private Map<String, Double> dimensionScores;
    private List<String> knowledgeGaps;
    private List<String> suggestions;
    private List<InterviewRecord> interviewRecords;
    private String candidateName;
    private List<InterviewQuestion> questions;
    private List<InterviewAnswer> answers;

    public InterviewReport() {
        this.dimensionScores = new HashMap<>();
        this.knowledgeGaps = new ArrayList<>();
        this.suggestions = new ArrayList<>();
        this.interviewRecords = new ArrayList<>();
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public Map<String, Double> getDimensionScores() {
        return dimensionScores;
    }

    public void setDimensionScores(Map<String, Double> dimensionScores) {
        this.dimensionScores = dimensionScores;
    }

    public List<String> getKnowledgeGaps() {
        return knowledgeGaps;
    }

    public void addKnowledgeGap(String gap) {
        this.knowledgeGaps.add(gap);
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void addSuggestion(String suggestion) {
        this.suggestions.add(suggestion);
    }

    public List<InterviewRecord> getInterviewRecords() {
        return interviewRecords;
    }

    public void setInterviewRecords(List<InterviewRecord> interviewRecords) {
        this.interviewRecords = interviewRecords;
    }

    public void setInterviewRecords(List<InterviewQuestion> questions, List<InterviewAnswer> answers) {
        // 匹配问题和回答
        for (int i = 0; i < questions.size(); i++) {
            InterviewQuestion question = questions.get(i);
            InterviewAnswer answer = i < answers.size() ? answers.get(i) : null;
            
            InterviewRecord record = new InterviewRecord();
            record.setQuestion(question);
            record.setAnswer(answer);
            
            interviewRecords.add(record);
        }
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public void setQuestions(List<InterviewQuestion> questions) {
        this.questions = questions;
    }

    public void setAnswers(List<InterviewAnswer> answers) {
        this.answers = answers;
    }
}
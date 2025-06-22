package com.example.aiinterview_v2.interview.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_answer")
public class InterviewAnswer {
    @Id
    private String answerId;
    private String questionId;
    private String content;
    private LocalDateTime timestamp;
    private double evaluationScore;

    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private InterviewQuestion question;

    public InterviewAnswer() {
    }

    public InterviewAnswer(String answerId, String questionId, String content, LocalDateTime timestamp, double evaluationScore) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.content = content;
        this.timestamp = timestamp;
        this.evaluationScore = evaluationScore;
    }

    // Getters and Setters
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(double evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public InterviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(InterviewQuestion question) {
        this.question = question;
    }
}
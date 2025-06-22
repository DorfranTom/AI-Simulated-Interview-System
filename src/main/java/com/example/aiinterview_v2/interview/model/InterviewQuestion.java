package com.example.aiinterview_v2.interview.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_question")
public class InterviewQuestion {
    @Id
    private String questionId;
    private String content;
    private String category;
    private InterviewDifficulty difficulty;
    @ElementCollection
    private List<String> relatedKnowledgeNodes;
    private boolean isFollowUp;
    private String originalQuestionId;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private InterviewSession session;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private InterviewAnswer answer;

    // Getters and Setters
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public InterviewDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(InterviewDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getRelatedKnowledgeNodes() {
        return relatedKnowledgeNodes;
    }

    public void setRelatedKnowledgeNodes(List<String> relatedKnowledgeNodes) {
        this.relatedKnowledgeNodes = relatedKnowledgeNodes;
    }

    public boolean isFollowUp() {
        return isFollowUp;
    }

    public void setFollowUp(boolean followUp) {
        isFollowUp = followUp;
    }

    public String getOriginalQuestionId() {
        return originalQuestionId;
    }

    public void setOriginalQuestionId(String originalQuestionId) {
        this.originalQuestionId = originalQuestionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public InterviewSession getSession() {
        return session;
    }

    public void setSession(InterviewSession session) {
        this.session = session;
    }

    public InterviewAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(InterviewAnswer answer) {
        this.answer = answer;
        answer.setQuestion(this);
    }
}
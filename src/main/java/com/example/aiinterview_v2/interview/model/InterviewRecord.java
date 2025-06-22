package com.example.aiinterview_v2.interview.model;

public class InterviewRecord {
    private InterviewQuestion question;
    private InterviewAnswer answer;

    // Getters and Setters
    public InterviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(InterviewQuestion question) {
        this.question = question;
    }

    public InterviewAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(InterviewAnswer answer) {
        this.answer = answer;
    }
}
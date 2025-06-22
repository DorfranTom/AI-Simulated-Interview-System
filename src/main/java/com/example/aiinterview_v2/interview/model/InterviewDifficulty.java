package com.example.aiinterview_v2.interview.model;

public enum InterviewDifficulty {
    EASY("简单"),
    MEDIUM("中等"),
    HARD("困难"),
    VERY_HARD("极难");

    private final String chineseName;

    InterviewDifficulty(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }
}
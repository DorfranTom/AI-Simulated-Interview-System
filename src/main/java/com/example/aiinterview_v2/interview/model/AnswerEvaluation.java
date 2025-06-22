package com.example.aiinterview_v2.interview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerEvaluation {
    private double overallScore;
    private boolean hasCriticalFlaw;
    private Map<String, Double> dimensionScores;
    private List<String> knowledgeGaps;
    private List<String> suggestions;

    public AnswerEvaluation() {
        this.dimensionScores = new HashMap<>();
        this.knowledgeGaps = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    // Getters and Setters
    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public boolean hasCriticalFlaw() {
        return hasCriticalFlaw;
    }

    public void setHasCriticalFlaw(boolean hasCriticalFlaw) {
        this.hasCriticalFlaw = hasCriticalFlaw;
    }

    public Map<String, Double> getDimensionScores() {
        return dimensionScores;
    }

    public void addDimensionScore(String dimension, double score) {
        this.dimensionScores.put(dimension, score);
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
}
package com.example.aiinterview_v2.interview.evaluator;

import com.example.aiinterview_v2.interview.model.AnswerEvaluation;
import com.example.aiinterview_v2.interview.model.InterviewQuestion;

public interface AnswerEvaluator {
    /**
     * 评估用户回答
     * @param userAnswer 用户回答内容
     * @param question 对应的问题
     * @return 评估结果
     */
    AnswerEvaluation evaluate(String userAnswer, InterviewQuestion question);
    double evaluateAnswer(String questionId, String answer);
    String generateFeedback(String questionId, String answer);
}
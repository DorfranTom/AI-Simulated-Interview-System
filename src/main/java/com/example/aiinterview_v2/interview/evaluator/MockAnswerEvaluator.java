//package com.example.aiinterview_v2.interview.evaluator;
//
//import com.example.aiinterview_v2.interview.model.AnswerEvaluation;
//import com.example.aiinterview_v2.interview.model.InterviewQuestion;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MockAnswerEvaluator implements AnswerEvaluator {
//
//    @Override
//    public AnswerEvaluation evaluate(String userAnswer, InterviewQuestion question) {
//        return null;
//    }
//
//    @Override
//    public double evaluateAnswer(String questionId, String answer) {
//        return Math.random() * 10; // mock score
//    }
//
//    @Override
//    public String generateFeedback(String questionId, String answer) {
//        return "回答整体合理，但可补充更多技术细节。";
//    }
//}

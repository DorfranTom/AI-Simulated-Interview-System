//package com.example.aiinterview_v2.interview.service;
//
//import com.example.aiinterview_v2.interview.model.InterviewQuestion;
//import com.example.aiinterview_v2.interview.model.InterviewReport;
//import com.example.aiinterview_v2.interview.model.InterviewSession;
//
//public interface InterviewService {
//    public InterviewSession startInterview(String positionType, String interviewTitle);
//    InterviewSession submitAnswer(String sessionId, String questionId, String responseText);
//    InterviewSession getSession(String sessionId);
//    public InterviewQuestion processAnswer(String sessionId, String userAnswer);
//    public InterviewReport endInterview(String sessionId);
//}


package com.example.aiinterview_v2.interview.service;

import com.example.aiinterview_v2.interview.model.InterviewSession;
import com.example.aiinterview_v2.interview.model.InterviewQuestion;
import com.example.aiinterview_v2.interview.model.InterviewAnswer;
import com.example.aiinterview_v2.interview.model.InterviewReport;

import java.util.List;
import java.util.Optional;

public interface InterviewService {

    // 开始新的面试会话
    InterviewSession startInterview(String candidateName, String positionType);

    // 提交问题的回答
    InterviewAnswer submitAnswer(String sessionId, String questionId, String answerContent);

    // 获取特定面试会话
    Optional<InterviewSession> getSession(String sessionId);

    // 获取所有面试会话
    List<InterviewSession> getAllSessions();

    // 结束面试并生成报告
    InterviewReport completeInterview(String sessionId);

    // 获取面试问题列表
    List<InterviewQuestion> getQuestionsBySession(String sessionId);

    // 获取面试回答列表
    List<InterviewAnswer> getAnswersBySession(String sessionId);
    InterviewSession saveInterview(InterviewSession session);
}
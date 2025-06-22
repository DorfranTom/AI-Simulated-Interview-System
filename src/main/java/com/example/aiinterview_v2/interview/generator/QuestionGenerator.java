package com.example.aiinterview_v2.interview.generator;

import com.example.aiinterview_v2.interview.model.InterviewDifficulty;
import com.example.aiinterview_v2.interview.model.InterviewQuestion;

import java.util.List;
public interface QuestionGenerator {
    /**
     * 根据岗位类型和难度生成初始问题
     * @param positionType 岗位类型
     * @param difficulty 难度级别
     * @return 生成的问题列表
     */
    List<InterviewQuestion> generateQuestions(String positionType, InterviewDifficulty difficulty);

    /**
     * 基于用户回答生成追问
     * @param userAnswer 用户回答内容
     * @param previousQuestion 之前的问题
     * @param difficulty 追问的难度
     * @return 生成的追问
     */
    InterviewQuestion generateFollowUpQuestion(String userAnswer, InterviewQuestion previousQuestion, InterviewDifficulty difficulty);

    /**
     * 生成下一个问题（基于当前面试进度）
     * @param positionType 岗位类型
     * @param difficulty 难度级别
     * @return 生成的下一个问题
     */
    InterviewQuestion generateNextQuestion(String positionType, InterviewDifficulty difficulty);
    List<InterviewQuestion> generateQuestions(int num);

    // 生成第一个问题（固定为自我介绍）
    InterviewQuestion generateFirstQuestion();
}
//package com.example.aiinterview_v2.interview.service;
//
//import com.example.aiinterview_v2.interview.model.*;
//import com.example.aiinterview_v2.interview.generator.QuestionGenerator;
//import com.example.aiinterview_v2.interview.evaluator.AnswerEvaluator;
//import com.example.aiinterview_v2.interview.repository.InterviewSessionRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class InterviewServiceImpl implements InterviewService {
//
//    private final QuestionGenerator questionGenerator;
//    private final AnswerEvaluator answerEvaluator;
//    private final InterviewSessionRepository interviewSessionRepository;
//
//    public InterviewServiceImpl(QuestionGenerator questionGenerator, AnswerEvaluator answerEvaluator, InterviewSessionRepository interviewSessionRepository) {
//        this.questionGenerator = questionGenerator;
//        this.answerEvaluator = answerEvaluator;
//        this.interviewSessionRepository = interviewSessionRepository;
//    }
//
//    @Override
//    public InterviewSession startInterview(String positionType, String interviewTitle) {
//        InterviewSession session = new InterviewSession();
//        session.setSessionId(UUID.randomUUID().toString());
//        session.setPositionType(positionType);
//        session.setInterviewTitle(interviewTitle);
//
//        InterviewQuestion firstQuestion = questionGenerator.generateFirstQuestion();
//        session.addQuestion(firstQuestion);
//
//        return interviewSessionRepository.save(session);
//    }
//
//    @Override
//    public InterviewSession submitAnswer(String sessionId, String questionId, String responseText) {
//        return null;
//    }
//
//    @Override
//    public InterviewSession getSession(String sessionId) {
//        return null;
//    }
//
//    @Override
//    public InterviewQuestion processAnswer(String sessionId, String userAnswer) {
//        InterviewSession session = interviewSessionRepository.findById(sessionId).orElse(null);
//        if (session == null) {
//            throw new IllegalArgumentException("无效的会话ID: " + sessionId);
//        }
//
//        InterviewQuestion lastQuestion = session.getLastQuestion();
//        if (lastQuestion == null) {
//            throw new IllegalStateException("会话中没有待回答的问题");
//        }
//
//        InterviewAnswer answer = new InterviewAnswer();
//        answer.setAnswerId(UUID.randomUUID().toString());
//        answer.setQuestionId(lastQuestion.getQuestionId());
//        answer.setContent(userAnswer);
//        answer.setTimestamp(LocalDateTime.now());
//
//        AnswerEvaluation evaluation = answerEvaluator.evaluate(userAnswer, lastQuestion);
//        answer.setEvaluation(evaluation);
//
//        session.addAnswer(answer);
//
//        if (evaluation.hasCriticalFlaw()) {
//            session.incrementConsecutiveWeakAnswers();
//            if (session.getConsecutiveWeakAnswers() >= 2 && !session.isStressMode()) {
//                session.setStressMode(true);
//                session.setCurrentDifficulty(InterviewDifficulty.HARD);
//            }
//        } else {
//            session.resetConsecutiveWeakAnswers();
//            if (evaluation.getOverallScore() >= 8) {
//                increaseDifficulty(session);
//            } else if (evaluation.getOverallScore() < 5) {
//                decreaseDifficulty(session);
//            }
//        }
//
//        InterviewQuestion nextQuestion;
//        if (session.isStressMode() && evaluation.hasCriticalFlaw()) {
//            nextQuestion = questionGenerator.generateFollowUpQuestion(
//                    userAnswer,
//                    lastQuestion,
//                    session.getCurrentDifficulty()
//            );
//        } else {
//            nextQuestion = questionGenerator.generateNextQuestion(
//                    session.getPositionType(),
//                    session.getCurrentDifficulty()
//            );
//        }
//
//        if (nextQuestion != null) {
//            session.addQuestion(nextQuestion);
//        }
//
//        interviewSessionRepository.save(session);
//        return nextQuestion;
//    }
//
//    @Override
//    public InterviewReport endInterview(String sessionId) {
//        InterviewSession session = interviewSessionRepository.findById(sessionId).orElse(null);
//        if (session == null) {
//            throw new IllegalArgumentException("无效的会话ID: " + sessionId);
//        }
//
//        InterviewReport report = generateReport(session);
//        interviewSessionRepository.deleteById(sessionId);
//
//        return report;
//    }
//
//    private void increaseDifficulty(InterviewSession session) {
//        InterviewDifficulty current = session.getCurrentDifficulty();
//        if (current == InterviewDifficulty.EASY) {
//            session.setCurrentDifficulty(InterviewDifficulty.MEDIUM);
//        } else if (current == InterviewDifficulty.MEDIUM) {
//            session.setCurrentDifficulty(InterviewDifficulty.HARD);
//        } else if (current == InterviewDifficulty.HARD) {
//            session.setCurrentDifficulty(InterviewDifficulty.VERY_HARD);
//        }
//    }
//
//    private void decreaseDifficulty(InterviewSession session) {
//        InterviewDifficulty current = session.getCurrentDifficulty();
//        if (current == InterviewDifficulty.VERY_HARD) {
//            session.setCurrentDifficulty(InterviewDifficulty.HARD);
//        } else if (current == InterviewDifficulty.HARD) {
//            session.setCurrentDifficulty(InterviewDifficulty.MEDIUM);
//        } else if (current == InterviewDifficulty.MEDIUM) {
//            session.setCurrentDifficulty(InterviewDifficulty.EASY);
//        }
//    }
//
//    private InterviewReport generateReport(InterviewSession session) {
//        InterviewReport report = new InterviewReport();
//        report.setSessionId(session.getSessionId());
//        report.setPositionType(session.getPositionType());
//        report.setStartTime(session.getQuestions().get(0).getTimestamp());
//        report.setEndTime(LocalDateTime.now());
//
//        double totalScore = 0;
//        int validAnswers = 0;
//
//        for (InterviewAnswer answer : session.getAnswers()) {
//            AnswerEvaluation evaluation = answer.getEvaluation();
//            if (evaluation != null) {
//                totalScore += evaluation.getOverallScore();
//                validAnswers++;
//            }
//        }
//
//        if (validAnswers > 0) {
//            report.setOverallScore(totalScore / validAnswers);
//        }
//
//        for (InterviewAnswer answer : session.getAnswers()) {
//            AnswerEvaluation evaluation = answer.getEvaluation();
//            if (evaluation != null) {
//                report.getKnowledgeGaps().addAll(evaluation.getKnowledgeGaps());
//                report.getSuggestions().addAll(evaluation.getSuggestions());
//            }
//        }
//
//        report.setInterviewRecords(session.getQuestions(), session.getAnswers());
//
//        return report;
//    }
//}

package com.example.aiinterview_v2.interview.service;

import com.example.aiinterview_v2.interview.model.*;
import com.example.aiinterview_v2.interview.repository.InterviewSessionRepository;
import com.example.aiinterview_v2.interview.repository.InterviewQuestionRepository;
import com.example.aiinterview_v2.interview.repository.InterviewAnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;

    public InterviewServiceImpl(InterviewSessionRepository sessionRepository,
                                InterviewQuestionRepository questionRepository,
                                InterviewAnswerRepository answerRepository) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public InterviewSession startInterview(String candidateName, String positionType) {
        String sessionId = UUID.randomUUID().toString();
        InterviewSession session = new InterviewSession();
        session.setSessionId(sessionId);
        session.setPositionType(positionType);
        session.setStartTime(LocalDateTime.from(Instant.now()));

        // 保存会话到数据库
        return sessionRepository.save(session);
    }

    @Override
    public InterviewAnswer submitAnswer(String sessionId, String questionId, String answerContent) {
        // 检查会话是否存在
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("无效的会话ID"));

        // 检查问题是否存在且属于该会话
        InterviewQuestion question = questionRepository.findById(questionId)
                .filter(q -> q.getSession().getSessionId().equals(sessionId))
                .orElseThrow(() -> new IllegalArgumentException("无效的问题ID"));

        // 创建新回答
        InterviewAnswer answer = new InterviewAnswer();
        answer.setAnswerId(UUID.randomUUID().toString());
        answer.setQuestion(question);
        answer.setContent(answerContent);
        answer.setTimestamp(LocalDateTime.from(Instant.now()) );

        // 保存回答到数据库
        return answerRepository.save(answer);
    }

    @Override
    public Optional<InterviewSession> getSession(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public List<InterviewSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public InterviewReport completeInterview(String sessionId) {
        // 获取会话及其相关问题和回答
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("无效的会话ID"));

        List<InterviewQuestion> questions = questionRepository.findBySessionSessionId(sessionId);
        List<InterviewAnswer> answers = answerRepository.findByQuestionSessionSessionId(sessionId);

        // 生成面试报告
        InterviewReport report = new InterviewReport();
        report.setSessionId(sessionId);
        report.setPositionType(session.getPositionType());
        report.setStartTime(session.getStartTime());
        report.setEndTime(LocalDateTime.from(Instant.now()));

        // 计算总分
        double totalScore = 0.0;
        int validAnswers = 0;

        for (InterviewAnswer answer : answers) {
            if (answer.getEvaluationScore() > 0) {
                totalScore += answer.getEvaluationScore();
                validAnswers++;
            }
        }

        if (validAnswers > 0) {
            report.setOverallScore(totalScore / validAnswers);
        }

        // 设置问题和回答
        report.setQuestions(questions);
        report.setAnswers(answers);

        // 标记会话为已完成（可选）
        // session.setCompleted(true);
        // sessionRepository.save(session);

        return report;
    }

    @Override
    public List<InterviewQuestion> getQuestionsBySession(String sessionId) {
        return questionRepository.findBySessionSessionId(sessionId);
    }

    @Override
    public List<InterviewAnswer> getAnswersBySession(String sessionId) {
        return answerRepository.findByQuestionSessionSessionId(sessionId);
    }

    @Override
    public InterviewSession saveInterview(InterviewSession session) {
        return sessionRepository.save(session);
    }
}
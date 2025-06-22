package com.example.aiinterview_v2.interview.generator.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.aiinterview_v2.interview.generator.QuestionGenerator;
import com.example.aiinterview_v2.interview.model.InterviewDifficulty;
import com.example.aiinterview_v2.interview.model.InterviewQuestion;

import com.example.aiinterview_v2.interview.model.QuestionCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TongyiQuestionGenerator implements QuestionGenerator {
    private static final Logger log = LoggerFactory.getLogger(TongyiQuestionGenerator.class);
    @Value("${dashscope.api.key}")
    private final String apiKey;
    private static final String FIRST_QUESTION = "请首先介绍一下你自己的基本情况";
    public TongyiQuestionGenerator(@Value("${dashscope.api.key}") String apiKey) {
        this.apiKey = apiKey;
//        this.apiKey = System.getenv("DASHSCOPE_API_KEY");
//        if (this.apiKey == null) {
//            throw new IllegalArgumentException("系统环境变量中未找到 DASHSCOPE_API_KEY");
//        }
    }

    @Override
    public List<InterviewQuestion> generateQuestions(String positionType, InterviewDifficulty difficulty) {
        String prompt = buildQuestionPrompt(positionType, difficulty, 3); // 生成3个问题

        try {
            GenerationResult response = callTongyi(prompt);
            if (response != null && response.getOutput() != null && !response.getOutput().getChoices().isEmpty()) {
                return parseQuestionsFromResponse(response.getOutput().getChoices().get(0).getMessage().getContent(), positionType, difficulty);
            } else {
                log.error("通义千问 API 调用失败，未获取到有效响应");
                return generateFallbackQuestions(positionType, difficulty);
            }
        } catch (Exception e) {
            log.error("调用通义千问 API 失败", e);
            return generateFallbackQuestions(positionType, difficulty);
        }
    }

    @Override
    public InterviewQuestion generateFollowUpQuestion(String userAnswer, InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
        String prompt = buildFollowUpPrompt(userAnswer, previousQuestion, difficulty);

        try {
            GenerationResult response = callTongyi(prompt);
            if (response != null && response.getOutput() != null && !response.getOutput().getChoices().isEmpty()) {
                List<InterviewQuestion> questions = parseQuestionsFromResponse(response.getOutput().getChoices().get(0).getMessage().getContent(),
                        previousQuestion.getCategory(),
                        difficulty);
                if (!questions.isEmpty()) {
                    InterviewQuestion followUp = questions.get(0);
                    followUp.setFollowUp(true);
                    followUp.setOriginalQuestionId(previousQuestion.getQuestionId());
                    return followUp;
                }
            }

            // 生成默认追问
            return createDefaultFollowUpQuestion(previousQuestion, difficulty);
        } catch (Exception e) {
            log.error("生成追问失败", e);
            return createDefaultFollowUpQuestion(previousQuestion, difficulty);
        }
    }

    @Override
    public InterviewQuestion generateNextQuestion(String positionType, InterviewDifficulty difficulty) {
        List<InterviewQuestion> questions = generateQuestions(positionType, difficulty);
        return questions.isEmpty() ? null : questions.get(0);
    }

    @Override
    public List<InterviewQuestion> generateQuestions(int num) {
        return null;
    }

    @Override
    public InterviewQuestion generateFirstQuestion() {
        InterviewQuestion question = new InterviewQuestion();
        question.setQuestionId(String.valueOf(System.currentTimeMillis())); // 使用时间戳作为ID
        question.setContent(FIRST_QUESTION);
        question.setCategory("基础题");
        question.setDifficulty(InterviewDifficulty.MEDIUM);
        return question;
    }

    private String buildQuestionPrompt(String positionType, InterviewDifficulty difficulty, int count) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的").append(positionType).append("面试官。请生成")
                .append(count).append("个")
                .append(difficulty.getChineseName()).append("难度的面试问题，")
                .append("涵盖算法、系统设计、项目经验和前沿技术等方面。")
                .append("每个问题后请用括号简要说明考察的知识点。")
                .append("问题应具有实际场景和技术深度，例如：\n")
                .append("1. 如何设计一个支持高并发的分布式缓存系统？(考察分布式系统设计、缓存策略)\n")
                .append("2. 请解释一下Kafka的分区机制和数据一致性保证。(考察消息队列、分布式系统)\n")
                .append("3. 编写一个函数，实现对大规模数据的快速去重。(考察算法设计、数据结构)\n\n")
                .append("请按以下格式回答：\n")
                .append("1. [问题内容] ([考察的知识点])\n")
                .append("2. [问题内容] ([考察的知识点])\n")
                .append("3. [问题内容] ([考察的知识点])");

        return prompt.toString();
    }

    private String buildFollowUpPrompt(String userAnswer, InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("用户对问题 \"").append(previousQuestion.getContent())
                .append("\" 的回答如下：\n")
                .append(userAnswer).append("\n\n")
                .append("请作为面试官，针对这个回答提出一个")
                .append(difficulty.getChineseName()).append("难度的追问，")
                .append("进一步考察候选人的技术深度或发现潜在的知识漏洞。")
                .append("请直接给出追问问题，无需额外解释。");

        return prompt.toString();
    }

    private GenerationResult callTongyi(String prompt) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

    private List<InterviewQuestion> parseQuestionsFromResponse(String content, String positionType, InterviewDifficulty difficulty) {
        List<InterviewQuestion> questions = new ArrayList<>();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            // 简单解析问题和知识点
            int index = line.indexOf(". ");
            if (index > 0 && index + 2 < line.length()) {
                String questionPart = line.substring(index + 2);
                String questionContent = questionPart;
                String category = "综合";

                // 尝试提取括号中的知识点
                int bracketStart = questionPart.indexOf("(");
                int bracketEnd = questionPart.indexOf(")");
                if (bracketStart > 0 && bracketEnd > bracketStart) {
                    questionContent = questionPart.substring(0, bracketStart).trim();
                    category = questionPart.substring(bracketStart + 1, bracketEnd).trim();
                }

                InterviewQuestion question = new InterviewQuestion();
                question.setQuestionId(UUID.randomUUID().toString());
                question.setContent(questionContent);
                question.setCategory(category);
                question.setDifficulty(difficulty);
                question.setFollowUp(false);

                questions.add(question);
            }
        }

        return questions;
    }

    private List<InterviewQuestion> generateFallbackQuestions(String positionType, InterviewDifficulty difficulty) {
        List<InterviewQuestion> fallbackQuestions = new ArrayList<>();

        // 创建默认问题
        InterviewQuestion question1 = new InterviewQuestion();
        question1.setQuestionId(UUID.randomUUID().toString());
        question1.setContent("请简要描述你对" + positionType + "领域中某个关键技术的理解。");
        question1.setCategory("基础知识");
        question1.setDifficulty(difficulty);

        InterviewQuestion question2 = new InterviewQuestion();
        question2.setQuestionId(UUID.randomUUID().toString());
        question2.setContent("假设你遇到一个性能问题，你会采取哪些步骤来诊断和解决它？");
        question2.setCategory("问题解决");
        question2.setDifficulty(difficulty);

        fallbackQuestions.add(question1);
        fallbackQuestions.add(question2);

        return fallbackQuestions;
    }

    private InterviewQuestion createDefaultFollowUpQuestion(InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
        InterviewQuestion followUp = new InterviewQuestion();
        followUp.setQuestionId(UUID.randomUUID().toString());
        followUp.setContent("能否详细说明你之前回答中的某个关键部分？");
        followUp.setCategory(previousQuestion.getCategory());
        followUp.setDifficulty(difficulty);
        followUp.setFollowUp(true);
        followUp.setOriginalQuestionId(previousQuestion.getQuestionId());

        return followUp;
    }
}
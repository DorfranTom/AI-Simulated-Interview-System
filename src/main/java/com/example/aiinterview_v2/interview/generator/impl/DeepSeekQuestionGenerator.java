//package com.example.aiinterview_v2.interview.generator.impl;
//
//import com.example.aiinterview_v2.interview.generator.QuestionGenerator;
//import com.example.aiinterview_v2.interview.model.InterviewDifficulty;
//import com.example.aiinterview_v2.interview.model.InterviewQuestion;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//@Service
//public class DeepSeekQuestionGenerator implements QuestionGenerator {
//    private static final Logger log = LoggerFactory.getLogger(DeepSeekQuestionGenerator.class);
//    private final RestTemplate restTemplate;
//    private final String deepSeekApiUrl;
//    @Value("${deep.seek.api.key}")
//    private final String apiKey;
//
//    public DeepSeekQuestionGenerator(@Value("${deep.seek.api.key}") String apiKey) {
//        this.restTemplate = new RestTemplate();
//        this.apiKey = apiKey;
//        this.deepSeekApiUrl = "https://api.deepseek.com/v1/chat/completions";
//    }
//
//    @Override
//    public List<InterviewQuestion> generateQuestions(String positionType, InterviewDifficulty difficulty) {
//        String prompt = buildQuestionPrompt(positionType, difficulty, 3); // 生成3个问题
//
//        try {
//            DeepSeekRequest request = createRequest(prompt);
//            ResponseEntity<DeepSeekResponse> response = restTemplate.exchange(
//                    deepSeekApiUrl,
//                    HttpMethod.POST,
//                    new HttpEntity<>(request, createHeaders()),
//                    DeepSeekResponse.class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                return parseQuestionsFromResponse(response.getBody(), positionType, difficulty);
//            } else {
//                log.error("DeepSeek API 调用失败，状态码: {}", response.getStatusCode());
//                return generateFallbackQuestions(positionType, difficulty);
//            }
//        } catch (Exception e) {
//            log.error("调用 DeepSeek API 失败", e);
//            return generateFallbackQuestions(positionType, difficulty);
//        }
//    }
//
//    @Override
//    public InterviewQuestion generateFollowUpQuestion(String userAnswer, InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
//        String prompt = buildFollowUpPrompt(userAnswer, previousQuestion, difficulty);
//
//        try {
//            DeepSeekRequest request = createRequest(prompt);
//            ResponseEntity<DeepSeekResponse> response = restTemplate.exchange(
//                    deepSeekApiUrl,
//                    HttpMethod.POST,
//                    new HttpEntity<>(request, createHeaders()),
//                    DeepSeekResponse.class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                List<InterviewQuestion> questions = parseQuestionsFromResponse(response.getBody(),
//                        previousQuestion.getCategory(),
//                        difficulty);
//                if (!questions.isEmpty()) {
//                    InterviewQuestion followUp = questions.get(0);
//                    followUp.setFollowUp(true);
//                    followUp.setOriginalQuestionId(previousQuestion.getQuestionId());
//                    return followUp;
//                }
//            }
//
//            // 生成默认追问
//            return createDefaultFollowUpQuestion(previousQuestion, difficulty);
//        } catch (Exception e) {
//            log.error("生成追问失败", e);
//            return createDefaultFollowUpQuestion(previousQuestion, difficulty);
//        }
//    }
//
//    @Override
//    public InterviewQuestion generateNextQuestion(String positionType, InterviewDifficulty difficulty) {
//        List<InterviewQuestion> questions = generateQuestions(positionType, difficulty);
//        return questions.isEmpty() ? null : questions.get(0);
//    }
//
//    @Override
//    public List<InterviewQuestion> generateQuestions(int num) {
//        return null;
//    }
//
//    private String buildQuestionPrompt(String positionType, InterviewDifficulty difficulty, int count) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("你是一位专业的").append(positionType).append("面试官。请生成")
//                .append(count).append("个")
//                .append(difficulty.getChineseName()).append("难度的面试问题，")
//                .append("涵盖算法、系统设计、项目经验和前沿技术等方面。")
//                .append("每个问题后请用括号简要说明考察的知识点。")
//                .append("问题应具有实际场景和技术深度，例如：\n")
//                .append("1. 如何设计一个支持高并发的分布式缓存系统？(考察分布式系统设计、缓存策略)\n")
//                .append("2. 请解释一下Kafka的分区机制和数据一致性保证。(考察消息队列、分布式系统)\n")
//                .append("3. 编写一个函数，实现对大规模数据的快速去重。(考察算法设计、数据结构)\n\n")
//                .append("请按以下格式回答：\n")
//                .append("1. [问题内容] ([考察的知识点])\n")
//                .append("2. [问题内容] ([考察的知识点])\n")
//                .append("3. [问题内容] ([考察的知识点])");
//
//        return prompt.toString();
//    }
//
//    private String buildFollowUpPrompt(String userAnswer, InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("用户对问题 \"").append(previousQuestion.getContent())
//                .append("\" 的回答如下：\n")
//                .append(userAnswer).append("\n\n")
//                .append("请作为面试官，针对这个回答提出一个")
//                .append(difficulty.getChineseName()).append("难度的追问，")
//                .append("进一步考察候选人的技术深度或发现潜在的知识漏洞。")
//                .append("请直接给出追问问题，无需额外解释。");
//
//        return prompt.toString();
//    }
//
//    private DeepSeekRequest createRequest(String prompt) {
//        DeepSeekRequest request = new DeepSeekRequest();
//        request.setModel("deepseek-chat");
//        request.setTemperature(0.7);
//        request.setMessages(Collections.singletonList(
//                new DeepSeekRequest.Message("user", prompt)
//        ));
//        return request;
//    }
//
//    private HttpHeaders createHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//        return headers;
//    }
//
//    private List<InterviewQuestion> parseQuestionsFromResponse(DeepSeekResponse response, String positionType, InterviewDifficulty difficulty) {
//        List<InterviewQuestion> questions = new ArrayList<>();
//        if (response.getChoices() == null || response.getChoices().isEmpty()) {
//            return questions;
//        }
//
//        String content = response.getChoices().get(0).getMessage().getContent();
//        String[] lines = content.split("\n");
//
//        for (String line : lines) {
//            if (line.trim().isEmpty()) continue;
//
//            // 简单解析问题和知识点
//            int index = line.indexOf(". ");
//            if (index > 0 && index + 2 < line.length()) {
//                String questionPart = line.substring(index + 2);
//                String questionContent = questionPart;
//                String category = "综合";
//
//                // 尝试提取括号中的知识点
//                int bracketStart = questionPart.indexOf("(");
//                int bracketEnd = questionPart.indexOf(")");
//                if (bracketStart > 0 && bracketEnd > bracketStart) {
//                    questionContent = questionPart.substring(0, bracketStart).trim();
//                    category = questionPart.substring(bracketStart + 1, bracketEnd).trim();
//                }
//
//                InterviewQuestion question = new InterviewQuestion();
//                question.setQuestionId(UUID.randomUUID().toString());
//                question.setContent(questionContent);
//                question.setCategory(category);
//                question.setDifficulty(difficulty);
//                question.setFollowUp(false);
//
//                questions.add(question);
//            }
//        }
//
//        return questions;
//    }
//
//    private List<InterviewQuestion> generateFallbackQuestions(String positionType, InterviewDifficulty difficulty) {
//        List<InterviewQuestion> fallbackQuestions = new ArrayList<>();
//
//        // 创建默认问题
//        InterviewQuestion question1 = new InterviewQuestion();
//        question1.setQuestionId(UUID.randomUUID().toString());
//        question1.setContent("请简要描述你对" + positionType + "领域中某个关键技术的理解。");
//        question1.setCategory("基础知识");
//        question1.setDifficulty(difficulty);
//
//        InterviewQuestion question2 = new InterviewQuestion();
//        question2.setQuestionId(UUID.randomUUID().toString());
//        question2.setContent("假设你遇到一个性能问题，你会采取哪些步骤来诊断和解决它？");
//        question2.setCategory("问题解决");
//        question2.setDifficulty(difficulty);
//
//        fallbackQuestions.add(question1);
//        fallbackQuestions.add(question2);
//
//        return fallbackQuestions;
//    }
//
//    private InterviewQuestion createDefaultFollowUpQuestion(InterviewQuestion previousQuestion, InterviewDifficulty difficulty) {
//        InterviewQuestion followUp = new InterviewQuestion();
//        followUp.setQuestionId(UUID.randomUUID().toString());
//        followUp.setContent("能否详细说明你之前回答中的某个关键部分？");
//        followUp.setCategory(previousQuestion.getCategory());
//        followUp.setDifficulty(difficulty);
//        followUp.setFollowUp(true);
//        followUp.setOriginalQuestionId(previousQuestion.getQuestionId());
//
//        return followUp;
//    }
//
//    // 内部类：DeepSeek API 请求结构
//    static class DeepSeekRequest {
//        private String model;
//        private double temperature;
//        private List<Message> messages;
//
//        public static class Message {
//            private String role;
//            private String content;
//
//            public Message(String role, String content) {
//                this.role = role;
//                this.content = content;
//            }
//
//            public String getRole() {
//                return role;
//            }
//
//            public String getContent() {
//                return content;
//            }
//        }
//
//        public String getModel() {
//            return model;
//        }
//
//        public void setModel(String model) {
//            this.model = model;
//        }
//
//        public double getTemperature() {
//            return temperature;
//        }
//
//        public void setTemperature(double temperature) {
//            this.temperature = temperature;
//        }
//
//        public List<Message> getMessages() {
//            return messages;
//        }
//
//        public void setMessages(List<Message> messages) {
//            this.messages = messages;
//        }
//    }
//
//    // 内部类：DeepSeek API 响应结构
//    static class DeepSeekResponse {
//        private String id;
//        private String object;
//        private long created;
//        private List<Choice> choices;
//
//        public static class Choice {
//            private int index;
//            private Message message;
//            private String finish_reason;
//
//            public static class Message {
//                private String role;
//                private String content;
//
//                public String getRole() {
//                    return role;
//                }
//
//                public String getContent() {
//                    return content;
//                }
//            }
//
//            public int getIndex() {
//                return index;
//            }
//
//            public Message getMessage() {
//                return message;
//            }
//
//            public String getFinish_reason() {
//                return finish_reason;
//            }
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public String getObject() {
//            return object;
//        }
//
//        public long getCreated() {
//            return created;
//        }
//
//        public List<Choice> getChoices() {
//            return choices;
//        }
//    }
//}
//package com.example.aiinterview_v2.interview.evaluator.impl;
//
//import com.example.aiinterview_v2.interview.evaluator.AnswerEvaluator;
//import com.example.aiinterview_v2.interview.model.AnswerEvaluation;
//import com.example.aiinterview_v2.interview.model.InterviewQuestion;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.*;
//
//@Service
//public class DeepSeekAnswerEvaluator implements AnswerEvaluator {
//    private static final Logger log = LoggerFactory.getLogger(DeepSeekAnswerEvaluator.class);
//    private final RestTemplate restTemplate;
//    @Value("${deep.seek.api.key}")
//    private final String apiKey;
//    private final String deepSeekApiUrl;
//
//    public DeepSeekAnswerEvaluator(@Value("${deep.seek.api.key}") String apiKey) {
//        this.apiKey = apiKey;
//        this.restTemplate = new RestTemplate();
//        this.deepSeekApiUrl = "https://api.deepseek.com/v1/answer-evaluator";
//    }
//
//    @Override
//    public AnswerEvaluation evaluate(String userAnswer, InterviewQuestion question) {
//        String prompt = buildEvaluationPrompt(userAnswer, question);
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
//                return parseEvaluationFromResponse(response.getBody());
//            } else {
//                log.error("DeepSeek API 调用失败，状态码: {}", response.getStatusCode());
//                return createDefaultEvaluation();
//            }
//        } catch (Exception e) {
//            log.error("评估回答失败", e);
//            return createDefaultEvaluation();
//        }
//    }
//
//    @Override
//    public double evaluateAnswer(String questionId, String answer) {
//        return 0;
//    }
//
//    @Override
//    public String generateFeedback(String questionId, String answer) {
//        return null;
//    }
//
//    private String buildEvaluationPrompt(String userAnswer, InterviewQuestion question) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("作为一名专业的").append(question.getCategory()).append("面试官，")
//                .append("请评估候选人对以下问题的回答：\n\n")
//                .append("问题：").append(question.getContent()).append("\n\n")
//                .append("回答：").append(userAnswer).append("\n\n")
//                .append("请从以下维度进行评估（分数范围0-10分）：\n")
//                .append("1. 技术准确性\n")
//                .append("2. 深度与完整性\n")
//                .append("3. 逻辑清晰度\n")
//                .append("4. 实际应用能力\n")
//                .append("5. 创新性\n\n")
//                .append("请检查回答中是否存在关键技术错误或知识漏洞。\n")
//                .append("请按以下格式返回评估结果（JSON格式）：\n")
//                .append("{\n")
//                .append("  \"overallScore\": 7.5,\n")
//                .append("  \"hasCriticalFlaw\": false,\n")
//                .append("  \"dimensionScores\": {\n")
//                .append("    \"技术准确性\": 8,\n")
//                .append("    \"深度与完整性\": 7,\n")
//                .append("    \"逻辑清晰度\": 8,\n")
//                .append("    \"实际应用能力\": 6,\n")
//                .append("    \"创新性\": 5\n")
//                .append("  },\n")
//                .append("  \"knowledgeGaps\": [\n")
//                .append("    \"缺乏对分布式事务一致性模型的理解\",\n")
//                .append("    \"算法复杂度分析不准确\"\n")
//                .append("  ],\n")
//                .append("  \"suggestions\": [\n")
//                .append("    \"深入学习CAP定理和BASE理论\",\n")
//                .append("    \"加强算法设计和复杂度分析练习\"\n")
//                .append("  ]\n")
//                .append("}");
//
//        return prompt.toString();
//    }
//
//    private DeepSeekRequest createRequest(String prompt) {
//        DeepSeekRequest request = new DeepSeekRequest();
//        request.setModel("deepseek-chat");
//        request.setTemperature(0.2); // 降低随机性，提高评估稳定性
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
//    private AnswerEvaluation parseEvaluationFromResponse(DeepSeekResponse response) {
//        AnswerEvaluation evaluation = new AnswerEvaluation();
//
//        if (response.getChoices() == null || response.getChoices().isEmpty()) {
//            return evaluation;
//        }
//
//        String content = response.getChoices().get(0).getMessage().getContent();
//
//        try {
//            // 简单解析JSON格式的评估结果
//            // 实际项目中建议使用Jackson等JSON解析库
//            Map<String, Object> evalMap = parseJsonResponse(content);
//
//            if (evalMap.containsKey("overallScore")) {
//                evaluation.setOverallScore(Double.parseDouble(evalMap.get("overallScore").toString()));
//            }
//
//            if (evalMap.containsKey("hasCriticalFlaw")) {
//                evaluation.setHasCriticalFlaw(Boolean.parseBoolean(evalMap.get("hasCriticalFlaw").toString()));
//            }
//
//            if (evalMap.containsKey("dimensionScores")) {
//                @SuppressWarnings("unchecked")
//                Map<String, Object> dimensionScores = (Map<String, Object>) evalMap.get("dimensionScores");
//                for (Map.Entry<String, Object> entry : dimensionScores.entrySet()) {
//                    evaluation.addDimensionScore(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
//                }
//            }
//
//            if (evalMap.containsKey("knowledgeGaps")) {
//                @SuppressWarnings("unchecked")
//                List<String> knowledgeGaps = (List<String>) evalMap.get("knowledgeGaps");
//                evaluation.getKnowledgeGaps().addAll(knowledgeGaps);
//            }
//
//            if (evalMap.containsKey("suggestions")) {
//                @SuppressWarnings("unchecked")
//                List<String> suggestions = (List<String>) evalMap.get("suggestions");
//                evaluation.getSuggestions().addAll(suggestions);
//            }
//        } catch (Exception e) {
//            log.error("解析评估结果失败", e);
//            // 解析失败时返回默认评估
//            return createDefaultEvaluation();
//        }
//
//        return evaluation;
//    }
//
//    private Map<String, Object> parseJsonResponse(String json) {
//        // 简化的JSON解析（实际项目中建议使用专业库）
//        Map<String, Object> result = new HashMap<>();
//
//        // 移除花括号和首尾空格
//        json = json.trim();
//        if (json.startsWith("{")) json = json.substring(1);
//        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
//
//        // 分割键值对
//        String[] pairs = json.split(",");
//        for (String pair : pairs) {
//            String[] keyValue = pair.split(":", 2);
//            if (keyValue.length == 2) {
//                String key = keyValue[0].trim().replace("\"", "");
//                String value = keyValue[1].trim();
//
//                // 处理不同类型的值
//                if (value.startsWith("\"") && value.endsWith("\"")) {
//                    result.put(key, value.substring(1, value.length() - 1));
//                } else if (value.equals("true") || value.equals("false")) {
//                    result.put(key, Boolean.parseBoolean(value));
//                } else if (value.matches("\\d+(\\.\\d+)?")) {
//                    result.put(key, Double.parseDouble(value));
//                } else if (value.startsWith("[")) {
//                    // 解析数组
//                    List<String> list = new ArrayList<>();
//                    String arrayContent = value.substring(1, value.length() - 1);
//                    String[] items = arrayContent.split(",");
//                    for (String item : items) {
//                        item = item.trim();
//                        if (item.startsWith("\"") && item.endsWith("\"")) {
//                            list.add(item.substring(1, item.length() - 1));
//                        } else {
//                            list.add(item);
//                        }
//                    }
//                    result.put(key, list);
//                } else if (value.startsWith("{")) {
//                    // 递归解析嵌套对象
//                    result.put(key, parseJsonResponse(value));
//                }
//            }
//        }
//
//        return result;
//    }
//
//    private AnswerEvaluation createDefaultEvaluation() {
//        AnswerEvaluation evaluation = new AnswerEvaluation();
//        evaluation.setOverallScore(5.0);
//        evaluation.setHasCriticalFlaw(false);
//        evaluation.addDimensionScore("技术准确性", 5.0);
//        evaluation.addDimensionScore("深度与完整性", 5.0);
//        evaluation.addDimensionScore("逻辑清晰度", 5.0);
//        evaluation.addDimensionScore("实际应用能力", 5.0);
//        evaluation.addDimensionScore("创新性", 5.0);
//        evaluation.addSuggestion("回答内容较为基础，建议进一步深化技术理解。");
//        return evaluation;
//    }
//
//    // 内部类：DeepSeek API 请求结构（与问题生成器中的相同，实际项目中可提取到公共类）
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
//        }
//
//        // Getters and Setters
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
//    // 内部类：DeepSeek API 响应结构（与问题生成器中的相同，实际项目中可提取到公共类）
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
//            public InterviewQuestion getMessage() {
//                return null;
//            }
//
//            public static class Message {
//                private String role;
//                private String content;
//            }
//        }
//
//        // Getters and Setters
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
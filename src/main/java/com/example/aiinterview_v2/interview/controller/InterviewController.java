package com.example.aiinterview_v2.interview.controller;

import com.example.aiinterview_v2.interview.model.InterviewSession;
import com.example.aiinterview_v2.interview.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public List<InterviewSession> getAllSessions() {
        return interviewService.getAllSessions();
    }

    @PostMapping
    public InterviewSession createInterview(@RequestBody InterviewSession session) {
        return interviewService.startInterview(session.getCandidateName(), session.getPositionType());
    }
}
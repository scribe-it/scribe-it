package com.message_clasificator.controller;

import com.message_clasificator.models.AnalysisResponse;
import com.message_clasificator.services.ChatAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final ChatAnalysisService chatAnalysisService;

    public TestController(ChatAnalysisService chatAnalysisService) {
        this.chatAnalysisService = chatAnalysisService;
    }

    @GetMapping("/correr-simulacion")
    public AnalysisResponse test() {
        return chatAnalysisService.procesarDia();
    }
}

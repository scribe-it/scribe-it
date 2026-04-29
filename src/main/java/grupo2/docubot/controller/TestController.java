package grupo2.docubot.controller;

import grupo2.docubot.models.AnalysisResponse;
import grupo2.docubot.services.ChatAnalysisService;
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

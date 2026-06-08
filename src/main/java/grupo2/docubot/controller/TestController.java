package grupo2.docubot.controller;

import grupo2.docubot.services.GroqService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final GroqService groqService;

    public TestController(GroqService groqService) {
        this.groqService = groqService;
    }

//    @GetMapping("/correr-simulacion")
//    public AnalysisResponse test() {
//        return messageProcesor.getAllMessages();
//    }
}

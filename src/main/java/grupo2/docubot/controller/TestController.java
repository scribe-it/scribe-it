package grupo2.docubot.controller;

import grupo2.docubot.dto.internal.AnalysisResponse;
import grupo2.docubot.services.MessageProcesor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final MessageProcesor messageProcesor;

    public TestController(MessageProcesor messageProcesor) {
        this.messageProcesor = messageProcesor;
    }

//    @GetMapping("/correr-simulacion")
//    public AnalysisResponse test() {
//        return messageProcesor.getAllMessages();
//    }
}

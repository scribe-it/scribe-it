package grupo2.docubot.controller;

import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<MessageResponseDto>> getAllByChatId(@PathVariable Long id){

        List<MessageResponseDto> chat_messages = messageService.getAllByChatId(id);

        return ResponseEntity.ok().body(chat_messages);

    }
}

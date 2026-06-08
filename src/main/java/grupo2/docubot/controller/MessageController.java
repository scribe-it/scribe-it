package grupo2.docubot.controller;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import grupo2.docubot.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@Valid @RequestBody MessageRequestDto messageRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(messageRequestDto));
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<MessageResponseDto>> getAllByChatId(@PathVariable Long id){

        List<MessageResponseDto> chat_messages = messageService.getAllByChatId(id);

        return ResponseEntity.ok().body(chat_messages);

    }

    @PostMapping("/chats/messages/forward")
    public ResponseEntity<MessageResponseDto> forwardMessage(@RequestParam Long originalMessageId,
                                                             @RequestParam Long toChatId,
                                                             @RequestParam Long senderId) {

        MessageResponseDto forwarded = messageService.forwardMessage(originalMessageId, toChatId, senderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(forwarded);
    }


}

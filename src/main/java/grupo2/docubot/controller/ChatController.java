package grupo2.docubot.controller;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponseDto> createChat(@RequestBody ChatRequestDto chatRequestDto){
        ChatResponseDto newChat = chatService.createChat(chatRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newChat);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponseDto>> getChats() {
        List<ChatResponseDto> chats = chatService.getAll();
        return ResponseEntity.ok()
                .body(chats);
    }

}

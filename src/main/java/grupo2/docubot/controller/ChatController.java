package grupo2.docubot.controller;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.User;
import grupo2.docubot.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.PATCH;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
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

    @PatchMapping("/{chatId}/add/{userId}")
    @PreAuthorize("hasRole('ANALISTA')")
    public ResponseEntity<List<User>> addUser(@PathVariable Long chatId,@PathVariable Long userId){
        return ResponseEntity.ok(chatService.addUser(chatId,userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDto> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getById(id));
    }


}

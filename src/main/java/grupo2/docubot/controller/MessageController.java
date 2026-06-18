package grupo2.docubot.controller;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import grupo2.docubot.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Mensajes", description="Mensajes individuales dentro de los chats")
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Obtener los mensajes del chat")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de mensajes del chat"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Chat no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<MessageResponseDto>> getAllByChatId(@PathVariable Long id){

        List<MessageResponseDto> chat_messages = messageService.getAllByChatId(id);

        return ResponseEntity.ok().body(chat_messages);

    }

//    @Operation(summary = "Reenviar mensaje")
//    @PostMapping("/chats/messages/forward")
//    public ResponseEntity<MessageResponseDto> forwardMessage(@RequestParam Long originalMessageId,
//                                                             @RequestParam Long toChatId,
//                                                             @RequestParam Long senderId) {
//
//        MessageResponseDto forwarded = messageService.forwardMessage(originalMessageId, toChatId, senderId);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(forwarded);
//    }

}

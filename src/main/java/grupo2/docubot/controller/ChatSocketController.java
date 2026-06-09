package grupo2.docubot.controller;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.services.ChatService;
import grupo2.docubot.services.MessageProcesor;
import grupo2.docubot.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/chat-send")
    public void sendMessage(MessageRequestDto messageRequestDto) {

        MessageResponseDto savedMessage = messageService.createMessage(messageRequestDto);

        simpMessagingTemplate.convertAndSend(
            "/topic/chat/" + messageRequestDto.getChatId(),
            savedMessage
        );

    }
}

package grupo2.docubot.controller;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.User;
import grupo2.docubot.services.MessageService;
import grupo2.docubot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    @MessageMapping("/chat-send")
    public void sendMessage(MessageRequestDto messageRequestDto) {

        MessageResponseDto savedMessage = messageService.createMessage(messageRequestDto);

        Long chatId = messageRequestDto.getChatId();

        simpMessagingTemplate.convertAndSend(
            "/topic/chat/" + chatId,
            savedMessage
        );
        User analyst = userService.findAnalyst();
        Long unreadChatMessagesCount = messageService.getUnreadChatMessagesCount(chatId);
        if(analyst.getId() != messageRequestDto.getSenderId()) {
            simpMessagingTemplate.convertAndSendToUser(
                    analyst.getEmail(),
                    "/queue/unread",
                    Map.of("chatId", chatId, "count", unreadChatMessagesCount)
            );
        }

    }


}

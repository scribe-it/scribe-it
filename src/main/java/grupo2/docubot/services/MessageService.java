package grupo2.docubot.services;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.mappers.MessageMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final MessageMapper messageMapper;

    public MessageResponseDto createMessage(MessageRequestDto messageRequestDto) {

        Chat chat = chatService.getChatById(messageRequestDto.getChatId())
            .orElseThrow(() -> new RuntimeException("Chat not found with id: " + messageRequestDto.getChatId()));

        User sender = userService.getUserById(messageRequestDto.getSenderId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + messageRequestDto.getSenderId()));

        Message newMessage = messageMapper.toEntity(messageRequestDto);

        newMessage.setChat(chat);
        newMessage.setUser(sender);

        Message savedMessage = messageRepository.save(newMessage);

        return messageMapper.toDto(savedMessage);

    }

    public List<MessageResponseDto> getAllByChatId(Long chatId) {

        List<Message> chat_messages = messageRepository.findAllByChatId(chatId);

        return chat_messages.stream()
                .map(messageMapper::toDto)
                .toList();
    }

}

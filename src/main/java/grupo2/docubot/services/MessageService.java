package grupo2.docubot.services;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.mappers.MessageMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.ChatRepository;
import grupo2.docubot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;

    public MessageResponseDto createMessage(MessageRequestDto messageRequestDto) {

        Chat chat = chatRepository.findById(messageRequestDto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User sender = userService.findById(messageRequestDto.getSenderId());

        Message newMessage = messageMapper.toEntity(messageRequestDto);

        newMessage.setChat(chat);
        newMessage.setUser(sender);

        boolean isAnalyst = sender.getRole().stream().anyMatch(r -> "ANALISTA".equals(r.getName()));

        if (isAnalyst) {
            newMessage.setIsRead(true);
        }

        Message savedMessage = messageRepository.save(newMessage);

        return messageMapper.toDto(savedMessage);

    }

    public MessageResponseDto forwardMessage(Long originalMessageId, Long toChatId, Long senderId) {

        Message originalMessage = messageRepository.findById(originalMessageId)
                .orElseThrow(() -> new RuntimeException("Original message not found with id: " + originalMessageId));

        Chat toChat = chatRepository.findById(toChatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        User sender = userService.findById(senderId);

        Message forwardedMessage = new Message();
        forwardedMessage.setContent(originalMessage.getContent());
        forwardedMessage.setType(originalMessage.getType());

        forwardedMessage.setChat(toChat);
        forwardedMessage.setUser(sender);

        Message savedMessage = messageRepository.save(forwardedMessage);

        /*
            lógica de qué hago con el mensaje reenviado al chat dedicado
        */

        return messageMapper.toDto(savedMessage);
    }

    public List<MessageResponseDto> getAllByChatId(Long chatId){
        return messageRepository.findAllByChatId(chatId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public List<MessageResponseDto> getAllDocubot() {
        Chat docubot = chatRepository.findByDepartment("docubot");

        return messageRepository.findAllByChatId(docubot.getId())
                .stream()
                .filter(m -> !m.getIsProcessed())
                .map(messageMapper::toDto)
                .toList();
    }

    public List<MessageResponseDto> markAsProcessed(List<Long> messagesIds) {
        List<Message> entities = messageRepository.findAllById(messagesIds);
        entities.forEach(m -> m.setIsProcessed(true));
        List<Message> saved = messageRepository.saveAll(entities);
        return saved.stream().map(messageMapper::toDto).toList();
    }

    public Long getUnreadChatMessagesCount(Long chatId) {

        return messageRepository.countByChatIdAndIsReadFalse(chatId);

    }

    public Void markChatMessagesAsRead(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatId(chatId);

        messages.forEach(msg -> {
            msg.setIsRead(true);
            msg.setIsReadAt(LocalDateTime.now());
        });

        messageRepository.saveAll(messages);

        return null;
    }
}

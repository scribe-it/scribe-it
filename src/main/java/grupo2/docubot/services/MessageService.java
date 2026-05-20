package grupo2.docubot.services;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.mappers.MessageMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import grupo2.docubot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageResponseDto createMessage(MessageRequestDto messageRequestDto) {

        Message newMessage = messageMapper.toEntity(messageRequestDto);

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

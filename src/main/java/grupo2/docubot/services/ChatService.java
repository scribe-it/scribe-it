package grupo2.docubot.services;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.mappers.ChatMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public ChatResponseDto createChat(ChatRequestDto chatRequestDto) {

        Chat newChat = chatMapper.toEntity(chatRequestDto);

        Chat savedChat = chatRepository.save(newChat);

        return chatMapper.toDto(savedChat);

    }

    public List<ChatResponseDto> getAll() {

        List<Chat> chats = chatRepository.findAll();

        return chats.stream()
                .map(chatMapper::toDto)
                .toList();
    }

}

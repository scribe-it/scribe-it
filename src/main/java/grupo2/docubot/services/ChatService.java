package grupo2.docubot.services;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.exceptions.response.RecourseNotFound;
import grupo2.docubot.mappers.ChatMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.ChatRepository;
import grupo2.docubot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;

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

    public Chat getById(Long id) {

        return chatRepository.findById(id)
                .orElseThrow(()-> new RecourseNotFound("El Chat con id "+ id +" no fue encontrado"));

    }

    @Transactional
    public List<User> addUser(Long chatId,Long userId){
        //Validando la existencia del chat
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(()-> new RecourseNotFound("El Chat con id "+ chatId +" no fue encontrado"));
        //Validando la existencia del usuario en el sistema
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RecourseNotFound("El Usuario con id "+ userId +" no fue encontrado"));

        //Añadiendo el usuario
        chat.getUsers().add(user);

        //Actualizando el chat
        chatRepository.save(chat);

        //Retornamos la lista de usuarios actualizada
        return chat.getUsers();
    }
}

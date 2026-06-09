package grupo2.docubot.services;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.exceptions.response.RecourseNotFound;
import grupo2.docubot.mappers.ChatMapper;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.User;
import grupo2.docubot.repository.ChatRepository;
import grupo2.docubot.security.MainUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserService userService;

    public ChatResponseDto createChat(ChatRequestDto chatRequestDto) {

        Chat newChat = chatMapper.toEntity(chatRequestDto);

        List<User> users = userService.getByDepartment(chatRequestDto.getDepartment());

        newChat.setUsers(users);

        Chat savedChat = chatRepository.save(newChat);

        return chatMapper.toDto(savedChat);

    }

    public List<ChatResponseDto> getAll() {

        List<Chat> chats = chatRepository.findAll();

        return chats.stream()
                .map(chatMapper::toDto)
                .toList();
    }

    public Chat getEntityById(Long id) {

        return chatRepository.findById(id)
                .orElseThrow(()-> new RecourseNotFound("El Chat con id "+ id +" no fue encontrado"));

    }

    public ChatResponseDto getById(Long id) {

        Chat chat = chatRepository.findById(id)
                .orElseThrow(()-> new RecourseNotFound("El Chat con id "+ id +" no fue encontrado"));

        //Puede haber usuarios que se dieron de alta después de creado el chat. Por eso lo hacemos manual
        List<User> users = userService.getByDepartment(chat.getDepartment());

        chat.setUsers(users);

        return chatMapper.toDto(chat);

    }

    public List<ChatResponseDto> getAllChatsByUserId(Long userId) {

        return chatRepository.findAllByUserId(userId).stream()
                .map(chatMapper::toDto)
                .toList();
    }

    public ChatResponseDto getByDepartment(@AuthenticationPrincipal MainUser user) {

        Chat chat = chatRepository.findByDepartment(user.getDepartment());

        return chatMapper.toDto(chat);
    }
    @Transactional(readOnly = true)
    public ChatResponseDto getDocubot() {

        Chat chat = chatRepository.findByDepartment("docubot");

        return chatMapper.toDto(chat);
    }

    @Transactional
    public List<User> addUser(Long chatId,Long userId){
        //Validando la existencia del chat
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(()-> new RecourseNotFound("El Chat con id "+ chatId +" no fue encontrado"));
        //Validando la existencia del usuario en el sistema
        User user = userService.findById(userId);

        //Añadiendo el usuario
        chat.getUsers().add(user);

        //Actualizando el chat
        chatRepository.save(chat);

        //Retornamos la lista de usuarios actualizada
        return chat.getUsers();
    }
}

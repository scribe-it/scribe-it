package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.models.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class, MessageMapper.class })
public interface ChatMapper {

    Chat toEntity(ChatRequestDto chatRequestDto);

    ChatResponseDto toDto(Chat chat);

}

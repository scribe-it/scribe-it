package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.models.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    Chat toEntity(ChatRequestDto chatRequestDto);

    @Mapping(target="user_id", source="user.id")
    ChatResponseDto toDto(Chat chat);


}

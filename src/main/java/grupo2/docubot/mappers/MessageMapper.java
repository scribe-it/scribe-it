package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target="chat.id", source="chatId")
    @Mapping(target="user.id", source="senderId")
    Message toEntity(MessageRequestDto messageRequestDto);

    @Mapping(target="user_id", source="user.id")
    MessageResponseDto toDto(Message message);

}

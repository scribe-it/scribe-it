package grupo2.docubot.mappers;

import dev.langchain4j.model.chat.request.ChatRequest;
import grupo2.docubot.dto.request.ChatRequestDto;
import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.response.ChatResponseDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Chat;
import grupo2.docubot.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    Chat toEntity(ChatRequestDto chatRequestDto);

    @Mapping(target="user_id", source="user.id")
    ChatResponseDto toDto(Chat chat);


}

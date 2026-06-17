package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.DocumentCommentRequestDto;
import grupo2.docubot.dto.response.DocumentCommentResponseDto;
import grupo2.docubot.models.DocumentComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    DocumentComment toEntity(DocumentCommentRequestDto dto);

    @Mapping(source = "user.firstName", target = "authorName")
    DocumentCommentResponseDto toDto(DocumentComment documentComment);
}

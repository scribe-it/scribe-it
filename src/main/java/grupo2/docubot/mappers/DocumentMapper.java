package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.DocumentRequestDto;
import grupo2.docubot.dto.response.DocumentResponseDto;
import grupo2.docubot.models.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(DocumentRequestDto documentRequestDto);

    DocumentResponseDto toDto(Document document);
}

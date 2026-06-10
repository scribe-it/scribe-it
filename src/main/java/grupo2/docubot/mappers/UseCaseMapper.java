package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.UseCaseRequestDto;
import grupo2.docubot.dto.response.UseCaseResponseDto;
import grupo2.docubot.models.UseCase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UseCaseMapper {
    UseCaseResponseDto toDto(UseCase useCase);
    UseCase toEntity(UseCaseRequestDto request);
}

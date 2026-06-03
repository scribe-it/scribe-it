package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.UserRegisterRequestDto;
import grupo2.docubot.dto.response.UserResponseRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(target = "fullName", expression = "java(registerRequest.getFirstName() + ' ' + registerRequest.getLastName())")
    UserResponseRegisterDto toDto(UserRegisterRequestDto registerRequest);
}

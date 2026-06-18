package grupo2.docubot.mappers;

import grupo2.docubot.dto.request.MessageRequestDto;
import grupo2.docubot.dto.request.UserRequestDto;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.dto.response.UserResponseDto;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.Role;
import grupo2.docubot.models.User;
import grupo2.docubot.models.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    default RoleName map(Role role) {
        return role != null ? role.getName() : null;
    }
}

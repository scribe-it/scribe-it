package grupo2.docubot.dto.response;

import grupo2.docubot.models.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private Role role;
    private String email;
    private String department;
}

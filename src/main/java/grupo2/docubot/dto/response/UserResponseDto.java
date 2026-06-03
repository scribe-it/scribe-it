package grupo2.docubot.dto.response;

import grupo2.docubot.models.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private Role role;
    private String department;
}

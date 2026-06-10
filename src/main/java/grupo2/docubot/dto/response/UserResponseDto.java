package grupo2.docubot.dto.response;

import grupo2.docubot.models.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private Set<Role> role;
    private String email;
    private String department;
}

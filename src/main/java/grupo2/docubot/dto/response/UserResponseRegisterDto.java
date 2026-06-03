package grupo2.docubot.dto.response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @Builder
public class UserResponseRegisterDto {
    private String fullName;
    private String email;
}

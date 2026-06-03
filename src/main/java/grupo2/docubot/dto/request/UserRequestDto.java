package grupo2.docubot.dto.request;

import grupo2.docubot.models.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String department;
}

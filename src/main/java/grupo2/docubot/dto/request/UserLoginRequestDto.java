package grupo2.docubot.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class UserLoginRequestDto {
    @NotBlank(message="El email es requerido")
    @Email(message = "Error en el formato del email")
    private String email;
    @NotBlank(message = "La contraseña es requida")
    private String password;
}

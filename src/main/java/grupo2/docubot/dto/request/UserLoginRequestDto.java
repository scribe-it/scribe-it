package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Schema(description = "Credenciales de inicio de sesión")
public class UserLoginRequestDto {
    @NotBlank(message="El email es requerido")
    @Email(message = "Error en el formato del email")
    @Schema(description = "Email del usuario", example = "analista@docubot.com")
    private String email;
    @NotBlank(message = "La contraseña es requida")
    @Schema(description = "Contraseña del usuario", example = "Password1!")
    private String password;
}

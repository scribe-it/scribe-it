package grupo2.docubot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @AllArgsConstructor @Builder
@Schema(description = "Respuesta después de registrar un usuario")
public class UserResponseRegisterDto {
    @Schema(description = "Nombre completo del usuario registrado", example = "Juan Pérez")
    private String fullName;
    @Schema(description = "Email del usuario registrado", example = "experto@docubot.com")
    private String email;
}

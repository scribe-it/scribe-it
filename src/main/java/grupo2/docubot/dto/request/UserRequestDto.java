package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos básicos de usuario")
public class UserRequestDto {
    @Schema(description = "ID del chat asociado")
    private Long chatId;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;
    @Schema(description = "Departamento del usuario", example = "Ventas")
    private String department;
}

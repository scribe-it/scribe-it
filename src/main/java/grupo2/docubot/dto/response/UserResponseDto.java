package grupo2.docubot.dto.response;

import grupo2.docubot.models.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos del usuario")
public class UserResponseDto {
    @Schema(description = "Roles del usuario", example = "[\"ANALISTA\"]")
    private Set<RoleName> role;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;
    @Schema(description = "Email del usuario", example = "usuario@docubot.com")
    private String email;
    @Schema(description = "Departamento del usuario", example = "Ventas")
    private String department;
}

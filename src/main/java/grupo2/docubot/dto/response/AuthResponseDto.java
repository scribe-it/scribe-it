package grupo2.docubot.dto.response;


import grupo2.docubot.models.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Respuesta de autenticación con token JWT")
public class AuthResponseDto {
    @Schema(description = "Token JWT de autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(description = "Rol del usuario (ANALISTA o EXPERTO)")
    private RoleName role;
    @Schema(description = "ID del usuario", example = "1")
    private Long userId;
    @Schema(description = "Email del usuario", example = "analista@docubot.com")
    private String username;
    @Schema(description = "Departamento del usuario", example = "Ventas")
    private String department;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;
}

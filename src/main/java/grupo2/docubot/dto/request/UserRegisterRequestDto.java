package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter  @Setter  @AllArgsConstructor  @NoArgsConstructor  @Builder
@Schema(description = "Datos para registrar un nuevo usuario")
public class UserRegisterRequestDto {

    @NotBlank(message="El nombre es requerido")
    @Size(min = 2,message = "El nombbre no puede tener menos de 2 caracteres")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;

    @NotBlank(message="El apellido es requerido")
    @Size(min = 2,message = "El apellido no puede tener menos de 2 caracteres")
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Error no se sigue el formato email")
    @Schema(description = "Email del usuario", example = "experto@docubot.com")
    private String email;

    @NotNull(message = "El rol es requerido")
    @NotEmpty(message = "Debe seleccionar al menos un rol")
    @Schema(description = "IDs de los roles a asignar", example = "[2]")
    private Set<Long> roleId;

    @NotBlank(message = "La contraseña es requerida")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).+$",
            message = "La contraseña debe contener al menos una letra, un número y un carácter especial")
    @Schema(description = "Contraseña (debe contener letra, número y carácter especial)", example = "Password1!")
    private String password;

    @NotBlank(message = "El departamento es requerido")
    @Schema(description = "Departamento del usuario", example = "Ventas")
    private String department;
}

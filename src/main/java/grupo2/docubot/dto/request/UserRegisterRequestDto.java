package grupo2.docubot.dto.request;

import grupo2.docubot.models.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter  @Setter  @AllArgsConstructor  @NoArgsConstructor  @Builder
public class UserRegisterRequestDto {

    @NotBlank(message="El nombre es requerido")
    @Min(value = 2,message = "El nombbre no puede tener menos de 2 caracteres")
    private String firstName;

    @NotBlank(message="El apellido es requerido")
    @Min(value = 2,message = "El apellido no puede tener menos de 2 caracteres")
    private String lastName;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Error no se sigue el formato email")
    private String email;

    @NotNull(message = "El rol es requerido")
    private Set<Role> role;

    @NotBlank(message = "La contraseña es requerida")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).+$",
            message = "La contraseña debe contener al menos una letra, un número y un carácter especial")
    private String password;
}

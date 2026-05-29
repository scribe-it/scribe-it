package grupo2.docubot.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatRequestDto {

    @NotNull(message = "El departamento es requerido")
    private Long departmentId;

    @NotBlank(message = "La descripcion es requerida")
    private String description;

}

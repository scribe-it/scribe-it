package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "Datos para crear un nuevo chat")
public class ChatRequestDto {

    @NotNull(message = "El departamento es requerido")
    @Schema(description = "Departamento asociado al chat", example = "Ventas")
    private String department;

    @NotBlank(message = "La descripcion es requerida")
    @Schema(description = "Descripción del motivo del chat", example = "Consultas sobre el módulo de ventas")
    private String description;

}

package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos de un caso de uso")
public class UseCaseRequestDto {
    @NotNull(message = "id requerido")
    @Schema(description = "ID del caso de uso (requerido para asociar a un documento)", example = "1")
    private Long id;
    @NotBlank(message = "actor es requerido")
    @Schema(description = "Actor que ejecuta el caso de uso", example = "Vendedor")
    private String actor;
    @NotBlank(message = "precondition es requerido")
    @Schema(description = "Precondición para ejecutar el caso de uso", example = "El usuario debe estar logueado en el sistema")
    private String precondition;
    @NotBlank(message = "trigger es requerido")
    @Schema(description = "Evento que dispara el caso de uso", example = "El vendedor selecciona 'Nueva venta'")
    private String trigger;
    @NotBlank(message = "main_flow es requerido")
    @Schema(description = "Flujo principal del caso de uso", example = "1. El vendedor ingresa los datos del cliente\n2. El sistema valida la información\n3. Se genera la orden de venta")
    private String main_flow;
    @NotBlank(message = "postcondition es requerido")
    @Schema(description = "Postcondición después de ejecutar el caso de uso", example = "La orden de venta queda registrada en el sistema")
    private String postcondition;

}

package grupo2.docubot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Caso de uso con su flujo completo")
public class UseCaseResponseDto {
    @Schema(description = "ID del caso de uso", example = "1")
    private Long id;
    @Schema(description = "Actor que ejecuta el caso de uso", example = "Vendedor")
    private String actor;
    @Schema(description = "Precondición", example = "Usuario logueado")
    private String precondition;
    @Schema(description = "Disparador", example = "Selecciona Nueva venta")
    private String trigger;
    @Schema(description = "Flujo principal del caso de uso")
    private String main_flow;
    @Schema(description = "Postcondición", example = "Orden registrada")
    private String postcondition;
}

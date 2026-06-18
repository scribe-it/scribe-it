package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para crear un nuevo documento")
public class DocumentRequestDto {

    @Schema(hidden = true)
    private Long id;

    @NotBlank
    @Schema(description = "Título del documento", example = "Análisis funcional - Módulo Ventas")
    private String title;

    @NotEmpty
    @Schema(description = "Lista de casos de uso del documento")
    private List<UseCaseRequestDto> content;

}

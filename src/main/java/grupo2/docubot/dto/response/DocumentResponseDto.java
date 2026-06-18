package grupo2.docubot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Documento con sus casos de uso")
public class DocumentResponseDto {

    @Schema(description = "ID del documento", example = "1")
    private Long id;

    @Schema(description = "Título del documento", example = "Análisis funcional - Módulo Ventas")
    private String title;

    @Schema(description = "Lista de casos de uso del documento")
    private List<UseCaseResponseDto> content;

    @Schema(description = "Indica si el documento está publicado")
    private Boolean published;
}

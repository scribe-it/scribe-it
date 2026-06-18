package grupo2.docubot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Comentario asociado a un documento")
public class DocumentCommentResponseDto {

    @Schema(description = "ID del comentario", example = "1")
    private Long id;

    @Schema(description = "Contenido del comentario", example = "Falta detallar la validación de stock")
    private String text;

    @Schema(description = "Nombre del autor del comentario", example = "Juan Pérez")
    private String authorName;

    @Schema(description = "Fecha y hora de creación")
    private LocalDateTime createdAt;
}

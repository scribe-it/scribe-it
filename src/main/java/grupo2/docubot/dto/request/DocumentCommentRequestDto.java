package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Texto de un comentario")
public class DocumentCommentRequestDto {

    @NotBlank
    @Schema(description = "Contenido del comentario", example = "Este caso de uso debería incluir la validación de stock")
    private String text;
}

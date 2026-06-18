package grupo2.docubot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Mensaje individual dentro de un chat")
public class MessageResponseDto {

    @Schema(description = "ID del mensaje", example = "1")
    private Long id;

    @Schema(description = "Contenido del mensaje", example = "¿Cómo maneja el sistema la devolución?")
    private String content;

    @Schema(description = "ID del usuario que envió el mensaje", example = "1")
    private Long user_id;

    @Schema(description = "Indica si el mensaje fue leído")
    private Boolean read;

    @Schema(description = "Fecha y hora de envío")
    private LocalDateTime timestamp;

    @Schema(description = "ID del chat al que pertenece", example = "1")
    private Long chat_id;

}

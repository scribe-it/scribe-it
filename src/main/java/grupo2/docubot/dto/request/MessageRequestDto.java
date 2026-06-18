package grupo2.docubot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import grupo2.docubot.models.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos de un mensaje")
public class MessageRequestDto {

    @Schema(hidden = true)
    private Long id;

    @NotNull
    @Schema(description = "ID del chat al que pertenece el mensaje", example = "1")
    private Long chatId;
    
    @NotBlank
    @Schema(description = "Contenido del mensaje", example = "¿Cómo maneja el sistema la devolución de productos?")
    private String content;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de mensaje (USER, AI, SYSTEM)")
    private MessageType type;

    @NotNull
    @Schema(description = "ID del usuario que envía el mensaje", example = "1")
    private Long senderId;

    @Schema(description = "Indica si el mensaje fue procesado por la IA")
    private Boolean processed;

}

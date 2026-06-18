package grupo2.docubot.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Schema(description = "Chat con sus mensajes y participantes")
public class ChatResponseDto {

    @Schema(description = "ID del chat", example = "1")
    private Long id;

    @Schema(description = "Departamento asociado", example = "Ventas")
    private String department;
    
    @Schema(description = "Descripción del chat", example = "Consultas sobre ventas")
    private String description;

    @Schema(description = "Mensajes del chat")
    private List<MessageResponseDto> messages;

    @Schema(description = "Usuarios participantes del chat")
    private List<UserResponseDto> users;

    @Schema(description = "Cantidad de mensajes no leídos", example = "3")
    private Long unreadCount;
}

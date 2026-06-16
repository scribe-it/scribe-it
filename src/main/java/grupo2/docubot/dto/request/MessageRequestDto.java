package grupo2.docubot.dto.request;

import grupo2.docubot.models.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {

    private Long id;

    @NotNull
    private Long chatId;
    
    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @NotNull
    private Long senderId;

    private Boolean processed;

}

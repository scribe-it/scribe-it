package grupo2.docubot.dto.request;

import java.time.LocalDateTime;

import grupo2.docubot.models.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MessageRequestDto {


    @NotNull
    private Long chatId;
    
    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @NotNull
    private Long senderId;

}

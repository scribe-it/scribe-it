package grupo2.docubot.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDto {

    private Long id;

    private String content;

    private Long user_id;

    private Boolean read;

    private LocalDateTime timestamp;

    private Long chat_id;

}

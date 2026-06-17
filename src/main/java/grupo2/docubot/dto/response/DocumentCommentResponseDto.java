package grupo2.docubot.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentCommentResponseDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime createdAt;
}

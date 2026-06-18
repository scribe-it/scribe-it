package grupo2.docubot.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String details;
    private String path;

    public ErrorResponseDto(String message, int status, String details, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
    }
}

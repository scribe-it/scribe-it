package grupo2.docubot.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class ChatRequestDto {

    @Column(nullable = false)
    private Long departmentId;

    private String description;

}

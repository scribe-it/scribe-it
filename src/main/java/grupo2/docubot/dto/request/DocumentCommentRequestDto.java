package grupo2.docubot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCommentRequestDto {

    @NotBlank
    private String text;
}

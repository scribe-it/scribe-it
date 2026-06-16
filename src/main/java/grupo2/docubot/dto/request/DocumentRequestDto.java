package grupo2.docubot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequestDto {

    private Long id;

    @NotBlank
    private String title;

    @NotEmpty
    private List<UseCaseRequestDto> content;



}

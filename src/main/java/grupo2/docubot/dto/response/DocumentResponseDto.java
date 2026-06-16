package grupo2.docubot.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentResponseDto {

    private Long id;

    private String title;

    private List<UseCaseResponseDto> content;

}

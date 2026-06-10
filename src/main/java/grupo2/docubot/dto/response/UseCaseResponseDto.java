package grupo2.docubot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UseCaseResponseDto {
    private String actor;
    private String precondition;
    private String trigger;
    private String main_flow;
    private String postcondition;
}

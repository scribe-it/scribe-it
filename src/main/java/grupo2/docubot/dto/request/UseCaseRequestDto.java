package grupo2.docubot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UseCaseRequestDto {
    @NotNull(message = "id requerido")
    private Long id;
    @NotBlank(message = "actor es requerido")
    private String actor;
    @NotBlank(message = "precondition es requerido")
    private String precondition;
    @NotBlank(message = "trigger es requerido")
    private String trigger;
    @NotBlank(message = "main_flow es requerido")
    private String main_flow;
    @NotBlank(message = "postcondition es requerido")
    private String postcondition;

}

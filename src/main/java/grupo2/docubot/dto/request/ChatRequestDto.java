package grupo2.docubot.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatRequestDto {

    @NotNull(message = "El departamento es requerido")
    private Long departmentId;

    @NotBlank(message = "La descripcion es requerida")
    private String description;

    private Boolean isDedicated;

}

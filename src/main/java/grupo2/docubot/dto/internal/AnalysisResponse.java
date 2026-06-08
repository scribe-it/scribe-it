package grupo2.docubot.dto.internal;

import grupo2.docubot.models.UseCase;
import lombok.*;

import java.util.List;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class AnalysisResponse {
    public List<UseCase> use_cases;
}

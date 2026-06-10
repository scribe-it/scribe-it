package grupo2.docubot.dto.internal;

import grupo2.docubot.models.UseCase;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisResponse {
    public List<UseCase> use_cases;
}

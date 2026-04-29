package grupo2.docubot.models;

    import lombok.*;

    import java.util.List;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UseCases {
        private String actor;

        private String precondition;

        private String trigger;

        private List<String> main_flow;

        private String poscondition;
    }

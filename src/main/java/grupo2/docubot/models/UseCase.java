package grupo2.docubot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actor;

    private String precondition;

    @Column(name = "trigger_event")
    private String trigger;

    @Column(columnDefinition = "TEXT")
    private String main_flow;

    @Column(name = "postcondition")
    private String postcondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private UseCaseHistory history;
}

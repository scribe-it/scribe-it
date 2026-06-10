package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actor;

    private String precondition;

    @Column(name = "trigger_event")
    private String trigger;

    private String main_flow;

    private String postcondition;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private UseCaseHistory history;
}

package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter  @Setter  @AllArgsConstructor  @NoArgsConstructor  @Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
}

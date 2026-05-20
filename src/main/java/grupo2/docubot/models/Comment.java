package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name="to_user_id", nullable = false)
    private User toUser;

    private String text;
}

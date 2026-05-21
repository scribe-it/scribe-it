package grupo2.docubot.models;

import grupo2.docubot.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    private Role role;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable
    private List<Chat> chats;

    private String department;
}

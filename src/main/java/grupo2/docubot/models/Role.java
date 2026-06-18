package grupo2.docubot.models;

import grupo2.docubot.models.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permisson",
            joinColumns=@JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")

    )
    private Set<Permission> permissions;
}

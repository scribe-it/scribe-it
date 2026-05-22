package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Role {
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany
    @ManyToMany
    @JoinTable(
            name = "role_permisson",
            joinColumns=@JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")

    )
    private Set<Permission> permissions;
}

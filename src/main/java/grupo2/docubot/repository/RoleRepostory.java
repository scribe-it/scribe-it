package grupo2.docubot.repository;

import grupo2.docubot.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepostory extends JpaRepository<Role,Long> {

}

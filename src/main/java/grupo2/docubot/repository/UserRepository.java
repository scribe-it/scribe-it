package grupo2.docubot.repository;

import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
}

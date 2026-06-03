package grupo2.docubot.repository;

import grupo2.docubot.models.CustomUserDetails;
import grupo2.docubot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    List<User> findByDepartment(String department);

    @Query("SELECT DISTINCT u.department FROM User u ORDER BY u.department")
    List<String> findDistinctDepartments();
}

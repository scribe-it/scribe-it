package grupo2.docubot.repository;

import grupo2.docubot.models.Role;
import grupo2.docubot.models.User;
import grupo2.docubot.models.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);
    //returns the number of rows affected
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.role = :newRole WHERE u.id = :id")
    int updateRoleById(@Param("id") Long id, @Param("newRole") Role newRole);

    //returns the number of rows affected
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.department = :newDepartment WHERE u.id = :id")
    int updateDepartmentById(@Param("id") Long id, @Param("newDepartment") String newDepartment);

    Optional<User> findByEmail(String email);

    List<User> findByDepartment(String department);

    @Query("SELECT DISTINCT u.department FROM User u ORDER BY u.department")
    List<String> findDistinctDepartments();

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name = :roleName")
    Optional<User> findByRole(@Param("roleName") String roleName);
}

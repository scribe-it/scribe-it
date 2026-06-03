package grupo2.docubot.repository;

import grupo2.docubot.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN User u WHERE u.id = :userId")
    List<Chat> findAllByUserId(@Param("userId") Long userId);

}

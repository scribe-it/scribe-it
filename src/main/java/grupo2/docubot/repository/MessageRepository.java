package grupo2.docubot.repository;

import grupo2.docubot.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findAllByChatId(Long chatId);

    long countByChatIdAndIsReadFalse(Long chatId);

    List<Message> findAllByChatIdAndIsReadFalse(Long chatId);

    Page<Message> findByChatIdAndContentContainingIgnoreCase(
            Long chatId,
            String content,
            Pageable pageable
    );
    Page<Message> findByChatIdAndTimestamp(
            Long chatId,
            LocalDateTime timestamp,
            Pageable pageable
    );

}

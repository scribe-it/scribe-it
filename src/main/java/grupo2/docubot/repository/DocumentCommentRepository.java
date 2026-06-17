package grupo2.docubot.repository;

import grupo2.docubot.models.DocumentComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentCommentRepository extends JpaRepository<DocumentComment, Long> {

    List<DocumentComment> findByDocumentIdOrderByCreatedAtAsc(Long documentId);
}

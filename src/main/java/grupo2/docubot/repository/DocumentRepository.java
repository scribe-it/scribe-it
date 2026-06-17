package grupo2.docubot.repository;

import grupo2.docubot.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findAllByPublishedFalse();

    List<Document> findAllByPublishedTrue();
}

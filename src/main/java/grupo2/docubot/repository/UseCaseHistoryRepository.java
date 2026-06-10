package grupo2.docubot.repository;

import grupo2.docubot.models.UseCaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseCaseHistoryRepository extends JpaRepository<UseCaseHistory,Long> {

}

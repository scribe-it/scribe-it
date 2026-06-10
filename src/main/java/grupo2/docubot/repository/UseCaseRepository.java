package grupo2.docubot.repository;

import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase,Long> {
}

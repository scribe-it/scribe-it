package grupo2.docubot.services;

import grupo2.docubot.dto.internal.AnalysisResponse;
import grupo2.docubot.dto.response.MessageResponseDto;
import grupo2.docubot.models.Message;
import grupo2.docubot.models.UseCase;
import grupo2.docubot.models.UseCaseHistory;
import grupo2.docubot.repository.UseCaseHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.stream.LangCollectors.collect;

@Service
@RequiredArgsConstructor
public class DocubotProcessingService {

    private final MessageProcesor messageProcesor;
    private final UserService userService;
    private final EmailService emailService;
    private final UseCaseService useCaseService;
    private final UseCaseHistoryRepository useCaseHistoryRepository;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedDelay = 60000)
    public AnalysisResponse checkAndProcess () {

        List<MessageResponseDto> unprocessedMessages = messageService.getAllDocubot();

        if(unprocessedMessages.size() > 0) {

        String standardizedMessages = normalizer(unprocessedMessages);

        AnalysisResponse response = messageProcesor.getAllMessages(standardizedMessages);

        if(response.use_cases != null && !response.use_cases.isEmpty()) {
            UseCaseHistory history =new UseCaseHistory();
            history.addHistory(2L,standardizedMessages);
            useCaseHistoryRepository.save(history);

            List<Long> processedMessagesIds = unprocessedMessages.stream().map(MessageResponseDto::getId).toList();
            messageService.markAsProcessed(processedMessagesIds);

            for(UseCase useCase: response.getUse_cases()){
                try {
                    useCase.setId(null);
                    useCase.setHistory(history);
                    useCaseService.save(useCase);
                } catch (Exception e) {
                    System.err.println("Error al guardar el caso de uso: " + e.getMessage());
                }
            }

            messagingTemplate.convertAndSend(
                    "/topic/analysis",
                    response
            );

            String analystEmail = userService.findAnalyst().getEmail();

            emailService.sendNotification(analystEmail);

        }else{
            System.out.println("No se encontro casos de uso");
            return null;
        }

        return response;
        }

        return null;
    };

    public String normalizer(List<MessageResponseDto> messages){
        return messages.stream()
                .map(m ->  userService.findById(m.getUser_id()).getFirstName() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));
    }
}

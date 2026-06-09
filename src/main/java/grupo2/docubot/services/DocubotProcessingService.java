package grupo2.docubot.services;

import grupo2.docubot.dto.internal.AnalysisResponse;
import grupo2.docubot.dto.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocubotProcessingService {

    private final MessageProcesor messageProcesor;
    private final UserService userService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(cron = "0 * * * * *")
    public AnalysisResponse checkAndProcess () {

        List<MessageResponseDto> unprocessedMessages = messageService.getAllDocubot();
        AnalysisResponse response = messageProcesor.getAllMessages(unprocessedMessages.stream()
                .map(m -> userService.findById(m.getUser_id()).getFirstName() + " " + m.getContent())
                .collect(Collectors.joining("\n")));
        if(response.use_cases != null && !response.use_cases.isEmpty()) {
            List<Long> processedMessagesIds = unprocessedMessages.stream().map(MessageResponseDto::getId).toList();
            messageService.markAsProcessed(processedMessagesIds);

            messagingTemplate.convertAndSend(
                    "/topic/analysis",
                    response
            );
        }

        return response;
    };
}
